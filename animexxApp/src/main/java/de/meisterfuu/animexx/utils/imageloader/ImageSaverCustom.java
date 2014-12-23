package de.meisterfuu.animexx.utils.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageSaverCustom {

    private String folder;
    Map<String, Bitmap> imageCache;
    boolean clickable = false;


    public ImageSaverCustom(String folder) {
        this.folder = folder;
        imageCache = new HashMap<String, Bitmap>();
    }


    public void download(final ImageSaveObject url, final Context context) {
        download(url, context, true);
    }


    // download function
    public void download(final ImageSaveObject url, final Context context, boolean clickable) {

        this.clickable = clickable;
        // Caching code right here
        String filename = url.getName();
        final File f = new File(getCacheDirectory(context), filename);

        // Is the bitmap in our memory cache?
        Bitmap bitmap = null;

        bitmap = imageCache.get(f.getPath());

        if (bitmap == null) {

            bitmap = BitmapFactory.decodeFile(f.getPath());

            if (bitmap != null) {
                imageCache.put(f.getPath(), bitmap);
            }

        }
        // No? download it
        if (bitmap == null) {
            BitmapDownloaderTask task = new BitmapDownloaderTask(context);
            task.execute(url);
        }

    }

    // our caching functions
    // Find the dir to save cached images
    private File getCacheDirectory(Context context) {
        File sdDir = context.getExternalCacheDir();
        File cacheDir;
        if (sdDir != null) {
            cacheDir = new File(sdDir, folder);
        } else
            cacheDir = context.getCacheDir();

        if (!cacheDir.exists()) cacheDir.mkdirs();
        return cacheDir;
    }


    private void writeFile(final Bitmap bmp, final File f) {
        new Thread(new Runnable() {

            public void run() {
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(f);
                    bmp.compress(Bitmap.CompressFormat.PNG, 80, out);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) out.close();
                    } catch (Exception ex) {
                    }
                }
            }
        }).start();
    }

    // /////////////////////

    // download asynctask
    public class BitmapDownloaderTask extends AsyncTask<ImageSaveObject, Void, Bitmap> {

        private ImageSaveObject url;
        private Context c;


        public BitmapDownloaderTask(Context context) {
            c = context;
        }


        @Override
        // Actual download method, run in the task thread
        protected Bitmap doInBackground(ImageSaveObject... params) {
            // params comes from the execute() call: params[0] is the url.
            url = params[0];
            return downloadBitmap(params[0].getUrl());
        }


        @Override
        // Once the image is downloaded, save it
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            // cache the image
            String filename = url.getName();
            final File f = new File(getCacheDirectory(c), filename);

            imageCache.put(f.getPath(), bitmap);
            writeFile(bitmap, f);


        }

    }


    // the actual download code
    static Bitmap downloadBitmap(String url) {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpClient client = new DefaultHttpClient(params);
        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or IllegalStateException
            getRequest.abort();
            Log.w("ImageDownloader", "Error while retrieving bitmap from " + url + e.toString());
        } finally {
            if (client != null) {
                // client.close();
            }
        }
        return null;
    }

}
