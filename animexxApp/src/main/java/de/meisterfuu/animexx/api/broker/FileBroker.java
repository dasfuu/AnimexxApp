package de.meisterfuu.animexx.api.broker;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.FileUploadObject;
import de.meisterfuu.animexx.objects.UploadedFile;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FileBroker extends BasicWebBroker {


    public FileBroker(Context pContext) {
        super(pContext);

    }

    /**
     * @param pFileURI
     */
    public void uploeadFile(final FileUploadObject pFileURI) {

    }


    public void getImageFileList(final Callback<ReturnObject<List<UploadedFile>>> c){
        this.getWebApi().getApi().getFileList(new Callback<ReturnObject<List<UploadedFile>>>() {
            @Override
            public void success(ReturnObject<List<UploadedFile>> listReturnObject, Response response) {
                List<UploadedFile> fList = new ArrayList<UploadedFile>();
                for(UploadedFile file: listReturnObject.getObj()){
                    if(file.getThumbnailUrl() != null){
                        fList.add(file);
                    }
                }
                listReturnObject.setObj(fList);
                c.success(listReturnObject, response);
            }

            @Override
            public void failure(RetrofitError error) {
                c.failure(error);
            }
        });
    }
}
