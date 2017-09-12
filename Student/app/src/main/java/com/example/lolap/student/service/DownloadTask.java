package com.example.lolap.student.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.lolap.student.adapter.GridViewAdapter;
import com.example.lolap.student.controller.CallbackInterface;
import com.example.lolap.student.Config;

import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;

/**
 * Created by root on 7/9/17.
 */

public class DownloadTask extends AsyncTask<Object, Object, Boolean> {

    private CallbackInterface callbackInterface;
    private Context context;
    private GridViewAdapter gridViewAdapter;
    private Thread thread;

    public DownloadTask(GridViewAdapter gridViewAdapter, CallbackInterface callbackInterface, Context context) {
        this.gridViewAdapter = gridViewAdapter;
        this.callbackInterface = callbackInterface;
        this.context = context;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Log.e("DownloadTask", "onPostExecute()");
    }

    protected Boolean doInBackground(Object... urls) {

        Log.e("DownloadTask", "doInBackground()");

        File folder = new File(Config.IMAGES_DIR);
        if (!folder.exists())
            folder.mkdir();

        Socket sock;

        try {
            sock = new Socket(Config.SERVER_IP_ADDRESS, 8000);

            // send a flag byte to distinguish teacher from student
            DataOutputStream dOut = new DataOutputStream(sock.getOutputStream());
            dOut.writeByte(2);

            // start listening for server response asynchronously
            ServerListener serverListener = new ServerListener(sock, callbackInterface, context, gridViewAdapter);
            thread = new Thread(serverListener);
            thread.start();

        } catch (Exception e) { e.printStackTrace(); }

        return true;
    }



}

