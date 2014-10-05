package de.meisterfuu.animexx.api.dav;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.meisterfuu.animexx.api.APICallback;
import de.meisterfuu.animexx.objects.FileUploadObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.PostBodyFactory;
import de.meisterfuu.animexx.utils.Request;

public class FileApi {

	
	private Context mContext;
	private Gson gson;

	public FileApi(Context pContext){
		this.mContext = pContext;
		GsonBuilder b = new GsonBuilder();
		gson = b.create();
	}
	
	/**
	 * @param pFileURI
	 * @param pCallback
	 */
	public void sendFile(final FileUploadObject pFileURI, final APICallback pCallback){		
		final Handler hand = new Handler();	
		
		new Thread(new Runnable() {
			public void run() {
				String id = null;
				APIException error = null;		
				try {
					id = storeFiletoWeb(pFileURI);
				} catch (APIException e) {
					error = e;
				}
				
				//if(id != -1) saveDB(temp);
	
				
				final String retu = id;
				final APIException ferror = error;
				
				hand.post(new Runnable() {			
					public void run() {
						if(pCallback != null) pCallback.onCallback(ferror, retu);
					}
				});
			}
		}).start();
	}

	
	
	
	//WEB
	
	
	private String storeFiletoWeb(FileUploadObject pFile) throws APIException {
		try {
			String url = "https://ws.animexx.de/json/mitglieder/files/upload/?api=2";

			PostBodyFactory factory = new PostBodyFactory();
			
			
			factory.putValue("filename", pFile.getFolder()+pFile.getName());
			factory.putValue("data", pFile.getBase64(mContext));
			
			String result = Request.SignSendScribePost(url, factory, mContext);
			
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				return "";
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	
	}
	
}
