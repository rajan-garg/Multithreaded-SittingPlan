package com.example.lolap.student.async;

import android.graphics.Bitmap;


import com.example.lolap.student.Config;
import com.example.lolap.student.Utils;

import java.io.FileNotFoundException;

/**
 * Created by root on 12/9/17.
 */

public class CompressImage extends Thread{

    private String fileName;

    public CompressImage(String fileName) {
        this.fileName = fileName;
    }

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
