package com.example.rajan.seatingplan.controller;

import com.example.rajan.seatingplan.model.Student;

import java.util.ArrayList;

/**
 * An interface to update UI in running activity from background thread
 */

public interface CallbackInterface {
    public void onDataUpdate(ArrayList<Student> students);
}
