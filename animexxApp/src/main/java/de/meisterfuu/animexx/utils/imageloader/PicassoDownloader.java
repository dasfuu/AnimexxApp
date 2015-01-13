/*
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.meisterfuu.animexx.utils.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;


/**
 * A {@link Downloader} which uses OkHttp to download images.
 */
public class PicassoDownloader implements Downloader {

    public static String createProfilePictureKey(long userID, long pictureID){
        return "profile_"+userID+"_"+pictureID;
    }
    public static String createHomePictureKey(String id){
        return "home_contact_"+id;
    }
    public static String createEventLogoKey(long id){
        return "event_logo_"+id;
    }
    public static String createAvatarKey(long userID){
        return "avatar_"+userID;
    }
    public static String createRPGAvatarKey(long rpgID, long avatarID, long charaID){
        return "rpgavatar_"+rpgID+"_"+charaID+"_"+avatarID;
    }
    public static String createGBAvatarKey(long gbAvatarID){
        return "gbavatar_"+gbAvatarID;
    }
    public static String createFileThumbnailKey(long fileID){
        return "filethumb_"+fileID;
    }

    private static Picasso sPicasso;
    public static Picasso getPicasso(Context pContext){
        if(sPicasso == null){
            sPicasso = new Picasso.Builder(pContext).downloader(new PicassoDownloader(pContext, "picture_cache")).build();
        }
        return sPicasso;
    }

    private static Picasso sPicassoAvatar;
    public static Picasso getAvatarPicasso(Context pContext){
        if(sPicassoAvatar == null){
            sPicassoAvatar = new Picasso.Builder(pContext).downloader(new PicassoDownloader(pContext, "forenavatar")).build();
        }
        return sPicassoAvatar;
    }

    public static Uri getAvatarURI(String key, Context pContext){
        File externalFile = new File(createCacheDir(pContext, "forenavatar"), key+".webp");
        return Uri.fromFile(externalFile);
    }

//    private static Picasso sPicassoProfile;
//    public static Picasso getProfilePicturePicasso(Context pContext){
//        if(sPicassoProfile == null){
//            sPicassoProfile = new Picasso.Builder(pContext).downloader(new PicassoDownloader(pContext, "profilepicture")).build();
//        }
//        return sPicassoProfile;
//    }

    private final OkUrlFactory urlFactory;
    private final File cacheDir;

    /**
     * Create new downloader that uses OkHttp. This will install an image cache into your application
     * cache directory.
     */
    public PicassoDownloader(final Context context, String foldername) {
        cacheDir = createCacheDir(context, foldername);
        this.urlFactory = new OkUrlFactory(new OkHttpClient());
    }

    /**
     * Create new downloader that uses OkHttp. This will install an image cache into your application
     * cache directory.
     */
    public PicassoDownloader(final Context context, String foldername, OkHttpClient client) {
        cacheDir = createCacheDir(context, foldername);
        this.urlFactory = new OkUrlFactory(client);
    }

    static File createCacheDir(Context context, String folderName) {
        File sdDir = context.getExternalCacheDir();
        File cacheDir;
        if (sdDir != null) {
            cacheDir = new File(sdDir, folderName);
        } else {
            cacheDir = new File(context.getCacheDir(), folderName);
        }

        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        return cacheDir;
    }

    protected OkHttpClient getClient() {
        return urlFactory.client();
    }

    @Override
    public Response load(Uri uri, boolean localCacheOnly, String stableKey)
            throws IOException {

        //Get from Disc
        String filename;
        if(stableKey != null){
            filename = stableKey;
        } else {
            filename = uri.hashCode()+"";
        }

        filename += ".webp";
        final File f = new File(cacheDir, filename);

        if(f.exists()){
            return new Response(new FileInputStream(f), true, -1);
        }

        String host = uri.getHost();
        if (localCacheOnly || host.equals("noDownload")) {
            return null;
        }

        com.squareup.okhttp.Request.Builder requestBuilder = new com.squareup.okhttp.Request.Builder().url(uri.toString());

        com.squareup.okhttp.Response response = getClient().newCall(requestBuilder.build()).execute();
        int responseCode = response.code();
        if (responseCode >= 300) {
            response.body().close();
            throw new ResponseException(responseCode + " " + response.message(), localCacheOnly,
                    responseCode);
        }

        //Save to cache dir
        FileOutputStream outputStream = null;
        InputStream inputStream = response.body().byteStream();

        try {

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            outputStream = new FileOutputStream(f);
            Log.e("BITMAP", "SIZE: "+ (bitmap != null ? bitmap.getByteCount(): "null"));
            if(bitmap.compress(Bitmap.CompressFormat.WEBP, 90, outputStream)){
                //return
                return new Response(bitmap, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            try {
                if (outputStream != null) outputStream.close();
            } catch (Exception ex) {
            }
        }

        return null;

    }


    @Override
    public void shutdown() {
        com.squareup.okhttp.Cache cache = urlFactory.client().getCache();
        if (cache != null) {
            try {
                cache.close();
            } catch (IOException ignored) {
            }
        }
    }




    /** Returns {@code true} if header indicates the response body was loaded from the disk cache. */
    static boolean parseResponseSourceHeader(String header) {
        if (header == null) {
            return false;
        }
        String[] parts = header.split(" ", 2);
        if ("CACHE".equals(parts[0])) {
            return true;
        }
        if (parts.length == 1) {
            return false;
        }
        try {
            return "CONDITIONAL_CACHE".equals(parts[0]) && Integer.parseInt(parts[1]) == 304;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
