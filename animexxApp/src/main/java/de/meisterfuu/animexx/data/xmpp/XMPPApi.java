package de.meisterfuu.animexx.data.xmpp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.DatabaseHelper;
import de.meisterfuu.animexx.objects.XMPPHistoryObject;
import de.meisterfuu.animexx.objects.XMPPMessageObject;
import de.meisterfuu.animexx.objects.XMPPRoosterObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;
import de.meisterfuu.animexx.xmpp.XMPPService;


public class XMPPApi {
	

	Context mContext;
	Gson gson;
	
	
	
	//Init
	
	public XMPPApi(Context pContext){
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
	public void getOnlineHistory(final long pID, final APICallback pCallback){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				
				ArrayList<XMPPHistoryObject> list = new ArrayList<XMPPHistoryObject>();
				
				try {
					list = getHistoryfromWeb(pID, 60);
				} catch (APIException e) {
					error = e;
				}			
				
				final ArrayList<XMPPHistoryObject> retu = list;
				final APIException ferror = error;
				
				hand.post(new Runnable() {			
					public void run() {
						if(pCallback != null) pCallback.onCallback(ferror, retu);
					}
				});
			}
		}).start();
	}
	
			
	/**
	 * @param pCallback 
	 */
	public void getOfflineHistory(final String pJid, final APICallback pCallback){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				
				List<XMPPMessageObject> obj = new ArrayList<XMPPMessageObject>();
				
				obj.addAll(getMessageFromDB(pJid));	
				
				final List<XMPPMessageObject> retu = obj;
				final APIException ferror = error;
				
				hand.post(new Runnable() {			
					public void run() {
						if(pCallback != null) pCallback.onCallback(ferror, retu);
					}
				});
			}
		}).start();
	}
	/**
	 * @param pCallback 
	 */
	public void getRooster(final APICallback pCallback, final boolean pOnlineOnly){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				
				ArrayList<XMPPRoosterObject> list = new ArrayList<XMPPRoosterObject>();
				
			
				for(XMPPRoosterObject obj: getCompleteRoosterFromDB()){
					if((pOnlineOnly && obj.getStatus() == XMPPRoosterObject.STATUS_OFFLINE)){	
						continue;
					}
					obj.latestMessage = getLatestMessageFromDB(obj.getJid());
					list.add(obj);
				}
				
	
				
				final ArrayList<XMPPRoosterObject> retu = list;
				final APIException ferror = error;
				
				hand.post(new Runnable() {			
					public void run() {
						if(pCallback != null) pCallback.onCallback(ferror, retu);
					}
				});
			}
		}).start();
	}
	

	/**
	 * @param pCallback 
	 */
	public void getSingleRooster(final String pJid, final APICallback pCallback){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				
				XMPPRoosterObject obj = new XMPPRoosterObject();
				
				obj = getSingleRoosterFromDB(pJid);	
				
				final XMPPRoosterObject retu = obj;
				final APIException ferror = error;
				
				hand.post(new Runnable() {			
					public void run() {
						if(pCallback != null) pCallback.onCallback(ferror, retu);
					}
				});
			}
		}).start();
	}
	
	/**
	 * @param pCallback 
	 */
	public void getLatestMessage(final String pJid, final APICallback pCallback){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				
				XMPPMessageObject obj = new XMPPMessageObject();
				
				obj = getLatestMessageFromDB(pJid);	
				
				final XMPPMessageObject retu = obj;
				final APIException ferror = error;
				
				hand.post(new Runnable() {			
					public void run() {
						if(pCallback != null) pCallback.onCallback(ferror, retu);
					}
				});
			}
		}).start();
	}
	
	/**
	 */
	public XMPPRoosterObject NTgetSingleRooster(final String pJid){				
				return getSingleRoosterFromDB(pJid);					
	}
	
	/**
	 * @throws APIException 
	 */
	public String NTgetNewChatAuth() throws APIException{				
				return getChatAuthfromWeb();					
	}
	
	//Web-Api Access
	
	
	private ArrayList<XMPPHistoryObject> getHistoryfromWeb(long pUserID, long pLimit)  throws APIException{
		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/xmpp/log_user_animexx/?api=2&user_id=" + pUserID + "&limit=" + pLimit, mContext);
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				Type collectionType = new TypeToken<ArrayList<XMPPHistoryObject>>(){}.getType();
				ArrayList<XMPPHistoryObject> list = gson.fromJson(resultObj.getString("return"), collectionType);		
				return list;
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	
	}
	
	private String getChatAuthfromWeb()  throws APIException{
		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/xmpp/get_chat_auth/?api=2", mContext);
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				String list = resultObj.getJSONObject("return")	.getString("chat_auth");
				return list;
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	
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
