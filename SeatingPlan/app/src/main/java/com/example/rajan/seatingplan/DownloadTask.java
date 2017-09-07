package com.example.rajan.seatingplan;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by root on 7/9/17.
 */

public class DownloadTask extends AsyncTask<Object, Object, Boolean> {

    private Map<String,String> map = new HashMap<String,String>();

    public DownloadTask(Map<String,String> map) {
        this.map = map;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        for(Map.Entry m:map.entrySet()) {
            System.out.println(m.getKey()+" "+m.getValue());
        }
//        map.clear();
//        DownloadTask downloadTask = new DownloadTask(map);
//        downloadTask.execute();

        Log.e("DownloadTask", "onPostExecute()");
    }

    protected Boolean doInBackground(Object... urls) {

        Log.e("DownloadTask", "doInBackground()");
        Socket sock;
        try {
            sock = new Socket(Config.IP_ADDR, 8000);
            DataOutputStream dOut = new DataOutputStream(sock.getOutputStream());
            dOut.writeByte(2);
            DataInputStream dIn = new DataInputStream(sock.getInputStream());
            String str;
            while(!((str=dIn.readUTF()).equals("")))
            {
                Log.e("frrf",str);
                StringTokenizer st = new StringTokenizer(str," ");
                map.put(st.nextToken(),st.nextToken());
            }
            Log.e("dsad0","file read done");
            for(Map.Entry m:map.entrySet()){
                Log.e("DT:DIB", m.getKey()+" "+m.getValue());
                String path = "/storage/sdcard0/" + m.getKey() + ".jpg";
                File imageFile = new  File(path);
                if(!imageFile.exists())
                {
                    dOut.writeByte(1);
                    dOut.writeUTF((String)m.getKey());
                    FileOutputStream fout = new FileOutputStream(path);
                    int i;
                    while ( (i = dIn.read()) > -1) {
                        fout.write(i);
                    }
                    fout.flush();
                    fout.close();
                }
            }
            dOut.writeByte(0);

            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

}

