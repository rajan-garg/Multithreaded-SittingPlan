package com.example.rajan.seatingplan.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * A model for student containing roll, seat , image of student
 */

public class Student {
    private String roll;
    private String seat;
    private Bitmap imageBitmap;

    /**
     * Constructor of student with two parameters
     * @param roll
     * @param seat
     */
    public Student(String roll, String seat) {
        this.roll = roll;
        this.seat = seat;
    }

    /**
     * Constructor of student with two parameters
     * @param roll
     * @param seat
     * @param imageBitmap
     */
    public Student(String roll, String seat, Bitmap imageBitmap) {
        this.roll = roll;
        this.seat = seat;
        this.imageBitmap = imageBitmap;
    }

    /**
     *
     * @return roll number
     */
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
