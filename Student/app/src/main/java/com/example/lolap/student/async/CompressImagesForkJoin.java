package com.example.lolap.student.async;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;


import com.example.lolap.student.Config;
import com.example.lolap.student.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

/**
 * Created by root on 12/9/17.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CompressImagesForkJoin extends RecursiveTask<Boolean>{

    private String[] files;

    public CompressImagesForkJoin(String[] files) {
        this.files = files;
    }

    @Override
    protected Boolean compute() {
        if(files.length == 1) {
            Bitmap bitmap;
            try {
                bitmap = Utils.decodeSampledBitmapFromResource(Config.IMAGES_DIR + files[0],
                        Config.THUMBNAIL_WIDTH, Config.THUMBNAIL_HEIGHT);
                Utils.saveImage(bitmap, Config.IMAGES_DIR, files[0]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (files.length > 1) {
            ArrayList<String[]> partitions = partitionList();
            CompressImagesForkJoin mergeTask1 = new CompressImagesForkJoin(partitions.get(0));
            CompressImagesForkJoin mergeTask2 = new CompressImagesForkJoin(partitions.get(2));
            invokeAll(mergeTask1, mergeTask2);
            mergeTask1.join();
            mergeTask2.join();
        }
        return true;
    }

    private ArrayList<String[]> partitionList() {
        int chunkSize = files.length % 2 == 0 ? files.length / 2 : (files.length / 2) + 1;
        ArrayList<String[]> result = new ArrayList<>();
        String[] partition1 = new String[chunkSize];
        String[] partition2 = new String[files.length - chunkSize];

        for(int i=0; i<chunkSize; i++)             partition1[i] = files[i];
        for(int i=chunkSize; i<files.length; i++)  partition2[i-chunkSize] = files[i];

        result.add(partition1);
        result.add(partition2);

        return result;
    }
}
