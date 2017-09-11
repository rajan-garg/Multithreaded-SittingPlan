package com.example.rajan.seatingplan.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by root on 7/9/17.
 */

public class Student {
    private String roll;
    private String seat;
    private Bitmap imageBitmap;

    public Student(String roll, String seat) {
        this.roll = roll;
        this.seat = seat;
    }

    public Student(String roll, String seat, Bitmap imageBitmap) {
        this.roll = roll;
        this.seat = seat;
        this.imageBitmap = imageBitmap;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
