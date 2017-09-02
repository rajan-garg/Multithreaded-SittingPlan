package com.example.rajan.seatingplan;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    private ImageView img;

    private EditText eText;
    private EditText eText1;
    public String roll="5";
    public String seat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eText = (EditText) findViewById(R.id.edittext);
        eText1 = (EditText) findViewById(R.id.edittext1);
        System.out.println("34");
        System.out.println("36");
        System.out.println("51");
        Button send = (Button) findViewById(R.id.bSend);

        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                roll = eText.getText().toString();
                seat = eText1.getText().toString();
                new DownloadImageTask().execute();

            }


            class DownloadImageTask extends AsyncTask<String,String,String> {

                protected String doInBackground(String... urls) {
                    Socket sock;
                    try {
                        sock = new Socket("192.168.0.100", 8000);
                        System.out.println("Connecting...");
                        DataOutputStream dOut = new DataOutputStream(sock.getOutputStream());
                        dOut.writeByte(1);
                        dOut.writeUTF(roll);
                        dOut.flush(); // Send off the data
                        dOut.close();
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
        });

        ((Button) findViewById(R.id.showbtn)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new DownloadImageTask2().execute();
            }

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

                        System.out.println("Message A: " + dIn.readUTF());

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

        });

        }

}