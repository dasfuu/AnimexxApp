package de.meisterfuu.animexx.objects;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


import android.content.Context;
import android.net.Uri;
import android.util.Base64;

public class FileUploadObject {

	String folder;
	String name;
	Uri uri;
	
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Uri getUri() {
		return uri;
	}
	public void setUri(Uri uri) {
		this.uri = uri;
	}
	
	public String getBase64(Context pContext){
		try {
			InputStream is = pContext.getContentResolver().openInputStream(this.getUri());
			byte[] bytes;
			byte[] buffer = new byte[8192];
			int bytesRead;
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			try {
			    while ((bytesRead = is.read(buffer)) != -1) {
			    output.write(buffer, 0, bytesRead);
			}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			bytes = output.toByteArray();
			String bs64 = Base64.encodeToString(bytes, Base64.DEFAULT);
			
			return bs64;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	
}
