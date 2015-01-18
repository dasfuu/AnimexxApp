package de.meisterfuu.animexx.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import de.meisterfuu.animexx.Debug;
import de.meisterfuu.animexx.api.Self;
import de.meisterfuu.animexx.api.broker.ENSBroker;

public class Helper {

    public static void sendStacTrace(Exception e, Context con) {
        if (!Debug.SEND_STACKTRACE_ENS) {
            return;
        }

        StackTraceElement[] stack = e.getStackTrace();

        String text = new String();
        for (StackTraceElement line : stack) {
            text += line.toString() + "\n";
        }

        String s = "(" + Self.getInstance(con).getUserID() + ") " + Self.getInstance(con).getUsername() + ":";
        s += "\n\n";
        s += e.getMessage();
        s += "\n\n";
        s += "" + text;
        //Request.sendENS("Debug Log", s, "Debug Log "+Constants.VERSION, new int[]{586283}, -1);
        ENSBroker.sendENSDEBUG(s, "StacTrace", con);
        Log.i("Debug", "Debug Log gesendet");
        //Request.doToast("Debug Log gesendet!", con);
    }

    public static String BetreffRe(String Betreff) {
        String s;
        String begin, mid, end;

        if (Betreff.startsWith("Re:") == true) {
            s = "Re[2]:" + Betreff.substring(3);
            return s;
        }

        int index = Betreff.indexOf("]:");
        int count = 0;
        if (index > 1) {
            end = "]:";
            mid = Betreff.substring(3, index);
            begin = Betreff.substring(0, 3);
            if ((Pattern.matches("\\d+", mid) == true) && (begin.equals("Re[")) && end.equals("]:")) {
                try {
                    count = Integer.parseInt(mid) + 1;
                    return begin + count + end + Betreff.substring(index + 2);
                } catch (Exception e) {
                    s = "Re:" + Betreff;
                    return s;
                }
            }
        }

        return "Re:" + Betreff;
    }


    public static void Tutorial(String text, String config, Context c) {
        SharedPreferences cfg = PreferenceManager.getDefaultSharedPreferences(c);
        if (!cfg.getBoolean(config, false)) {
            AlertDialog alertDialog = new AlertDialog.Builder(c).create();
            alertDialog.setMessage(text);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    //
                }
            });
            alertDialog.show();

            final Editor edit = cfg.edit();
            edit.putBoolean(config, true);
            edit.commit();
        }
    }

    public static String agoString(long time, boolean Short) {


        //java.util.Date Now = new java.util.Date();
        Calendar Now = Calendar.getInstance();

        long diff = (Now.getTimeInMillis() - time);
        if (diff < 0) return "Gerade eben";

        diff /= 1000;

        if (diff < 60) {
            if (Short)
                return "vor " + diff + "s";
            else if ((diff) < 2)
                return "vor einer Sekunde";
            else
                return "vor " + (diff) + " Sekunden";
        } else if (diff < 3600) {
            if (Short)
                return "vor " + (diff / 60) + "min";
            else if ((diff / 60) < 2)
                return "vor einer Minute";
            else
                return "vor " + (int) (diff / 60) + " Minuten";
        } else if (diff < 86400) {
            if (Short)
                return "vor " + (diff / 60 / 60) + "h";
            else if ((diff / 60 / 60) < 2)
                return "vor einer Stunde";
            else
                return "vor " + (int) (diff / 60 / 60) + " Stunden";
        } else {
            if (Short)
                return "vor " + (diff / 60 / 60 / 24) + "d";
            else if ((diff / 60 / 60 / 24) < 2)
                return "vor einem Tag";
            else
                return "vor " + (int) (diff / 60 / 60 / 24) + " Tagen";
        }

    }

    public static String toDateString(long timestamp){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        return sdf.format(timestamp);
    }

    public static String toTimeString(long timestamp){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm", Locale.GERMANY);
        return sdf.format(timestamp);
    }

    public static String toDateTimeString(long timestamp){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY);
        return sdf.format(timestamp);
    }

    public static long toTimestamp(String utcString){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(utcString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long dateToTimestamp(String utcString){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(utcString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long toTimestamp(String date, String format) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format, Locale.GERMANY);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        java.util.Date temp;

        try {
            temp = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
        return temp.getTime();
    }

}
