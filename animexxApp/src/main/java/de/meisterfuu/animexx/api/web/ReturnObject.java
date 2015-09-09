package de.meisterfuu.animexx.api.web;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Meisterfuu on 28.09.2014.
 */
public class ReturnObject<T> {

    @SerializedName("return")
    private T obj;

    @SerializedName("success")
    private boolean success;

    @SerializedName("error_source")
    private int errorSource;

    @SerializedName("error_code")
    private int errorCode;

    @SerializedName("error_msg")
    private String errorMsg;

    public T getObj() {
        return obj;
    }

    public void setObj(final T pObj) {
        obj = pObj;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorSource() {
        return errorSource;
    }

    public void setErrorSource(int errorSource) {
        this.errorSource = errorSource;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
