package de.meisterfuu.animexx;

import android.provider.CalendarContract;

public class Constants {

	// OAuth
	public static final String CONSUMER_KEY = KEYS.CONSUMER_KEY;
	public static final String CONSUMER_SECRET = KEYS.CONSUMER_SECRET;

	public static final String REQUEST_URL = "https://ws.animexx.de/oauth/request_token";
	public static final String ACCESS_URL = "https://ws.animexx.de/oauth/access_token";
	public static final String AUTHORIZE_URL = "https://ssl.animexx.de/oauth/authorize";

	//OAuth2
	public static final String CLIENT_ID = KEYS.OAUTH_CLIENT_ID;
	public static final String CLIENT_SECRET = KEYS.OAUTH_CLIENT_SECRET;
	public static final String GRANT_TYPE = "authorization_code";

	public static final String OAUTH_URL = "https://ssl.animexx.de/oauth2/authorize/?client_id="+Constants.CLIENT_ID+"&response_type=code";

	public static final String OAUTH_CALLBACK_SCHEME = "animexx";
	public static final String OAUTH_CALLBACK_HOST = "www.animexx.de/";
	public static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME	+ "://" + OAUTH_CALLBACK_HOST;

    public static final String FULL_PACKAGE_NAME = "de.meisterfuu.animexx";
    public static final String ACCOUNT_NAME = "Animexx";
    public static final String ACCOUNT_TYPE = FULL_PACKAGE_NAME+".account";
    public static final String CONTENT_CALENDAR_AUTHORITY = CalendarContract.AUTHORITY;
	
	// Other
	public static final String VERSION = "v2.0.0";

}
