package de.meisterfuu.animexx.data.ens;

import java.lang.reflect.Type;
import java.util.ArrayList;

import oauth.signpost.OAuth;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import android.content.Context;
import android.os.Handler;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.objects.ENSFolderObject;
import de.meisterfuu.animexx.objects.ENSObject;
import de.meisterfuu.animexx.objects.ENSDraftObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;


public class ENSApi {
	
	public static final String TYPE_INBOX = "an";
	public static final String TYPE_OUTBOX = "von";
	
	ENSDatabase mDB;
	Context mContext;
	Gson gson;
	
	
	
	//Init
	
	public ENSApi(Context pContext){
		this.mContext = pContext;
		GsonBuilder b = new GsonBuilder();
		gson = b.create();
	}
	
	
	//Public Methods
	
	/**
	 * @param pENS
	 * @param pCallback
	 */
	public void sendENS(final ENSDraftObject pENS, final APICallback pCallback){		
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				int id = -1;
				APIException error = null;				

				try {
					id = sendENStoWeb(pENS);
				} catch (APIException e) {
					error = e;
				}
				
				//if(id != -1) saveDB(temp);
	
				
				final Integer retu = id;
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
	 * @param pENS
	 * @param pCallback
	 */
	public void saveENSDraft(final ENSDraftObject pENS, final APICallback pCallback){		
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
	
				final Boolean retu = ENSApi.this.saveDraftToDB(pENS);	
				final APIException ferror = null;
				
				hand.post(new Runnable() {			
					public void run() {
						if(pCallback != null) pCallback.onCallback(ferror, retu);	
						
					}
				});
			}
		}).start();
	}
	
	/**
	 * @param pID
	 * @param pCallback
	 */
	public void getENSDraft(final long pID, final APICallback pCallback){		
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
	
				final ENSDraftObject retu = ENSApi.this.getFromDraftDB(pID);	
				final APIException ferror = null;
				
				hand.post(new Runnable() {			
					public void run() {
						if(pCallback != null) pCallback.onCallback(ferror, retu);
						
					}
				});
			}
		}).start();
	}
	
	/**
	 * @param pENS
	 * @param pCallback
	 */
	public void deleteENSDraft(final ENSDraftObject pENS, final APICallback pCallback){		
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
	
				final boolean retu = ENSApi.this.removeDraftFromDB(pENS);	
				final APIException ferror = null;
				
				hand.post(new Runnable() {			
					public void run() {
						if(pCallback != null) pCallback.onCallback(ferror, retu);
						
					}
				});
			}
		}).start();
	}
	



	/**
	 * @param pID ID der ENS
	 * @param pCallback
	 */
	public void getENS(final long pID, final APICallback pCallback){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				ENSObject temp = getFromDB(pID);
				APIException error = null;
				
				if(temp == null){
					try {
						temp = getENSfromWeb(pID);
					} catch (APIException e) {
						error = e;
					}
					saveToDB(temp);
				}
				
				final ENSObject retu = temp;
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
	 * @param pPage Startet bei 0. 30 ENS/Page
	 * @param pFolder Ornder ID. Abhängig von pType
	 * @param pType Ordner Typ
	 * @param pCallback 
	 */
	public void getENSList(final long pPage, final long pFolder, final String pType, final APICallback pCallback){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				
				ArrayList<ENSObject> list = new ArrayList<ENSObject>();
				
				try {
					list = getENSListfromWeb(pPage, pFolder, pType);
				} catch (APIException e) {
					error = e;
				}			
				
				final ArrayList<ENSObject> retu = list;
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
	public void getFolderList(final APICallback pCallback){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				
				ArrayList<ENSFolderObject> list = new ArrayList<ENSFolderObject>();
				
				try {
					list = getFolderfromWeb();
				} catch (APIException e) {
					error = e;
				}			
				
				final ArrayList<ENSFolderObject> retu = list;
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
	
	private ENSObject getENSfromWeb(long pID) throws APIException{

		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/ens/ens_open/?ens_id=" + pID + "&text_format=both&api=2");
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				ENSObject ENS = gson.fromJson(resultObj.getString("return"), ENSObject.class);
				return ENS;
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	

	}
	
	private ArrayList<ENSObject> getENSListfromWeb(long pPage, long pFolder, String pType)  throws APIException{
		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/ens/ordner_ens_liste/?ordner_id=" + pFolder + "&ordner_typ=" + pType + "&seite=" + pPage + "&api=2&get_user_avatar=true");
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				Type collectionType = new TypeToken<ArrayList<ENSObject>>(){}.getType();
				ArrayList<ENSObject> list = gson.fromJson(resultObj.getString("return"), collectionType);		
				//ENSObject[] x = gson.fromJson(resultObj.getString("return"), ENSObject[].class);

				return list;
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	
	}
	
	private ArrayList<ENSFolderObject> getFolderfromWeb()  throws APIException{
		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/ens/ordner_liste/?api=2");
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				Type collectionType = new TypeToken<ArrayList<ENSFolderObject>>(){}.getType();
				ArrayList<ENSFolderObject> listAn = gson.fromJson(resultObj.getJSONObject("return").getString("an"), collectionType);
				ArrayList<ENSFolderObject> listVon = gson.fromJson(resultObj.getJSONObject("return").getString("von"), collectionType);
				
				for(ENSFolderObject p: listAn){
					p.setType(ENSApi.TYPE_INBOX);
				}
				for(ENSFolderObject p: listVon){
					p.setType(ENSApi.TYPE_OUTBOX);
				}
				listAn.addAll(listVon);
				//ENSObject[] x = gson.fromJson(resultObj.getString("return"), ENSObject[].class);

				return listAn;
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	
	}
	
	
	private int sendENStoWeb(ENSDraftObject pENS)  throws APIException {
		try {
			String url = "https://ws.animexx.de/json/ens/ens_senden/?api=2";
			HttpPost request = new HttpPost(url);

			String body = new String();
			
			body += "betreff=" + OAuth.percentEncode(pENS.getSubject());
			body += "&text=" + OAuth.percentEncode(pENS.getMessage());
			body += "&sig=" + OAuth.percentEncode(pENS.getSignature());
			
			for (int i = 0; i < pENS.getRecipients().size(); i++){
				body += "&an_users[]=" + pENS.getRecipients().get(i);
			}

			if (pENS.getReferenceType() != null) {
				body += "&referenz_typ=" + pENS.getReferenceType();
				body += "&referenz_id=" + pENS.getReferenceID();
			}
			
			StringEntity se = new StringEntity(body);
			se.setContentType("application/x-www-form-urlencoded");
			request.setEntity(se);

			String result = Request.SignSend(request);
			
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				return resultObj.getJSONObject("return").getInt("id");
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	
	}
	
	

	//DB Access
	
	private boolean saveToDB(ENSObject ens){
		getHelper().getENSDataDao().createOrUpdate(ens);
		return true;
	}
	
	private boolean saveDraftToDB(ENSDraftObject ens){
		getHelper().getSendENSDataDao().createOrUpdate(ens);
		return true;
	}
	
	@SuppressWarnings("unused")
	private boolean removeFromDB(ENSObject ens){
		getHelper().getENSDataDao().delete(ens);
		return true;
	}
	
	private boolean removeDraftFromDB(ENSDraftObject ens){
		getHelper().getSendENSDataDao().delete(ens);
		return true;
	}
	
	private ENSObject getFromDB(long id){
		return getHelper().getENSDataDao().queryForId(id);
	}
	
	private ENSDraftObject getFromDraftDB(long id){
		return getHelper().getSendENSDataDao().queryForId(id);
	}
	
	private ENSDatabase databaseHelper = null;

	public void close() {
	    if (databaseHelper != null) {
	        OpenHelperManager.releaseHelper();
	        databaseHelper = null;
	    }
	}

	private ENSDatabase getHelper() {
	    if (databaseHelper == null) {
	        databaseHelper =
	            OpenHelperManager.getHelper(mContext, ENSDatabase.class);
	    }
	    return databaseHelper;
	}
	


}
