package com.boboddy.vault.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nick on 3/27/16.
 */
public class Util {
    
    public static String createFilename(Context ctx) {
        return ctx.getFilesDir() + File.separator + getImageFileName();
    }

    private static String getImageFileName() {
        String filename = "";

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        filename = timeStamp + ".png";

        return filename;
    }

    public static Bitmap getThumbnail(String filepath) {
        Bitmap bmp = null;
        if(filepath != null && !filepath.equals("")) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filepath, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 200, 200);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeFile(filepath, options);
        }
        return bmp;
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
