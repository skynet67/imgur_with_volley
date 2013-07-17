package com.masterofcode.imgur_and_volley;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Test activity
 */
public class MainActivity extends ListActivity {

    private List<ImgurImage> mLoadedImages;

    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getString(R.string.imgur_clientID).equals("YOUR IMGUR CLIENT ID")) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setTitle("Error")
                    .setMessage("Please set your imgur client id in the settings.xml")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    })
                    .create()
                    .show();
        } else {
            getImagesData();
        }
    }

    private void initAdapter() {
//        mImageLoader = new ImageLoader(mQueue, new ImageLoader.ImageCache() {
//            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
//
//            public void putBitmap(String url, Bitmap bitmap) {
//                mCache.put(url, bitmap);
//            }
//
//            public Bitmap getBitmap(String url) {
//                return mCache.get(url);
//            }
//        });
        mImageLoader = new ImageLoader(mQueue, new BitmapLruCache());

        ListView list = getListView();
        ImgurAdapter adapter = new ImgurAdapter(this, R.layout.img_item, mLoadedImages, mImageLoader);
        list.setAdapter(adapter);
    }

    private void getImagesData() {

        mQueue = Volley.newRequestQueue(this);
        String url = "https://api.imgur.com/3/gallery/random/random/0";

        ImgurRequest jsonObjectRequest = new ImgurRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJSONRespone(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", error.toString());
                        mProgress.dismiss();
                        getImagesData(); //sometimes Volley falls into  E/ERROR: com.android.volley.NoConnectionError: java.io.EOFException
                    }
                }
        );


        jsonObjectRequest.setImgurClientId(getString(R.string.imgur_clientID));
        mQueue.add(jsonObjectRequest);

        mProgress = ProgressDialog.show(this, "Please wait.", "Loading images.");
    }

    private void parseJSONRespone(JSONObject response) {
        try {
            int status = response.getInt("status");
            if (status == 200) {

                JSONArray arr = response.getJSONArray("data");
                final int len = (arr != null) ? arr.length() : 0;

                mLoadedImages = new ArrayList<ImgurImage>(len);

                for (int i = 0; i < len; i++, i++) {
                    JSONObject json = arr.getJSONObject(i);
                    ImgurImage img = new ImgurImage();

                    img.setUrl(json.getString("link").replace("\\", ""));
                    img.setTitle(json.getString("title"));

                    mLoadedImages.add(img);
                }
            }

            mProgress.dismiss();

            initAdapter();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
