package de.meisterfuu.animexx.objects.aidb;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MangaSeriesObject {


    @SerializedName("serie_name")
    private String name;
    @SerializedName("thema_name")
    private String themeName;
    @SerializedName("thema")
    private long themeId;
    @SerializedName("typ")
    private int type;
    @SerializedName("id")
    private long mangaId;
    @SerializedName("baende")
    private ArrayList<Volume> volumes;

    //    "jp_verlag":"Media Works",
    @SerializedName("jp_verlag")
    private String publisherJp;
    //    "dt_verlag":23,
    @SerializedName("dt_verlag")
    private int publisherDe;
    //    "anz_baende":5,
    @SerializedName("anz_baende")
    private int volumeCount;
    //    "dtl_angeschlossen":0
    @SerializedName("personen")
    private JsonObject person;

    @SerializedName("dtl_angeschlossen")
    private int dtl_angeschlossen;

    @SerializedName("ap_intro")
    private String apIntro;
    @SerializedName("ap_img")
    private String apImageUrl;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMangaId() {
        return mangaId;
    }

    public void setMangaId(long mangaId) {
        this.mangaId = mangaId;
    }

    public ArrayList<Volume> getVolumes() {
        return volumes;
    }

    public void setVolumes(ArrayList<Volume> volumes) {
        this.volumes = volumes;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public long getThemeId() {
        return themeId;
    }

    public void setThemeId(long themeId) {
        this.themeId = themeId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPublisherJp() {
        return publisherJp;
    }

    public void setPublisherJp(String publisherJp) {
        this.publisherJp = publisherJp;
    }

    public int getPublisherDe() {
        return publisherDe;
    }

    public void setPublisherDe(int publisherDe) {
        this.publisherDe = publisherDe;
    }

    public int getVolumeCount() {
        return volumeCount;
    }

    public void setVolumeCount(int volumeCount) {
        this.volumeCount = volumeCount;
    }

    public JsonObject getPerson() {
        return person;
    }

    public void setPerson(JsonObject person) {
        this.person = person;
    }

    public int getDtl_angeschlossen() {
        return dtl_angeschlossen;
    }

    public void setDtl_angeschlossen(int dtl_angeschlossen) {
        this.dtl_angeschlossen = dtl_angeschlossen;
    }

    public String getApImageUrl() {
        return apImageUrl;
    }

    public void setApImageUrl(String apImageUrl) {
        this.apImageUrl = apImageUrl;
    }

    public String getApIntro() {
        return apIntro;
    }

    public void setApIntro(String apIntro) {
        this.apIntro = apIntro;
    }

    @Override
    public String toString() {
        return name +" ("+mangaId+")";
    }

    public static class Volume{
        @SerializedName("band_name")
        private String name;
        @SerializedName("id")
        private long mangaId;
        @SerializedName("band")
        private int volume;
//        "erschienen":"2010-08-23",
        @SerializedName("erschienen")
        private String release;
//        "angekuendigt":"2010-12",
        @SerializedName("angekuendigt")
        private String announced;
//        "cover":"https:\/\/media.animexx.de\/images\/download\/artikel\/manga\/7237\/logo_gross.jpg-943916400-200-300-95.jpg?st=IRKgAd64hF0LFVJ-a8rwag&e=1378738800",
        @SerializedName("cover")
        private String coverUrl;
//        "cover_highres":"https:\/\/media.animexx.de\/images\/download\/artikel\/manga\/7237\/logo_gross.jpg-943916400-400-600-95.jpg?st=9OmJEm_fkCas0-8fqYkERQ&e=1378738800"
        @SerializedName("cover_highres")
        private String coverUrlHighRes;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getMangaId() {
            return mangaId;
        }

        public void setMangaId(long mangaId) {
            this.mangaId = mangaId;
        }

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        public String getRelease() {
            return release;
        }

        public void setRelease(String release) {
            this.release = release;
        }

        public String getAnnounced() {
            return announced;
        }

        public void setAnnounced(String announced) {
            this.announced = announced;
        }

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public String getCoverUrlHighRes() {
            return coverUrlHighRes;
        }

        public void setCoverUrlHighRes(String coverUrlHighRes) {
            this.coverUrlHighRes = coverUrlHighRes;
        }

        @Override
        public String toString() {
            return "Volume{" +
                    "name='" + name + '\'' +
                    ", mangaId=" + mangaId +
                    ", volume='" + volume + '\'' +
                    '}';
        }
    }

}