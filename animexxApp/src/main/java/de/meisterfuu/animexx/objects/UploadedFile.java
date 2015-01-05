package de.meisterfuu.animexx.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Furuha on 04.01.2015.
 */
public class UploadedFile {

//        "filename":"\/files\/03---Art.png",
    @SerializedName("filename")
    String filename;

//            "url":"https:\/\/media.animexx.de\/himitsu\/mitglieder\/files\/2\/2\/files\/03---Art.png?st=abcdefghi&e=12345678",
    @SerializedName("url")
    String url;

//            "mime":"image\/png",
    @SerializedName("mime")
    String mime;

//            "size":1156701,
    @SerializedName("size")
    int size;

//            "img_width":1280,
    @SerializedName("img_width")
    int imageWidth;

//            "img_height":720,
    @SerializedName("img_height")
    int imageHeight;

//            "uploaded_server":"2014-02-14 16:04:57",
    @SerializedName("uploaded_server")
    String uploadTimeServer;

//            "uploaded_utc":"2014-02-14 15:04:57"
    @SerializedName("uploaded_utc")
    String uploadTimeUtc;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getUploadTimeServer() {
        return uploadTimeServer;
    }

    public void setUploadTimeServer(String uploadTimeServer) {
        this.uploadTimeServer = uploadTimeServer;
    }

    public String getUploadTimeUtc() {
        return uploadTimeUtc;
    }

    public void setUploadTimeUtc(String uploadTimeUtc) {
        this.uploadTimeUtc = uploadTimeUtc;
    }
}
