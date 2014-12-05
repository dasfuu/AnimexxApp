package de.meisterfuu.animexx.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Meisterfuu on 09.10.2014.
 */
public class KarotalerStatsObject {

    //	- kt_abholbar: Objekt-Array / Es ist möglich, dass mehrere KT-Buchungen abholbar sind, z.B. wenn neben den täglichen KTs noch Preise von Gewinnspielen dazu kommen. In solchen Fällen hat das Array mehr als einen Eintrag. Man kann sie nur gesammelt abholen, aber auf diese Weise lässt sich genauer aufschlüsseln, wie sich der abholbare Betrag zusammensetzt. Die Objekte des Arrays haben folgende Werte: kt (int), grund (string), datum_server u. datum_utc
    @SerializedName("kt_guthaben")
    private List<KarotalerPickupObject> pickup;

    //	- kt_guthaben: int
    @SerializedName("kt_guthaben")
    private long balance;

    public List<KarotalerPickupObject> getPickup() {
        return pickup;
    }

    public void setPickup(final List<KarotalerPickupObject> pPickup) {
        pickup = pPickup;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(final long pBalance) {
        balance = pBalance;
    }

    private static class KarotalerPickupObject {

        @SerializedName("kt")
        private long value;
        @SerializedName("grund")
        private String reason;
        @SerializedName("datum_server")
        private String dateServer;
        @SerializedName("datum_utc")
        private String dateUtc;


        public long getValue() {
            return value;
        }

        public void setValue(final long pValue) {
            value = pValue;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(final String pReason) {
            reason = pReason;
        }

        public String getDateServer() {
            return dateServer;
        }

        public void setDateServer(final String pDateServer) {
            dateServer = pDateServer;
        }

        public String getDateUtc() {
            return dateUtc;
        }

        public void setDateUtc(final String pDateUtc) {
            dateUtc = pDateUtc;
        }
    }
}
