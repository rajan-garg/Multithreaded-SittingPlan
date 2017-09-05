package com.example.rajan.seatingplan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class PlanActivity extends AppCompatActivity {

    String da;
    Map<String,String> map=new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        ImageView img = (ImageView) findViewById(R.id.imageView2);

        class DownloadImageTask2 extends AsyncTask<String,String,String> {

            protected String doInBackground(String... urls) {
                Socket sock;
                try {
                    sock = new Socket("192.168.0.103", 8000);
                    System.out.println("Connecting...");
                    DataOutputStream dOut = new DataOutputStream(sock.getOutputStream());
                    dOut.writeByte(2);
                    DataInputStream dIn = new DataInputStream(sock.getInputStream());
                    String str;
                    while(!((str=dIn.readUTF()).equals("")))
                    {
                        StringTokenizer st = new StringTokenizer(str," ");
                        map.put(st.nextToken(),st.nextToken());
                    }
                    System.out.println("file reading done");
                    for(Map.Entry m:map.entrySet()){
                       System.out.println(m.getKey()+" "+m.getValue());
                        String path = "/storage/sdcard0/" + m.getKey() + ".jpg";
                        File imageFile = new  File(path);
                        if(!imageFile.exists())
                        {
                            System.out.println("image copying..");
                            dOut.writeByte(1);
                            dOut.writeUTF((String)m.getKey());
                            FileOutputStream fout = new FileOutputStream(path);
                            int i;
                            while ( (i = dIn.read()) > -1) {
                                fout.write(i);
                            }
                            fout.flush();
                            fout.close();
                            System.out.println("image copied");
                        }
                    }
                    dOut.writeByte(0);

                     sock.close();
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return "done";
            }

            protected void onPostExecute() {

            }
        }
    while(true) {
        new DownloadImageTask2().execute();

        for (Map.Entry m : map.entrySet()) {
            String path = "/storage/sdcard0/" + m.getKey() + ".jpg";
            Bitmap bmp = BitmapFactory.decodeFile(path);
            img.setImageBitmap(bmp);
        }
    }
     }

}
