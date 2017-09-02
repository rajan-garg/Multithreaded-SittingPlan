package com.example.rajan.seatingplan;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class PlanActivity extends AppCompatActivity {

    String da;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        final TextView t1 = (TextView)findViewById(R.id.textView1);


        class DownloadImageTask2 extends AsyncTask<String,String,String> {

            protected String doInBackground(String... urls) {
                Socket sock;
                try {
                    sock = new Socket("192.168.0.100", 8000);
                    System.out.println("Connecting...");
                    DataOutputStream dOut = new DataOutputStream(sock.getOutputStream());
                    dOut.writeByte(2);
                    DataInputStream dIn = new DataInputStream(sock.getInputStream());
                    byte messageType = dIn.readByte();
                    da = dIn.readUTF();
                    System.out.println("Message A: " + da);
                    System.out.println(da);

//                        sock.close();
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

        new DownloadImageTask2().execute();
        t1.setText(da);

    }
}
