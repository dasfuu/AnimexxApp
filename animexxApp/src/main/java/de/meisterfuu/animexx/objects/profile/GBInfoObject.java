package de.meisterfuu.animexx.objects.profile;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class GBInfoObject {

    //	- freigabe_lesen, boolean / hat der Aktuelle Benutzer Leserechte?
    @SerializedName("freigabe_lesen")
    boolean readable;

    //	- freigabe_schreiben, boolean / hat der Aktuelle Benutzer Schreibrechte?
    @SerializedName("freigabe_schreiben")
    boolean writeable;

    //	- gb_anzahl: int / Anzahl der bisherigen GB-Einträge
    @SerializedName("gb_anzahl")
    int entryCount;

    //	- kann_avatar: boolean / können Avatare verwendet werden? (Abhängig vom Tofu des GB-Inhabers, des aktuellen Benutzers sowie den GB-Einstellungen)
    @SerializedName("kann_avatar")
    boolean avatarUseable;

    //	- avatare: array()
    @SerializedName("avatare")
    ArrayList<GBAvatar> avatars;

    static public class GBAvatar {
        //		 - id / int
        @SerializedName("id")
        long id;

        //		 - width / int
        @SerializedName("width")
        int width;

        //		 - height / int
        @SerializedName("height")
        int height;

        //		 - url / string / nur wenige Minuten gültig
        @SerializedName("url")
        String url;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public boolean isReadable() {
        return readable;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    public boolean isWriteable() {
        return writeable;
    }

    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }

    public int getEntryCount() {
        return entryCount;
    }

    public void setEntryCount(int entryCount) {
        this.entryCount = entryCount;
    }

    public boolean isAvatarUseable() {
        return avatarUseable;
    }

    public void setAvatarUseable(boolean avatarUseable) {
        this.avatarUseable = avatarUseable;
    }

    public ArrayList<GBAvatar> getAvatars() {
        return avatars;
    }

    public void setAvatars(ArrayList<GBAvatar> avatars) {
        this.avatars = avatars;
    }

}
