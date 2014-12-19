package de.meisterfuu.animexx.utils.imageloader;


import android.support.v7.graphics.Palette;

public class ImageSaveObject {

    String url;
    String name;
    Palette palette;
    boolean usePalette;


    public ImageSaveObject(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public ImageSaveObject(String url, String name, boolean usePalette) {
        this.url = url;
        this.name = name;
        this.usePalette = usePalette;
    }

    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }

    public boolean isUsePalette() {
        return usePalette;
    }

    public void setUsePalette(boolean usePalette) {
        this.usePalette = usePalette;
    }

    @Override
    public String toString() {
        return this.getUrl();
    }

}
