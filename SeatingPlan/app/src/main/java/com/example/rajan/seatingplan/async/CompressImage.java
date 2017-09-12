package com.example.rajan.seatingplan.async;

import android.graphics.Bitmap;

import com.example.rajan.seatingplan.Config;
import com.example.rajan.seatingplan.Utils;

import java.io.FileNotFoundException;

/**
 * A thread class to compress the image
 */

public class CompressImage extends Thread{

    private String fileName;

    public CompressImage(String fileName) {
        this.fileName = fileName;
    }

    /**
     * this method will asynchronously and compresses the image to 50*50
     */
    @Override
    public void run() {
        Bitmap bitmap;
        try {
            bitmap = Utils.decodeSampledBitmapFromResource(Config.IMAGES_DIR + fileName,
                    Config.THUMBNAIL_WIDTH, Config.THUMBNAIL_HEIGHT);
            Utils.saveImage(bitmap, Config.IMAGES_DIR, fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
