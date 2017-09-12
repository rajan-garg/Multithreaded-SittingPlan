package com.example.lolap.student.async;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.lolap.student.Config;
import com.example.lolap.student.Utils;
import com.example.lolap.student.model.Student;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

/**
 * Created by root on 12/9/17.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class AsyncAttributesBuilderForkJoin extends RecursiveTask<ArrayList<Student>> {

    private ArrayList<String[]> info;

    public AsyncAttributesBuilderForkJoin(ArrayList<String[]> info) {
        this.info = info;
    }

    @Override
    protected ArrayList<Student> compute() {
        if(info.size() == 1) {
            String s_roll = info.get(0)[0], s_seat = info.get(0)[1];
            Log.e("ServerListener", "roll, seat = " + s_roll + ", " + s_seat);
            if(Utils.isInteger(s_seat) && Integer.parseInt(s_seat) > 0 && Integer.parseInt(s_seat) <= Config.TOTAL_SEATS) {
                Student student = new Student(s_roll, s_seat);
                ArrayList<Student> res = new ArrayList<>();
                res.add(student);
                return  res;
            }
        } else if (info.size() > 1) {
            ArrayList<ArrayList<String[]>> partitions = partitionList();
            AsyncAttributesBuilderForkJoin mergeTask1 = new AsyncAttributesBuilderForkJoin(partitions.get(0));
            AsyncAttributesBuilderForkJoin mergeTask2 = new AsyncAttributesBuilderForkJoin(partitions.get(2));
            invokeAll(mergeTask1, mergeTask2);

            ArrayList<Student> res1 = mergeTask1.join();
            ArrayList<Student> res2 = mergeTask2.join();

            return merge(res1, res2);
        }
        return null;
    }

    private ArrayList<Student> merge(ArrayList<Student> res1, ArrayList<Student> res2) {
        ArrayList<Student> res = new ArrayList<>();
        for(Student student: res1)
            res.add(student);
        for(Student student: res2)
            res.add(student);
        return res;
    }

    private ArrayList<ArrayList<String[]>> partitionList() {
        int chunkSize = info.size() % 2 == 0 ? info.size() / 2 : (info.size() / 2) + 1;
        ArrayList<ArrayList<String[]>> result = new ArrayList<>();
        ArrayList<String[]> partition1 = new ArrayList<>();
        ArrayList<String[]> partition2 = new ArrayList<>();

        for(int i=0; i<chunkSize; i++)            partition1.add(info.get(i));
        for(int i=chunkSize; i<info.size(); i++)  partition2.add(info.get(i));

        result.add(partition1);
        result.add(partition2);

        return result;
    }
}
