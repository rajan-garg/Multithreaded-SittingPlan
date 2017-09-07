package com.example.rajan.seatingplan;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by root on 7/9/17.
 */

public class UploadTask extends AsyncTask<Object, Object, Boolean> {

    private String roll, seat;

    public UploadTask(String roll, String seat) {
        this.roll = roll;
        this.seat = seat;
    }

    protected Boolean doInBackground(Object... urls) {
        Socket sock;
        try {
            sock = new Socket(Config.IP_ADDR, 8000);
            DataOutputStream dOut = new DataOutputStream(sock.getOutputStream());
            dOut.writeByte(1);
            dOut.writeUTF(roll +" " + seat + "\n");
            dOut.flush(); // Send off the data
            dOut.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

}