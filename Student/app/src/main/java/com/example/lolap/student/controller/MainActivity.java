package com.example.lolap.student.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lolap.student.Config;
import com.example.lolap.student.R;
import com.example.lolap.student.service.UploadTask;

public class MainActivity extends AppCompatActivity {

    private EditText roll_et;
    private EditText seat_et;
    private EditText implementation_et;
    private String roll, seat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        roll_et = (EditText) findViewById(R.id.ROLL);
        seat_et = (EditText) findViewById(R.id.SEAT);
        implementation_et = (EditText) findViewById(R.id.implementation);
        Button sendBtn = (Button) findViewById(R.id.bSend);
        Button showBtn = (Button) findViewById(R.id.showbtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                roll = roll_et.getText().toString();
                seat = seat_et.getText().toString();
                UploadTask uploadTask = new UploadTask(roll, seat);
                uploadTask.execute();
            }
        });

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String imp = String.valueOf(implementation_et.getText());
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