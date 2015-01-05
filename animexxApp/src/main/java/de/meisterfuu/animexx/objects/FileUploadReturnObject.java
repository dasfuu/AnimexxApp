package de.meisterfuu.animexx.objects;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;

import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileUploadReturnObject {

    @SerializedName("id")
    long id;

    //        "filename":"\/files\/03---Art.png",
    @SerializedName("filename")
    String filename;

    @SerializedName("url_share")
    String urlShare;

    @SerializedName("url_intern")
    String urlIntern;

    @SerializedName("url_thumb")
    String urlThumb;

    //            "mime":"image\/png",
    @SerializedName("mime")
    String mime;

    @SerializedName("etag")
    String etag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrlShare() {
        return urlShare;
    }

    public void setUrlShare(String urlShare) {
        this.urlShare = urlShare;
    }

    public String getUrlIntern() {
        return urlIntern;
    }

    public void setUrlIntern(String urlIntern) {
        this.urlIntern = urlIntern;
    }

    public String getUrlThumb() {
        return urlThumb;
    }

    public void setUrlThumb(String urlThumb) {
        this.urlThumb = urlThumb;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    @Override
    public String toString() {
        return "FileUploadReturnObject{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", urlShare='" + urlShare + '\'' +
                ", urlIntern='" + urlIntern + '\'' +
                ", urlThumb='" + urlThumb + '\'' +
                ", mime='" + mime + '\'' +
                ", etag='" + etag + '\'' +
                '}';
    }
}
