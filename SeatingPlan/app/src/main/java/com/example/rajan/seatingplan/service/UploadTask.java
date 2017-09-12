package com.example.rajan.seatingplan.service;

import android.os.AsyncTask;

import com.example.rajan.seatingplan.Config;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * A thread class for student client to upload data on server
 */
public class UploadTask extends AsyncTask<Object, Object, Boolean> {

    private String roll, seat;

    /**
     *
     * @param roll the roll number of student
     * @param seat the seat number of student
     */
    public UploadTask(String roll, String seat) {
        this.roll = roll;
        this.seat = seat;
    }

    /**
     * 
     * @param urls default object
     * @return
     */
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