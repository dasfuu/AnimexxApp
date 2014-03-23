package de.meisterfuu.animexx.data.ens;

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
import android.os.Handler;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.DatabaseHelper;
import de.meisterfuu.animexx.objects.ENSFolderObject;
import de.meisterfuu.animexx.objects.ENSObject;
import de.meisterfuu.animexx.objects.ENSDraftObject;
import de.meisterfuu.animexx.objects.ENSQueueObject;
import de.meisterfuu.animexx.services.ENSQueueService;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.PostBodyFactory;
import de.meisterfuu.animexx.utils.Request;


public class ENSApi {
	
	public static final String TYPE_INBOX = "an";
	public static final String TYPE_OUTBOX = "von";
	
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
				long id = -1;
				APIException error = null;		
				try {
					id = sendENStoWeb(pENS);
				} catch (APIException e) {
					ENSQueueObject qObj = new ENSQueueObject();
					qObj.setDraft(pENS);
					qObj.setSubject(pENS.getSubject());
					saveQueueToDB(qObj);
					ENSQueueService.startAction(mContext);
					error = e;
				}
				
				//if(id != -1) saveDB(temp);
	
				
				final Long retu = id;
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
	 * @throws APIException 
	 */
	public long NTsendENS(final ENSDraftObject pENS) throws APIException{		
		long id = -1;
		try {
			id = sendENStoWeb(pENS);
		} catch (APIException e) {
//			throw e;
		}
		return id;
	}
	
	public static void sendENSDEBUG(final String string, final String title, Context c){		
				ENSApi api = new ENSApi(c);
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
	
	// /ens/an_check/
	/**
	 * @param pCallback (ENSApi.anCheckObject.class)
	 */
	public void checkUserName(final ArrayList<String> pUserName, final APICallback pCallback){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				
				anCheckObject list = null;
				
				try {
					list = checkUserNameWeb(pUserName);
				} catch (APIException e) {
					error = e;
				}			
				
				final anCheckObject retu = list;
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
	
	private anCheckObject checkUserNameWeb(ArrayList<String> pUserName) throws APIException {
		String names = "";
		for(String id: pUserName){
			names += "&users%5B%5D="+id;
		}
		
		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/ens/an_check/?api=2"+names);
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				anCheckObject arr = gson.fromJson(resultObj.getString("return"), ENSApi.anCheckObject.class);
				return arr;
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	
	}
	
	public static class anCheckObject{
		
		@SerializedName("user_ids")
		public ArrayList<Long> IDs;
		
		@SerializedName("errors")
		public ArrayList<String> errors;
		
		@SerializedName("warnings")
		public ArrayList<String> warnings;
		
		public anCheckObject(){
			
		}
		
	}


	private ENSObject getENSfromWeb(long pID) throws APIException{

		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/ens/ens_open/?ens_id=" + pID + "&text_format=both&api=2&get_user_avatar=true");
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
	
	
	private long sendENStoWeb(ENSDraftObject pENS)  throws APIException {
		try {
			String url = "https://ws.animexx.de/json/ens/ens_senden/?api=2";

			PostBodyFactory factory = new PostBodyFactory();
			
			
			factory.putValue("betreff", pENS.getSubject());
			factory.putValue("text", pENS.getMessage());
			if(pENS.getSignature() == null){
				factory.putValue("sig", "Android App");
			} else {
				factory.putValue("sig", pENS.getSignature());
			}

						
			for (int i = 0; i < pENS.getRecipients().size(); i++){
				factory.putValue("an_users[]", pENS.getRecipients().get(i)+"");
			}

			if (pENS.getReferenceType() != null) {
				factory
				.putValue("referenz_typ", pENS.getReferenceType())
				.putValue("referenz_id", pENS.getReferenceID()+"");
			}
			
			String result = Request.SignSendScribePost(url, factory);
			
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				return 1;
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
	
	private boolean saveQueueToDB(ENSQueueObject ens){
		getHelper().getENSQueueDataDao().createOrUpdate(ens);
		return true;
	}
	
	private boolean saveDraftToDB(ENSDraftObject ens){
		getHelper().getENSDraftDataDao().createOrUpdate(ens);
		return true;
	}
	
	@SuppressWarnings("unused")
	private boolean removeFromDB(ENSObject ens){
		getHelper().getENSDataDao().delete(ens);
		return true;
	}
	
	private boolean removeDraftFromDB(ENSDraftObject ens){
		getHelper().getENSDraftDataDao().delete(ens);
		return true;
	}
	
	public boolean removeFromQueue(ENSQueueObject ens){
		getHelper().getENSQueueDataDao().delete(ens);
		return true;
	}
	
	private ENSObject getFromDB(long id){
		return getHelper().getENSDataDao().queryForId(id);
	}
	
	private ENSDraftObject getFromDraftDB(long id){
		return getHelper().getENSDraftDataDao().queryForId(id);
	}
	
	public List<ENSQueueObject> getFromQueue(){
		return getHelper().getENSQueueDataDao().queryForAll();
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
	            OpenHelperManager.getHelper(mContext, DatabaseHelper.class);
	    }
	    return databaseHelper;
	}
	


}
