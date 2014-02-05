package de.meisterfuu.animexx.utils;


public class APIException extends Exception {
	
	public static final int REQUEST_FAILED = 1;
	public static final int OTHER = 1000;
	

	private static final long serialVersionUID = 1L;
	private String message = null;
	private int errorID = 0;
	
	public APIException(String message, int errorID){
		super(message);
		this.setMessage(message);
		this.setErrorID(errorID);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getErrorID() {
		return errorID;
	}

	public void setErrorID(int errorID) {
		this.errorID = errorID;
	}
	
	
}
