package de.meisterfuu.animexx.objects.rpg;

public class RPGDraftObject {

    long rpgID;

    //	- charakter, int / benötigt / Die ID des Charakters
    long charaID;

    //	- text, string / benötigt / Der Text
    String text;

    //	- avatar, int / optional / Die ID des Avatars. Kann nur bei Vertofuzierten RPGs angegeben werden.
    long avatarID;

    //	- kursiv, int / optional / Falls angegeben und "1", dann wird das Posting kursiv.
    int kursiv;

    //	- intime, int / optional / Falls angegeben und ungleich "1", dann wird es ein Out-Time-Posting. Standard: In-Time.
    int inTime;

//	- metadatum, array / optional / Die Metadaten des Postings als assoziatives Array. Es können nur diejenigen Angaben als Schlüssel des Arrays verwendet werden, die auch in /rpg/rpg_details/ angezeigt werden. In der URL sieht es z.B. so aus: /rpg/erstelle_posting/?rpg=123&metadatum[Ort]=Irgendwo&metdatum[Zeit]=Samstag&...


    public long getCharaID() {
        return charaID;
    }

    public void setCharaID(long charaID) {
        this.charaID = charaID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getAvatarID() {
        return avatarID;
    }

    public void setAvatarID(long avatarID) {
        this.avatarID = avatarID;
    }

    public int getKursiv() {
        return kursiv;
    }

    public void setKursiv(int kursiv) {
        this.kursiv = kursiv;
    }

    public int getInTime() {
        return inTime;
    }

    public void setInTime(int inTime) {
        this.inTime = inTime;
    }

    public long getRpgID() {
        return rpgID;
    }

    public void setRpgID(long rpgID) {
        this.rpgID = rpgID;
    }


}
