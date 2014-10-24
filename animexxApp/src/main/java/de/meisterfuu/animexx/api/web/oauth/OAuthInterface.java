package de.meisterfuu.animexx.api.web.oauth;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Meisterfuu on 28.09.2014.
 */
public interface OAuthInterface {

	public static final String TOKEN_ENDPOINT = "https://ssl.animexx.de";


	@POST("/oauth2/token/")
	@FormUrlEncoded
	AccessToken getAccessToken(@Field("client_id") String clientId,
	                           @Field("client_secret") String clientSecret,
	                           @Field("code") String code,
	                           @Field("grant_type") String grantType);

	@POST("/oauth2/token/")
	@FormUrlEncoded
	void getAccessToken(@Field("client_id") String clientId,
                        @Field("client_secret") String clientSecret,
                        @Field("code") String code,
                        @Field("grant_type") String grantType,
                        Callback<AccessToken> pCallback);

	@POST("/oauth2/token/")
	@FormUrlEncoded
	RefreshToken refreshAccessToken(@Field("client_id") String clientId,
	                                @Field("client_secret") String clientSecret,
	                                @Field("refresh_token") String refreshToken,
	                                @Field("grant_type") String grantType);

	@POST("/oauth2/token/")
	@FormUrlEncoded
	void refreshAccessToken(@Field("client_id") String clientId,
                            @Field("client_secret") String clientSecret,
                            @Field("refresh_token") String refreshToken,
                            @Field("grant_type") String grantType,
                            Callback<RefreshToken> pCallback);

}
