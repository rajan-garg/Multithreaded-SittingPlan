package com.example.rajan.seatingplan.controller;

import com.example.rajan.seatingplan.model.Student;

import java.util.ArrayList;

/**
 * Created by root on 11/9/17.
 */

public interface CallbackInterface {
    public void onDataUpdate(ArrayList<Student> students);
}
