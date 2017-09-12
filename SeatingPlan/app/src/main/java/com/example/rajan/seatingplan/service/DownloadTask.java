package com.example.rajan.seatingplan.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.rajan.seatingplan.adapter.GridViewAdapter;
import com.example.rajan.seatingplan.controller.CallbackInterface;
import com.example.rajan.seatingplan.Config;

import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;

/**
 * A thread class for teacher client to get data from server
 */
public class DownloadTask extends AsyncTask<Object, Object, Boolean> {

    private CallbackInterface callbackInterface;
    private Context context;
    private GridViewAdapter gridViewAdapter;
    private Thread thread;

    /**
     *
     * @param gridViewAdapter the layout generated in this grid format
     * @param callbackInterface to update UI in running activity from background thread
     * @param context current activity
     */
    public DownloadTask(GridViewAdapter gridViewAdapter, CallbackInterface callbackInterface, Context context) {
        this.gridViewAdapter = gridViewAdapter;
        this.callbackInterface = callbackInterface;
        this.context = context;
    }

    /**
     *
     * @param aBoolean
     */
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Log.e("DownloadTask", "onPostExecute()");
    }

    /**
     *
     * @param urls default object
     * @return
     */
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

