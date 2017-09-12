package com.example.lolap.student.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.lolap.student.service.DownloadTask;
import com.example.lolap.student.adapter.GridViewAdapter;
import com.example.lolap.student.R;
import com.example.lolap.student.Config;
import com.example.lolap.student.model.Student;

import java.util.ArrayList;

public class PlanActivity extends AppCompatActivity implements com.example.lolap.student.controller.CallbackInterface {

    private GridViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);

        // default bitmap to show as thumbnail
        Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);;

        // default students' data
        ArrayList<Student> students = new ArrayList<>();
        for(int i = 0; i < Config.TOTAL_SEATS; i++)
            students.add(new Student("", "", defaultBitmap));

        // inflate grid view via adapter
        gridViewAdapter = new GridViewAdapter(students, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, Config.SEATS_PER_ROW));
        recyclerView.setAdapter(gridViewAdapter);

        // start download task
        DownloadTask downloadTask = new DownloadTask(gridViewAdapter, this, this);
        downloadTask.execute();

     }

    @Override
    public void onDataUpdate(final ArrayList<Student> students) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gridViewAdapter.clearStudents();
                gridViewAdapter.setStudents(students);
                long timeDur = System.nanoTime() - Config.startTime;
                Log.e("Time Taken", String.valueOf(timeDur));
            }
        });
    }
}
