package com.example.rajan.seatingplan.async;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.rajan.seatingplan.Config;
import com.example.rajan.seatingplan.Utils;
import com.example.rajan.seatingplan.model.Student;

/**
 * Created by root on 12/9/17.
 */

public class AsyncAttributesBuilder extends Thread {

    private String s_roll, s_seat;
    private Student student;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public AsyncAttributesBuilder(String s_roll, String s_seat) {
        this.s_roll = s_roll;
        this.s_seat = s_seat;
    }

    @Override
    public void run() {
        Log.e("ServerListener", "roll, seat = " + s_roll + ", " + s_seat);
        if(Utils.isInteger(s_seat) && Integer.parseInt(s_seat) > 0 && Integer.parseInt(s_seat) <= Config.TOTAL_SEATS) {
            student = new Student(s_roll, s_seat);
        }
    }
}
