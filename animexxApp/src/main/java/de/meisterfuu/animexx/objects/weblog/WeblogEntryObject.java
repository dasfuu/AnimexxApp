package de.meisterfuu.animexx.objects.weblog;

import com.google.gson.annotations.SerializedName;

import de.meisterfuu.animexx.objects.UserObject;

public class WeblogEntryObject {

    @SerializedName("id")
    private long id;
    @SerializedName("typ")
    private String type;
    @SerializedName("entwurf")
    private boolean draft;
    @SerializedName("titel")
    private String title;
    @SerializedName("anzKommentare")
    private long comments;
    @SerializedName("sprache")
    private String language;
    @SerializedName("text")
    private String textShort;
    @SerializedName("textLang")
    private String textLong;
    @SerializedName("datum")
    private String datum;
    @SerializedName("mitglied")
    private UserObject author;
    @SerializedName("weblogOwner")
    private UserObject owner;
    @SerializedName("abonniert")
    private boolean abonniert;

    public long getId() {
        return id;
    }

    public void setId(final long pId) {
        id = pId;
    }

    public String getType() {
        return type;
    }

    public void setType(final String pType) {
        type = pType;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(final boolean pDraft) {
        draft = pDraft;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String pTitle) {
        title = pTitle;
    }

    public long getComments() {
        return comments;
    }

    public void setComments(final long pComments) {
        comments = pComments;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String pLanguage) {
        language = pLanguage;
    }

    public String getTextShort() {
        return textShort;
    }

    public void setTextShort(final String pTextShort) {
        textShort = pTextShort;
    }

    public String getTextLong() {
        return textLong;
    }

    public void setTextLong(final String pTextLong) {
        textLong = pTextLong;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(final String pDatum) {
        datum = pDatum;
    }

    public UserObject getAuthor() {
        return author;
    }

    public void setAuthor(final UserObject pAuthor) {
        author = pAuthor;
    }

    public UserObject getOwner() {
        return owner;
    }

    public void setOwner(final UserObject pOwner) {
        owner = pOwner;
    }

    public boolean isAbonniert() {
        return abonniert;
    }

    public void setAbonniert(final boolean pAbonniert) {
        abonniert = pAbonniert;
    }
}