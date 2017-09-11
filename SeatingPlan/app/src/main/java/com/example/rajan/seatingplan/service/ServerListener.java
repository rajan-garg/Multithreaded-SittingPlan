package com.example.rajan.seatingplan.service;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.rajan.seatingplan.Utils;
import com.example.rajan.seatingplan.adapter.GridViewAdapter;
import com.example.rajan.seatingplan.controller.CallbackInterface;
import com.example.rajan.seatingplan.Config;
import com.example.rajan.seatingplan.model.Student;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

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
        try {
            BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
            DataInputStream din = new DataInputStream(bis);

            int filesCount = din.readInt();
            File[] files = new File[filesCount];

            Log.e("ServerListener", "files count = " + filesCount);

            for(int i = 0; i < filesCount; i++) {
                long fileLength = din.readLong();
                String fileName = din.readUTF();

                Log.e("ServerListener", "filename = " + fileName);
                files[i] = new File(Config.IMAGES_DIR + fileName);

                FileOutputStream fos = new FileOutputStream(files[i]);
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                for(int j = 0; j < fileLength; j++)
                    bos.write(bis.read());

                bos.close();
            }

            Long studentsCount = din.readLong();
            Log.e("ServerListener", "studentsCount = " + studentsCount);
            students = new ArrayList<>(gridViewAdapter.getStudents());
            for(int i=0; i<studentsCount; i++) {
                String s_roll = din.readUTF(), s_seat = din.readUTF();
                Log.e("ServerListener", "roll, seat = " + s_roll + ", " + s_seat);
                if(Utils.isInteger(s_seat) && Integer.parseInt(s_seat) > 0 && Integer.parseInt(s_seat) <= Config.TOTAL_SEATS) {
                    Integer seat = Integer.parseInt(s_seat) - 1;
                    students.get(seat).setSeat(String.valueOf(s_seat));
                    students.get(seat).setRoll(String.valueOf(s_roll));
                    students.get(seat).setImageBitmap(BitmapFactory.decodeFile(Config.IMAGES_DIR + s_roll + ".jpg"));
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
}

