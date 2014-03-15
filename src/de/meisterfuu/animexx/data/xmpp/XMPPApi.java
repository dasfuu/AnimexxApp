package de.meisterfuu.animexx.data.xmpp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuth;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.DatabaseHelper;
import de.meisterfuu.animexx.objects.ENSFolderObject;
import de.meisterfuu.animexx.objects.ENSObject;
import de.meisterfuu.animexx.objects.ENSDraftObject;
import de.meisterfuu.animexx.objects.ENSQueueObject;
import de.meisterfuu.animexx.objects.XMPPHistoryObject;
import de.meisterfuu.animexx.objects.XMPPRoosterObject;
import de.meisterfuu.animexx.services.ENSQueueService;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;
import de.meisterfuu.animexx.xmpp.XMPPChatActivity;
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
					list = getHistoryfromWeb(pID, 30);
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
	public void getRooster(final APICallback pCallback){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				
				ArrayList<XMPPRoosterObject> list = new ArrayList<XMPPRoosterObject>();
				
				list.addAll(getCompleteRoosterFromDB());		
				
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
	public void getSingleRooster(final APICallback pCallback){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				
				XMPPRoosterObject obj = new XMPPRoosterObject();
				
				obj = null;	
				
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
	
	
	//Web-Api Access
	
	
	private ArrayList<XMPPHistoryObject> getHistoryfromWeb(long pUserID, long pLimit)  throws APIException{
		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/xmpp/log_user_animexx/?user_id=" + pUserID + "&limit=" + pLimit);
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
	

	//DB Access
		
	private DatabaseHelper databaseHelper = null;
	
	List<XMPPRoosterObject> getCompleteRoosterFromDB(){		
		return getHelper().getXMPPRoosterDataDao().queryForAll();
	}
	
	XMPPRoosterObject getSingleRoosterFromDB(long pID){		
		return getHelper().getXMPPRoosterDataDao().queryForId(pID);
	}
	
	public void insertSingleRoosterToDB(XMPPRoosterObject pRoosterObj){		
		getHelper().getXMPPRoosterDataDao().createOrUpdate(pRoosterObj);
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