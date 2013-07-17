package com.masterofcode.imgur_and_volley;

/**
 * Simple class for storing imgur data from the json response
 */
public class ImgurImage {

    private String url;
    private String title;

    public ImgurImage(){
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
