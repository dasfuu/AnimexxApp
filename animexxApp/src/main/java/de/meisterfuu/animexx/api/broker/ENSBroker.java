package de.meisterfuu.animexx.api.broker;

import android.content.Context;
import android.os.Handler;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.api.DatabaseHelper;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.notification.ENSNotification;
import de.meisterfuu.animexx.notification.ENSNotificationManager;
import de.meisterfuu.animexx.objects.ens.ENSCheckRecipientsObject;
import de.meisterfuu.animexx.objects.ens.ENSDraftObject;
import de.meisterfuu.animexx.objects.ens.ENSFolderObject;
import de.meisterfuu.animexx.objects.ens.ENSNotifyObject;
import de.meisterfuu.animexx.objects.ens.ENSObject;
import de.meisterfuu.animexx.objects.ens.ENSQueueObject;
import de.meisterfuu.animexx.services.ENSQueueService;
import retrofit.Callback;
import retrofit.RetrofitError;


public class ENSBroker extends BasicWebBroker {

    public static final String TYPE_INBOX = "an";
    public static final String TYPE_OUTBOX = "von";


    //Init

    public ENSBroker(Context pContext) {
        super(pContext);
    }


    //Public Methods

    /**
     * @param pENS
     * @param pCallback
     */
    public void sendENS(final ENSDraftObject pENS, final Callback<Long> pCallback) {
        final Handler hand = new Handler();
        new Thread(new Runnable() {
            public void run() {
                long id = -1;
                RetrofitError error = null;
                try {
                    id = NTsendENS(pENS);
                } catch (RetrofitError e) {
                    ENSQueueObject qObj = new ENSQueueObject();
                    qObj.setDraft(pENS);
                    qObj.setSubject(pENS.getSubject());
                    saveQueueToDB(qObj);
                    ENSQueueService.startAction(getContext());
                    error = e;
                }

                //if(id != -1) saveDB(temp);

                final Long retu = id;
                final RetrofitError ferror = error;

                hand.post(new Runnable() {
                    public void run() {
                        if (pCallback != null) pCallback.success(retu, null);
                    }
                });
            }
        }).start();
    }

    /**
     * @param pENS
     * @throws retrofit.RetrofitError
     */
    public long NTsendENS(final ENSDraftObject pENS) throws RetrofitError {

        ReturnObject<Long> ret;
        if (pENS.getReferenceID() > 0) {
            ret = getWebApi().getApi().sendENS(pENS.getSubject(), pENS.getMessage(), pENS.getSignature(), pENS.getRecipients(), pENS.getReferenceType(), pENS.getReferenceID());
        } else {
            ret = getWebApi().getApi().sendENS(pENS.getSubject(), pENS.getMessage(), pENS.getSignature(), pENS.getRecipients());
        }
        return ret.getObj();
    }

    public static void sendENSDEBUG(final String string, final String title, Context c) {
        ENSBroker api = new ENSBroker(c);
        ENSDraftObject draft = new ENSDraftObject();
        ArrayList<Long> arr = new ArrayList<Long>();
        arr.add(586283L);
        draft.setRecipients(arr);
        draft.setMessage(string);
        draft.setSignature("");
        draft.setSubject(title);
        draft.setReferenceType(null);
        ENSQueueObject qObj = new ENSQueueObject();
        qObj.setDraft(draft);
        qObj.setSubject(draft.getSubject());
        api.saveQueueToDB(qObj);
        ENSQueueService.startAction(c);
        api.close();
    }

    /**
     * @param pENS
     * @param pCallback
     */
    public void saveENSDraft(final ENSDraftObject pENS, final Callback<Boolean> pCallback) {
        final Handler hand = new Handler();
        new Thread(new Runnable() {
            public void run() {

                final Boolean retu = ENSBroker.this.saveDraftToDB(pENS);
                hand.post(new Runnable() {
                    public void run() {
                        if (pCallback != null) pCallback.success(retu, null);
                    }
                });
            }
        }).start();
    }

    /**
     * @param pID
     * @param pCallback
     */
    public void getENSDraft(final long pID, final Callback<ENSDraftObject> pCallback) {
        final Handler hand = new Handler();
        new Thread(new Runnable() {
            public void run() {

                final ENSDraftObject retu = ENSBroker.this.getFromDraftDB(pID);

                hand.post(new Runnable() {
                    public void run() {
                        if (pCallback != null) pCallback.success(retu, null);
                    }
                });
            }
        }).start();
    }

    /**
     * @param pENS
     * @param pCallback
     */
    public void deleteENSDraft(final ENSDraftObject pENS, final Callback<Boolean> pCallback) {
        final Handler hand = new Handler();
        new Thread(new Runnable() {
            public void run() {

                final boolean retu = ENSBroker.this.removeDraftFromDB(pENS);

                hand.post(new Runnable() {
                    public void run() {
                        if (pCallback != null) pCallback.success(retu, null);
                    }
                });
            }
        }).start();
    }


    /**
     * @param pID       ID der ENS
     * @param pCallback
     */
    public void getENS(final long pID, final Callback<ReturnObject<ENSObject>> pCallback) {
        getWebApi().getApi().getENS(pID, pCallback);
    }


    /**
     * @param pPage     Startet bei 0. 30 ENS/Page
     * @param pFolder   Ornder ID. Abhängig von pType
     * @param pType     Ordner Typ
     * @param pCallback
     */
    public void getENSList(final int pPage, final long pFolder, final String pType, final Callback<ReturnObject<List<ENSObject>>> pCallback) {
        getWebApi().getApi().getENSList(pFolder, pType, pPage, pCallback);
    }


    /**
     * @param pCallback
     */
    public void getFolderList(final Callback<ReturnObject<ENSFolderObject.ENSFolderObjectContainer>> pCallback) {
        getWebApi().getApi().getENSFolder(pCallback);
    }

    // /ens/an_check/

    /**
     * @param pCallback (ENSApi.anCheckObject.class)
     */
    public void checkUserName(final ArrayList<String> pUserName, final Callback<ReturnObject<ENSCheckRecipientsObject>> pCallback) {
        getWebApi().getApi().checkENSRecipients(pUserName, pCallback);
    }

    public static class anCheckObject {

        @SerializedName("user_ids")
        public ArrayList<Long> IDs;

        @SerializedName("errors")
        public ArrayList<String> errors;

        @SerializedName("warnings")
        public ArrayList<String> warnings;

        public anCheckObject() {

        }

    }


    //DB Access

    private boolean saveToDB(ENSObject ens) {
        getHelper().getENSDataDao().createOrUpdate(ens);
        return true;
    }

    private boolean saveQueueToDB(ENSQueueObject ens) {
        getHelper().getENSQueueDataDao().createOrUpdate(ens);
        return true;
    }

    private boolean saveDraftToDB(ENSDraftObject ens) {
        getHelper().getENSDraftDataDao().createOrUpdate(ens);
        return true;
    }

    @SuppressWarnings("unused")
    private boolean removeFromDB(ENSObject ens) {
        getHelper().getENSDataDao().delete(ens);
        return true;
    }

    private boolean removeDraftFromDB(ENSDraftObject ens) {
        getHelper().getENSDraftDataDao().delete(ens);
        return true;
    }

    public boolean removeFromQueue(ENSQueueObject ens) {
        getHelper().getENSQueueDataDao().delete(ens);
        return true;
    }

    private ENSObject getFromDB(long id) {
        return getHelper().getENSDataDao().queryForId(id);
    }

    private ENSDraftObject getFromDraftDB(long id) {
        return getHelper().getENSDraftDataDao().queryForId(id);
    }

    public List<ENSQueueObject> getFromQueue() {
        return getHelper().getENSQueueDataDao().queryForAll();
    }

    public void clearNotification() {
        ENSNotificationManager manager = new ENSNotificationManager(getContext());
        manager.clearNotifications();
    }

    private DatabaseHelper databaseHelper = null;

    public void close() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(getContext(), DatabaseHelper.class);
        }
        return databaseHelper;
    }


}
