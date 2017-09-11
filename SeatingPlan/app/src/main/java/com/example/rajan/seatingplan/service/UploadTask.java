package com.example.rajan.seatingplan.service;

import android.os.AsyncTask;

import com.example.rajan.seatingplan.Config;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by root on 11/9/17.
 */


public class UploadTask extends AsyncTask<Object, Object, Boolean> {

    private String roll, seat;

    public UploadTask(String roll, String seat) {
        this.roll = roll;
        this.seat = seat;
    }

    protected Boolean doInBackground(Object... urls) {

        try {
            Socket sock = new Socket(Config.SERVER_IP_ADDRESS, 8000);
            DataOutputStream dOut = new DataOutputStream(sock.getOutputStream());
            dOut.writeByte(1);
            dOut.writeUTF(roll +" " + seat + "\n");

            // upload
            dOut.flush();
            dOut.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

}