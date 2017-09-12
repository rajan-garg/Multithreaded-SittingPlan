package com.example.lolap.student.controller;

import com.example.lolap.student.model.Student;

import java.util.ArrayList;

/**
 * Created by root on 11/9/17.
 */

public interface CallbackInterface {
    public void onDataUpdate(ArrayList<Student> students);
}
