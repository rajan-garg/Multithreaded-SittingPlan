package com.example.rajan.seatingplan.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.rajan.seatingplan.service.DownloadTask;
import com.example.rajan.seatingplan.adapter.GridViewAdapter;
import com.example.rajan.seatingplan.R;
import com.example.rajan.seatingplan.Config;
import com.example.rajan.seatingplan.model.Student;

import java.util.ArrayList;

/**
 * Client activity for teacher that gets data from server and shows layout
 */
public class PlanActivity extends AppCompatActivity implements CallbackInterface {

    private GridViewAdapter gridViewAdapter;

    /**
     * called when activity is created
     * @param savedInstanceState
     */
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

    /**
     * called when some data is updated
     * @param students
     */
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
