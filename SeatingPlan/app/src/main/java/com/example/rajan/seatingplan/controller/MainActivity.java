package com.example.rajan.seatingplan.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.rajan.seatingplan.Config;
import com.example.rajan.seatingplan.R;
import com.example.rajan.seatingplan.service.UploadTask;

public class MainActivity extends AppCompatActivity {

    private EditText roll_et;
    private EditText seat_et;
    private EditText implementation_et;
    private EditText server_address_et;
    private String roll, seat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        roll_et = (EditText) findViewById(R.id.ROLL);
        seat_et = (EditText) findViewById(R.id.SEAT);
        implementation_et = (EditText) findViewById(R.id.implementation);
        server_address_et = (EditText) findViewById(R.id.ip);
        Button sendBtn = (Button) findViewById(R.id.bSend);
        Button showBtn = (Button) findViewById(R.id.showbtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                roll = roll_et.getText().toString();
                seat = seat_et.getText().toString();
                Config.SERVER_IP_ADDRESS = server_address_et.getText().toString();
                UploadTask uploadTask = new UploadTask(roll, seat);
                uploadTask.execute();
            }
        });

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Config.SERVER_IP_ADDRESS = server_address_et.getText().toString();

                String imp = implementation_et.getText().toString();
                try {
                    Config.IMPLEMENTATION = Integer.valueOf(imp);
                } catch (Exception e) {
                    Config.IMPLEMENTATION = 1;
                }
                Intent myIntent = new Intent(MainActivity.this, PlanActivity.class);
                startActivity(myIntent);
            }
        });

    }

}