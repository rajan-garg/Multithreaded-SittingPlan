package com.example.rajan.seatingplan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by root on 11/9/17.
 */

public class Utils {

    /**
     * checks if device is online
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    /**
     * calculate in sample size
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * compresses bitmap
     * @param path
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws FileNotFoundException
     */
    public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) throws FileNotFoundException {
        File f = new File(path);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new FileInputStream(f), null, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeStream(new FileInputStream(f), null, options);
    }

    /**
     * save image in phone memory
     * @param finalBitmap
     * @param dirPath
     * @param fileName
     */
    public static void saveImage(Bitmap finalBitmap, String dirPath, String fileName) {
        File myDir = new File(dirPath);
        myDir.mkdirs();
        File file = new File (myDir, fileName);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getFiles(String directoryPath) {
        ArrayList<String> MyFiles = new ArrayList<>();
        File f = new File(directoryPath);
        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else
            for (File file : files) MyFiles.add(file.getAbsolutePath());
        return MyFiles;
    }

    /**
     * checks if str is integer
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        try {
            Integer i = Integer.parseInt(str);
        }
        catch(Exception e) {
            return false;
        }
        return true;
    }
}
