package de.meisterfuu.animexx.objects.ens;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.api.broker.ENSBroker;


public class ENSFolderObject {

    @SerializedName("ordner_id")
    long id;

    String type;

    @SerializedName("name")
    String name;

    @SerializedName("gesamt")
    int count;

    @SerializedName("ungelesen")
    int unread;

    public ENSFolderObject() {

    }


    @Override
    public String toString() {
        return this.getName();
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


    public int getCount() {
        return count;
    }


    public void setCount(int count) {
        this.count = count;
    }


    public int getUnread() {
        return unread;
    }


    public void setUnread(int unread) {
        this.unread = unread;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }

    public static class ENSFolderObjectContainer {

        @SerializedName("an")
        List<ENSFolderObject> in;

        @SerializedName("von")
        List<ENSFolderObject> out;

        public ENSFolderObjectContainer() {

        }

        public List<ENSFolderObject> getIn() {
            return in;
        }

        public void setIn(List<ENSFolderObject> in) {
            this.in = in;
        }

        public List<ENSFolderObject> getOut() {
            return out;
        }

        public void setOut(List<ENSFolderObject> out) {
            this.out = out;
        }

        public List<ENSFolderObject> getAll() {
            List<ENSFolderObject> all = new ArrayList<ENSFolderObject>();
            for (ENSFolderObject obj : in) {
                obj.setType(ENSBroker.TYPE_INBOX);
            }
            for (ENSFolderObject obj : out) {
                obj.setType(ENSBroker.TYPE_OUTBOX);
            }
            all.addAll(in);
            all.addAll(out);
            return all;
        }
    }

}
