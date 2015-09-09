package de.meisterfuu.animexx.objects.aidb;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyMangaObject {


//        "id":339, Serien-ID
//        "name":"3x3 Augen",
//            "baende":[
//        {
//            "id":729, Manga-ID
//            "band":1, Bei One-Shots meistens "0"
//            "name":"Band 1" Meistens "Band" + band, kann aber auch der Titel des Einzelbands sein
//          }

    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private long mangaId;
    @SerializedName("baende")
    private ArrayList<Volume> volumes;

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

    @Override
    public String toString() {
        return name;
    }

    public static class Volume{
        @SerializedName("name")
        private String name;
        @SerializedName("id")
        private long mangaId;
        @SerializedName("band")
        private int volume;

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