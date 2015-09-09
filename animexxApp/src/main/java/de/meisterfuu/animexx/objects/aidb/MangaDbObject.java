package de.meisterfuu.animexx.objects.aidb;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Furuha on 08.09.2015.
 */
public class MangaDbObject {

    @DatabaseField(id = true)
    long mangaId;

    @DatabaseField(columnName = "seriedId")
    long seriedId;

    @DatabaseField
    String seriesName;

    @DatabaseField
    int volume;

    @DatabaseField
    String name;

    @DatabaseField
    boolean inPossession;

    @DatabaseField
    String ean;

    public long getMangaId() {
        return mangaId;
    }

    public void setMangaId(long mangaId) {
        this.mangaId = mangaId;
    }

    public long getSeriedId() {
        return seriedId;
    }

    public void setSeriedId(long seriedId) {
        this.seriedId = seriedId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInPossession() {
        return inPossession;
    }

    public void setInPossession(boolean inPossession) {
        this.inPossession = inPossession;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    @Override
    public String toString(){
        return this.getName() + " ("+this.getVolume()+")";
    }
}
