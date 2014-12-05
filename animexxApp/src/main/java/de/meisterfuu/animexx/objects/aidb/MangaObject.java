package de.meisterfuu.animexx.objects.aidb;

import com.google.gson.annotations.SerializedName;

public class MangaObject {

    @SerializedName("name")
    private String name;
    @SerializedName("beschreibung")
    private String beschreibung;
    @SerializedName("isbn")
    private String isbn;
    @SerializedName("ean")
    private String ean;
    @SerializedName("dtl_erschienen")
    private String releaseDate;
    @SerializedName("last_changed")
    private String lastChanged;
    @SerializedName("id")
    private long mangaId;
    @SerializedName("serie")
    private long seriesId;
    @SerializedName("band")
    private int volumeNumber;
    @SerializedName("seiten")
    private long pages;
    @SerializedName("dtl_richtung")
    private int readingDirection;

    @SerializedName("dtl_monat")
    private int deMonth;
    @SerializedName("dtl_jahr")
    private int deYear;
    @SerializedName("jp_jahr")
    private long jpReleaseYear;
    @SerializedName("dtl_verlag")
    private long gerPublisherId;
    @SerializedName("dtl_preis")
    private long dtlPfennig;
    @SerializedName("dtl_euro")
    private long dtlEuro;
    @SerializedName("dtl_status")
    private int dtlStatus;

    @SerializedName("last_changed")
    private String coverUrl;
    @SerializedName("cover_highres")
    private String coverHqUrl;

    public String getName() {
        return name;
    }

    public void setName(final String pName) {
        name = pName;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(final String pBeschreibung) {
        beschreibung = pBeschreibung;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(final String pIsbn) {
        isbn = pIsbn;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(final String pEan) {
        ean = pEan;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(final String pReleaseDate) {
        releaseDate = pReleaseDate;
    }

    public String getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(final String pLastChanged) {
        lastChanged = pLastChanged;
    }

    public long getMangaId() {
        return mangaId;
    }

    public void setMangaId(final long pMangaId) {
        mangaId = pMangaId;
    }

    public long getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(final long pSeriesId) {
        seriesId = pSeriesId;
    }

    public int getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(final int pVolumeNumber) {
        volumeNumber = pVolumeNumber;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(final long pPages) {
        pages = pPages;
    }

    public int getReadingDirection() {
        return readingDirection;
    }

    public void setReadingDirection(final int pReadingDirection) {
        readingDirection = pReadingDirection;
    }

    public int getDeMonth() {
        return deMonth;
    }

    public void setDeMonth(final int pDeMonth) {
        deMonth = pDeMonth;
    }

    public int getDeYear() {
        return deYear;
    }

    public void setDeYear(final int pDeYear) {
        deYear = pDeYear;
    }

    public long getJpReleaseYear() {
        return jpReleaseYear;
    }

    public void setJpReleaseYear(final long pJpReleaseYear) {
        jpReleaseYear = pJpReleaseYear;
    }

    public long getGerPublisherId() {
        return gerPublisherId;
    }

    public void setGerPublisherId(final long pGerPublisherId) {
        gerPublisherId = pGerPublisherId;
    }

    public long getDtlPfennig() {
        return dtlPfennig;
    }

    public void setDtlPfennig(final long pDtlPfennig) {
        dtlPfennig = pDtlPfennig;
    }

    public long getDtlEuro() {
        return dtlEuro;
    }

    public void setDtlEuro(final long pDtlEuro) {
        dtlEuro = pDtlEuro;
    }

    public int getDtlStatus() {
        return dtlStatus;
    }

    public void setDtlStatus(final int pDtlStatus) {
        dtlStatus = pDtlStatus;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(final String pCoverUrl) {
        coverUrl = pCoverUrl;
    }

    public String getCoverHqUrl() {
        return coverHqUrl;
    }

    public void setCoverHqUrl(final String pCoverHqUrl) {
        coverHqUrl = pCoverHqUrl;
    }
}