package de.meisterfuu.animexx.api.broker;


import android.content.Context;

import java.util.List;

import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.FileUploadObject;
import de.meisterfuu.animexx.objects.UploadedFile;
import retrofit.Callback;

public class FileBroker extends BasicWebBroker {


    public FileBroker(Context pContext) {
        super(pContext);

    }

    /**
     * @param pFileURI
     */
    public void uploeadFile(final FileUploadObject pFileURI) {

    }


    public void getFileList(Callback<ReturnObject<List<UploadedFile>>> c){
        this.getWebApi().getApi().getFileList(c);
    }
}
