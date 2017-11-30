package com.berniac.vocalwarmup.ui.data;

/**
 * Created by Mikhail Lipkovich on 11/25/2017.
 */
public class WarmUpCategory {
    private String title;
    private int imageId;

    public WarmUpCategory(){}

    public WarmUpCategory(String name, int imageId) {
        this.title = name;
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
