package com.example.rajan.seatingplan.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.rajan.seatingplan.service.DownloadTask;
import com.example.rajan.seatingplan.adapter.GridViewAdapter;
import com.example.rajan.seatingplan.R;
import com.example.rajan.seatingplan.Config;
import com.example.rajan.seatingplan.model.Student;

import java.util.ArrayList;

public class PlanActivity extends AppCompatActivity implements CallbackInterface {

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
        if (Config.IMPLEMENTATION == 3) {
            // TODO Implement fork join framework
        }
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gridViewAdapter.clearStudents();
                    gridViewAdapter.setStudents(students);
                }
            });
        }
    }
}
