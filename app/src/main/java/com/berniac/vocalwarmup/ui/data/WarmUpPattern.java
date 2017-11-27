package com.berniac.vocalwarmup.ui.data;

/**
 * Created by Mikhail Lipkovich on 11/25/2017.
 */
public class WarmUpPattern {
    private String title;
    private int imageId;
    private int playButtonId;

    public WarmUpPattern(){}

    public WarmUpPattern(String title, int imageId, int playButtonId) {
        this.title = title;
        this.imageId = imageId;
        this.playButtonId = playButtonId;
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

    public int getPlayButtonId() {
        return playButtonId;
    }

    public void setPlayButtonId(int playButtonId) {
        this.playButtonId = playButtonId;
    }
}
