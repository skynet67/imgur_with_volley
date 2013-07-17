package com.masterofcode.imgur_and_volley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Adapter that uses Volley NetworkImageView for showing images
 */
public class ImgurAdapter extends ArrayAdapter<ImgurImage> {

    private ImageLoader loader;

    public ImgurAdapter(Context context, int textViewResourceId, List<ImgurImage> objects, ImageLoader loader) {
        super(context, textViewResourceId, objects);
        this.loader = loader;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.img_item, parent, false);
        NetworkImageView imageView = (NetworkImageView) rowView.findViewById(R.id.image);
        TextView textView = (TextView) rowView.findViewById(R.id.title);

        ImgurImage img = getItem(position);

        imageView.setDefaultImageResId(R.drawable.no_image);
        imageView.setErrorImageResId(R.drawable.no_image);
        imageView.setImageUrl(img.getUrl(), loader);
        textView.setText(img.getTitle());

        return rowView;
    }
}
