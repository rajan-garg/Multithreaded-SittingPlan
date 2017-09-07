package com.example.rajan.seatingplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {


    private EditText roll_et;
    private EditText seat_et;
    private String roll, seat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        roll_et = (EditText) findViewById(R.id.ROLL);
        seat_et = (EditText) findViewById(R.id.SEAT);
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
                Intent myIntent = new Intent(MainActivity.this, PlanActivity.class);
                startActivity(myIntent);
            }
        });

    }

}