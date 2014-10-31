package de.meisterfuu.animexx.api.xmpp;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import de.meisterfuu.animexx.api.DatabaseHelper;
import de.meisterfuu.animexx.api.broker.BasicWebBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.xmpp.XMPPHistoryObject;
import de.meisterfuu.animexx.objects.xmpp.XMPPMessageObject;
import de.meisterfuu.animexx.objects.xmpp.XMPPRoosterObject;
import de.meisterfuu.animexx.xmpp.XMPPService;
import retrofit.Callback;


public class XMPPApi extends BasicWebBroker {
	

	Context mContext;
	Gson gson;
	
	
	
	//Init
	
	public XMPPApi(Context pContext){
        super(pContext.getApplicationContext());
        this.mContext = pContext;
		GsonBuilder b = new GsonBuilder();
		gson = b.create();
	}
	
	
	//Public Methods
	
	/**
	 * 
	 */
	public static void sendMessage(final String pToJID, final String pMessage, Context pContext){		
		Intent intent = new Intent(XMPPService.SEND_MESSAGE);
		intent.setPackage(pContext.getPackageName());
		intent.putExtra(XMPPService.BUNDLE_MESSAGE_BODY, pMessage);
		intent.putExtra(XMPPService.BUNDLE_TO, pToJID);
	    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
		    intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
	    }
		pContext.sendBroadcast(intent);		
	}
	
	/**
     * @param pCallback
     */
	public void getOnlineHistory(final long pID, final Callback<ReturnObject<List<XMPPHistoryObject>>> pCallback){
        getWebApi().getApi().getXMPPLog(pID, 60, pCallback);
	}
	
			
	/**
	 * @param pCallback 
	 */
	public void getOfflineHistory(final String pJid, final Callback<List<XMPPMessageObject>> pCallback){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				List<XMPPMessageObject> obj = new ArrayList<XMPPMessageObject>();
				
				obj.addAll(getMessageFromDB(pJid));	
				
				final List<XMPPMessageObject> retu = obj;

				hand.post(new Runnable() {			
					public void run() {
						if(pCallback != null) pCallback.success(retu, null);
					}
				});
			}
		}).start();
	}

	/**
	 * @param pCallback 
	 */
	public void getRooster(final Callback<List<XMPPRoosterObject>> pCallback, final boolean pOnlineOnly){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				ArrayList<XMPPRoosterObject> list = new ArrayList<XMPPRoosterObject>();
				
			
				for(XMPPRoosterObject obj: getCompleteRoosterFromDB()){
					if((pOnlineOnly && obj.getStatus() == XMPPRoosterObject.STATUS_OFFLINE)){	
						continue;
					}
					obj.latestMessage = getLatestMessageFromDB(obj.getJid());
					list.add(obj);
				}
				
	
				
				final ArrayList<XMPPRoosterObject> retu = list;

                hand.post(new Runnable() {
                    public void run() {
                        if(pCallback != null) pCallback.success(retu, null);
                    }
                });
			}
		}).start();
	}


	public XMPPRoosterObject NTgetSingleRooster(final String pJid){				
				return getSingleRoosterFromDB(pJid);					
	}

	public String NTgetNewChatAuth() {
				return getWebApi().getApi().getXMPPAuth().getObj().getToken();
	}


	//DB Access
		
	private DatabaseHelper databaseHelper = null;
	
	List<XMPPRoosterObject> getCompleteRoosterFromDB(){		
		return getHelper().getXMPPRoosterDataDao().queryForAll();
	}

    public XMPPRoosterObject getSingleRoosterFromDB(String pID){
		return getHelper().getXMPPRoosterDataDao().queryForId(pID);
	}
	
	public void insertSingleRoosterToDB(XMPPRoosterObject pRoosterObj){		
		getHelper().getXMPPRoosterDataDao().createOrUpdate(pRoosterObj);
	}

    public void deleteSingleRoosterFromDB(String JID){
        getHelper().getXMPPRoosterDataDao().deleteById(JID);
    }
	
	public void insertMessageToDB(XMPPMessageObject pMessageObj){		
		getHelper().getXMPPMessageDataDao().createOrUpdate(pMessageObj);
	}
	
	public List<XMPPMessageObject> getMessageFromDB(String pJid){		
		if(pJid == null){
			return getHelper().getXMPPMessageDataDao().queryForAll();
		}
		XMPPMessageObject args = new XMPPMessageObject();
		args.setTopicJID(pJid);
		return getHelper().getXMPPMessageDataDao().queryForMatchingArgs(args);
	}
	
	public XMPPMessageObject getLatestMessageFromDB(String pJid){		
		XMPPMessageObject args = new XMPPMessageObject();
		args.setTopicJID(pJid);
		List<XMPPMessageObject> x = getHelper().getXMPPMessageDataDao().queryForMatchingArgs(args);
		if(x.size() > 0){
			return getHelper().getXMPPMessageDataDao().queryForMatchingArgs(args).get(x.size()-1);
		}
		return null;
	}

	public void close() {
	    if (databaseHelper != null) {
	        OpenHelperManager.releaseHelper();
	        databaseHelper = null;
	    }
	}

	private DatabaseHelper getHelper() {
	    if (databaseHelper == null) {
	        databaseHelper =
	            OpenHelperManager.getHelper(mContext, DatabaseHelper.class);
	    }
	    return databaseHelper;
	}
	


}
