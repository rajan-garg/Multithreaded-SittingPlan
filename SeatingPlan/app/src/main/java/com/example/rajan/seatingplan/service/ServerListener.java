package com.example.rajan.seatingplan.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.rajan.seatingplan.Config;
import com.example.rajan.seatingplan.Utils;
import com.example.rajan.seatingplan.adapter.GridViewAdapter;
import com.example.rajan.seatingplan.async.AsyncAttributesBuilder;
import com.example.rajan.seatingplan.async.CompressImage;
import com.example.rajan.seatingplan.async.CompressImagesForkJoin;
import com.example.rajan.seatingplan.controller.CallbackInterface;
import com.example.rajan.seatingplan.model.Student;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by root on 11/9/17.
 */

public class ServerListener implements Runnable {

    private Socket sock;
    private CallbackInterface callbackInterface;
    private ArrayList<Student> students;
    private Context context;
    private GridViewAdapter gridViewAdapter;

    public ServerListener(Socket sock, CallbackInterface callbackInterface, Context context, GridViewAdapter gridViewAdapter) {
        this.sock = sock;
        this.callbackInterface = callbackInterface;
        this.context = context;
        this.gridViewAdapter = gridViewAdapter;
    }

    @Override
    public void run() {

        Config.startTime = System.nanoTime();

        switch (Config.IMPLEMENTATION) {
            case 1:
                computeSync();
                break;
            case 2:
                computeAsync();
                break;
            case 3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    computeAsyncForkJoin();
                }
                break;
        }
    }

    private void computeSync() {
        try {
            BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
            DataInputStream din = new DataInputStream(bis);

            int filesCount = din.readInt();
            File[] files = new File[filesCount];

            Log.e("Sync", "files count = " + filesCount);

            for(int i = 0; i < filesCount; i++) {
                long fileLength = din.readLong();
                String fileName = din.readUTF();

                Log.e("Sync", "filename = " + fileName);
                files[i] = new File(Config.IMAGES_DIR + fileName);

                FileOutputStream fos = new FileOutputStream(files[i]);
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                for(int j = 0; j < fileLength; j++)
                    bos.write(bis.read());

                bos.close();
            }

            Long studentsCount = din.readLong();
            Log.e("Sync", "studentsCount = " + studentsCount);
            students = new ArrayList<>(gridViewAdapter.getStudents());
            for(int i=0; i<studentsCount; i++) {
                String s_roll = din.readUTF(), s_seat = din.readUTF();
                Log.e("Sync", "roll, seat = " + s_roll + ", " + s_seat);
                if(Utils.isInteger(s_seat) && Integer.parseInt(s_seat) > 0 && Integer.parseInt(s_seat) <= Config.TOTAL_SEATS) {
                    Integer seat = Integer.parseInt(s_seat) - 1;
                    students.get(seat).setSeat(String.valueOf(s_seat));
                    students.get(seat).setRoll(String.valueOf(s_roll));
                    Bitmap bitmap = Utils.decodeSampledBitmapFromResource(Config.IMAGES_DIR +
                            s_roll + ".jpg", Config.THUMBNAIL_WIDTH, Config.THUMBNAIL_HEIGHT);
                    students.get(seat).setImageBitmap(bitmap);
                }
            }

            if(null!=sock) {
                sock.close();
            }

            callbackInterface.onDataUpdate(students);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void computeAsync() {
        try {
            BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
            DataInputStream din = new DataInputStream(bis);

            int filesCount = din.readInt();
            File[] files = new File[filesCount];

            Log.e("Async", "files count = " + filesCount);

            ArrayList<CompressImage> imageThreads = new ArrayList<>();

            for(int i = 0; i < filesCount; i++) {
                long fileLength = din.readLong();
                String fileName = din.readUTF();

                Log.e("Async", "filename = " + fileName);
                files[i] = new File(Config.IMAGES_DIR + fileName);

                FileOutputStream fos = new FileOutputStream(files[i]);
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                for(int j = 0; j < fileLength; j++)
                    bos.write(bis.read());
                bos.close();

                CompressImage compute = new CompressImage(fileName);
                compute.start();

                imageThreads.add(compute);
            }

            Long studentsCount = din.readLong();
            Log.e("Async", "studentsCount = " + studentsCount);
            students = new ArrayList<>(gridViewAdapter.getStudents());
            ArrayList<AsyncAttributesBuilder> attributesBuilders = new ArrayList<>();

            for(int i=0; i<studentsCount; i++) {
                String s_roll = din.readUTF(), s_seat = din.readUTF();

                AsyncAttributesBuilder asyncAttributesBuilder = new AsyncAttributesBuilder(s_roll, s_seat);
                asyncAttributesBuilder.start();

                attributesBuilders.add(asyncAttributesBuilder);
            }

            if(null!=sock) {
                sock.close();
            }

            for(int i=0; i<studentsCount; i++)
                students.add(new Student("", ""));

            // wait
            for(CompressImage compressImage : imageThreads)
                compressImage.join();
            for(AsyncAttributesBuilder asyncAttributesBuilder: attributesBuilders)
                asyncAttributesBuilder.join();

            // update compressed bitmap
            for(AsyncAttributesBuilder asyncAttributesBuilder: attributesBuilders){
                Student student = asyncAttributesBuilder.getStudent();
                student.setImageBitmap(BitmapFactory.decodeFile(Config.IMAGES_DIR + student.getRoll() + ".jpg"));
                asyncAttributesBuilder.setStudent(student);
                students.set(Integer.parseInt(student.getSeat())-1, student);
            }

            callbackInterface.onDataUpdate(students);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void computeAsyncForkJoin() {
        try {
            BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
            DataInputStream din = new DataInputStream(bis);

            int filesCount = din.readInt();
            File[] files = new File[filesCount];
            String[] fileNames = new String[filesCount];

            Log.e("AsyncForkJoin", "files count = " + filesCount);

            for(int i = 0; i < filesCount; i++) {
                long fileLength = din.readLong();
                String fileName = din.readUTF();

                Log.e("AsyncForkJoin", "filename = " + fileName);
                files[i] = new File(Config.IMAGES_DIR + fileName);
                fileNames[i] = fileName;

                FileOutputStream fos = new FileOutputStream(files[i]);
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                for(int j = 0; j < fileLength; j++)
                    bos.write(bis.read());
                bos.close();
            }

            CompressImagesForkJoin mergeTask = new CompressImagesForkJoin(fileNames);
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            forkJoinPool.invoke(mergeTask);

            Long studentsCount = din.readLong();
            Log.e("AsyncForkJoin", "studentsCount = " + studentsCount);
            students = new ArrayList<>(gridViewAdapter.getStudents());
            ArrayList<AsyncAttributesBuilder> attributesBuilders = new ArrayList<>();

            for(int i=0; i<studentsCount; i++) {
                String s_roll = din.readUTF(), s_seat = din.readUTF();

                AsyncAttributesBuilder asyncAttributesBuilder = new AsyncAttributesBuilder(s_roll, s_seat);
                asyncAttributesBuilder.start();

                attributesBuilders.add(asyncAttributesBuilder);
            }

//            AsyncAttributesBuilderForkJoin mergeTask2 = new AsyncAttributesBuilderForkJoin(info);
//            ForkJoinPool forkJoinPool2 = new ForkJoinPool();
//            forkJoinPool.invoke(mergeTask2);

            if(null!=sock) {
                sock.close();
            }

            for(int i=0; i<studentsCount; i++)
                students.add(new Student("", ""));

            // wait
            mergeTask.join();
            for(AsyncAttributesBuilder asyncAttributesBuilder: attributesBuilders)
                asyncAttributesBuilder.join();

            // update compressed bitmap
            for(AsyncAttributesBuilder asyncAttributesBuilder: attributesBuilders){
                Student student = asyncAttributesBuilder.getStudent();
                student.setImageBitmap(BitmapFactory.decodeFile(Config.IMAGES_DIR + student.getRoll() + ".jpg"));
                asyncAttributesBuilder.setStudent(student);
                students.set(Integer.parseInt(student.getSeat())-1, student);
            }

            callbackInterface.onDataUpdate(students);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

