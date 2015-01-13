package de.meisterfuu.animexx.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

import java.io.File;
import java.util.ArrayList;

import de.meisterfuu.animexx.api.EventBus;
import de.meisterfuu.animexx.api.UploadProgressEvent;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.api.web.WebAPI;
import de.meisterfuu.animexx.objects.FileUploadReturnObject;
import retrofit.mime.TypedFile;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class UploadService extends IntentService {

    private static final String ACTION_FOO = "de.meisterfuu.animexx.activitys.share.action.FOO";

    private static final String EXTRA_FILES = "de.meisterfuu.animexx.activitys.share.extra.PARAM1";
    private static final String EXTRA_CALLERID = "de.meisterfuu.animexx.activitys.share.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startAction(Context context, ArrayList<String> files, int callerID) {
        Intent intent = new Intent(context, UploadService.class);
        intent.setAction(ACTION_FOO);
        intent.putStringArrayListExtra(EXTRA_FILES, files);
        intent.putExtra(EXTRA_CALLERID, callerID);
        context.startService(intent);
    }


    public UploadService() {
        super("UploadService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                ArrayList<String> files = intent.getStringArrayListExtra(EXTRA_FILES);
                int callerID = intent.getIntExtra(EXTRA_CALLERID, -1);
                upload(files, callerID);
            }
        }
    }

    private void upload(ArrayList<String> files, final int callerID) {
        WebAPI api = new WebAPI(this);
        final Bus eventBus = EventBus.getBus().getOtto();
        Handler handler = new Handler(Looper.getMainLooper());

        TypedFile file;
        ArrayList<ReturnObject<FileUploadReturnObject>> answers = new ArrayList<>();
        float finished = 0;
        float oneP = files.size() / 100f;
        for(String path: files){
            file = new TypedFile("image/png", getFileFromPath(path));
            final ReturnObject<FileUploadReturnObject> pReturn = api.getApi().uploadFile("/Android/"+file.fileName(), file);
            answers.add(pReturn);
            finished = finished+oneP;
            final int fin = (int) finished;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    eventBus.post(new UploadProgressEvent(callerID, fin, pReturn));
                }
            });
        }
    }

    public static File getFileFromPath(String path) {
        final File f = new File(path);
        return f;
    }

}