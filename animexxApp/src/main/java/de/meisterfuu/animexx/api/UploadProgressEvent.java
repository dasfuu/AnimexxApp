package de.meisterfuu.animexx.api;

import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.FileUploadReturnObject;

/**
 * Created by Furuha on 06.12.2014.
 */
public class UploadProgressEvent extends ApiEvent<ReturnObject<FileUploadReturnObject>> {

    private int progress;

    public UploadProgressEvent(int callerID, int progress, ReturnObject<FileUploadReturnObject> data) {
        this.setCallerID(callerID);
        this.setObj(data);
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

}
