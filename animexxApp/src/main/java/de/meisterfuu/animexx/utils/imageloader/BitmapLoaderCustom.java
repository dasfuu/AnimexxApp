package de.meisterfuu.animexx.utils.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class BitmapLoaderCustom {

    public static Bitmap getUserBitmap(final String id, Context c) {
        ImageSaveObject url = new ImageSaveObject("", id);

        Bitmap res = existsUser(url, c);
        if (res != null) return res;

        res = existsAvatar(url, c);
        return res;

    }

    public static Bitmap existsUser(final ImageSaveObject url, Context c) {
        // Caching code right here
        String filename = url.getName();
        final File f = new File(getCacheDirectory(c, "profilbild"), filename);
        Bitmap bitmap = null;

        if (bitmap == null) {
            bitmap = BitmapFactory.decodeFile(f.getPath());
        }

        if (bitmap == null) {
            // No
            return null;
        } else {
            // Yes
            return bitmap;
        }
    }

    public static Bitmap existsAvatar(final ImageSaveObject url, Context c) {
        // Caching code right here
        String filename = url.getName();
        final File f = new File(getCacheDirectory(c, "forenavatar"), filename);
        Bitmap bitmap = null;

        if (bitmap == null) {
            bitmap = BitmapFactory.decodeFile(f.getPath());
        }

        if (bitmap == null) {
            // No
            return null;
        } else {
            // Yes
            return bitmap;
        }
    }

    // our caching functions
    // Find the dir to save cached images
    private static File getCacheDirectory(Context context, String folder) {
        File sdDir = context.getExternalCacheDir();
        File cacheDir;
        if (sdDir != null) {
            cacheDir = new File(sdDir, folder);
        } else
            cacheDir = context.getCacheDir();

        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir;
    }

}
