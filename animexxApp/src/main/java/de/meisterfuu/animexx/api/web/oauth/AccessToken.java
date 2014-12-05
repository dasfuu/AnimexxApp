package de.meisterfuu.animexx.api.web.oauth;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.annotations.SerializedName;


public class AccessToken extends BaseResponse {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("expires_in")
    private Long expiresIn;

    @SerializedName("refresh_token")
    private String refreshToken;

    private long lastRefresh;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setAccessToken(final String pAccessToken) {
        accessToken = pAccessToken;
    }

    public void setTokenType(final String pTokenType) {
        tokenType = pTokenType;
    }

    public void setExpiresIn(final Long pExpiresIn) {
        expiresIn = pExpiresIn;
    }

    public void setRefreshToken(final String pRefreshToken) {
        refreshToken = pRefreshToken;
    }

    @Override
    public String toString() {

        if (super.getError() != null) {
            return "AccessToken{error='" + super.getError() + "'}";
        }

        return "AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expiresIn=" + expiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }

    public static AccessToken getToken(Context pContext) {
        AccessToken token = new AccessToken();

        SharedPreferences config = PreferenceManager.getDefaultSharedPreferences(pContext.getApplicationContext());
        token.accessToken = config.getString("OAUTH_ACCESSTOKEN", "");
        token.tokenType = config.getString("OAUTH_TYPE", "");
        token.expiresIn = config.getLong("OAUTH_EXPIRE", 0);
        token.lastRefresh = config.getLong("OAUTH_LASTREFRESH", 0);
        token.refreshToken = config.getString("OAUTH_REFRESHTOKEN", "");

        return token;
    }

    public static void saveToken(AccessToken pToken, Context pContext) {
        SharedPreferences.Editor config = PreferenceManager.getDefaultSharedPreferences(pContext.getApplicationContext()).edit();
        config.putString("OAUTH_ACCESSTOKEN", pToken.accessToken);
        config.putString("OAUTH_TYPE", pToken.tokenType);
        config.putLong("OAUTH_EXPIRE", pToken.expiresIn);
        config.putLong("OAUTH_LASTREFRESH", pToken.lastRefresh);
        config.putString("OAUTH_REFRESHTOKEN", pToken.refreshToken);
        config.commit();
    }

    public static void clearToken(Context pContext) {
        SharedPreferences.Editor config = PreferenceManager.getDefaultSharedPreferences(pContext.getApplicationContext()).edit();
        config.remove("OAUTH_ACCESSTOKEN");
        config.remove("OAUTH_TYPE");
        config.remove("OAUTH_EXPIRE");
        config.remove("OAUTH_LASTREFRESH");
        config.remove("OAUTH_REFRESHTOKEN");
        config.commit();
    }
}