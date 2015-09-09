package de.meisterfuu.animexx.objects.aidb;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Furuha on 01.07.2015.
 */
public class EANResultObject {

//    "typ":"manga",
    @SerializedName("typ")
    private String type;

//    "id":10181,
    @SerializedName("id")
    private long id;

//    "name":"Namida Usagi - Tr\u00e4nenhase - Band 1",
    @SerializedName("name")
    private String name;

//    "url":"http:\/\/animexx.onlinewelten.com\/aidb\/manga-deutschland.phtml\/10181_Namida_Usagi_-_Traenenhase_-_Band_1",
    @SerializedName("url")
    private String url;

//    "image":"https:\/\/media.animexx.de\/images\/download\/artikel\/manga\/10181\/logo_gross.jpg-1389633656-300-500-95.jpg?st=b9vBDa7QYWepiuZz4Gp3Ug&e=1433770200"
    @SerializedName("image")
    private String imageUrl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
