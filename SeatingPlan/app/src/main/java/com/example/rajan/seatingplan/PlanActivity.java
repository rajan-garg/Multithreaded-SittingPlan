package com.example.rajan.seatingplan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlanActivity extends AppCompatActivity {

    String da;
    Map<String,String> map=new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);

        DownloadTask downloadTask = new DownloadTask(map);
        downloadTask.execute();

        ArrayList<Student> students = new ArrayList<>();
        for(int i = 0; i < Config.TOTAL_SEATS; i++)
            students.add(new Student("", ""));

        GridViewAdapter gridViewAdapter = new GridViewAdapter(students);
        recyclerView.setLayoutManager(new GridLayoutManager(this, Config.SEATS_PER_ROW));
        recyclerView.setAdapter(gridViewAdapter);

//        for(Map.Entry m:map.entrySet()) {
//            String path = "/storage/sdcard0/" + m.getKey() + ".jpg";
//            Bitmap bmp = BitmapFactory.decodeFile(path);
//            img.setImageBitmap(bmp);
//         }
     }

}
