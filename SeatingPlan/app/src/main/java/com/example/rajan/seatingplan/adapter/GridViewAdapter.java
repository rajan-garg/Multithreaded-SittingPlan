package com.example.rajan.seatingplan.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rajan.seatingplan.Config;
import com.example.rajan.seatingplan.R;
import com.example.rajan.seatingplan.model.Student;

import java.util.ArrayList;

/**
 * Created by root on 7/9/17.
 */

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.MyViewHolder>{

    private ArrayList<Student> students;
    private Context context;

    public GridViewAdapter(ArrayList<Student> students, Context context) {
        this.students = students;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Student student = students.get(position);

        switch (Config.IMPLEMENTATION) {
            case 1:
                holder.roll_tv.setText(student.getRoll());
                holder.seat_tv.setText(student.getSeat());
                holder.imageView.setImageBitmap(student.getImageBitmap());
                break;
            case 2:
                // background thread
                Thread thread = new Thread(){
                    public void run(){
                        // UI thread
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.roll_tv.setText(student.getRoll());
                                holder.seat_tv.setText(student.getSeat());
                                holder.imageView.setImageBitmap(student.getImageBitmap());
                            }
                        });
                    }
                };
                thread.start();
                break;
            case 3:
                break;
        }

    }

    public void setStudents(ArrayList<Student> students) {
        for(Student student:students)
            Log.e("AdapterChanged(). r, s", student.getRoll() + ", " + student.getSeat());
        this.students = students;
        notifyDataSetChanged();
    }

    public void clearStudents() {
        this.students.clear();
        notifyDataSetChanged();
    }

    public ArrayList<Student> getStudents() {
        return this.students;
    }

    @Override
    public int getItemCount() {
        return (students == null) ? 0 : students.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView roll_tv, seat_tv;
        public ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            roll_tv = (TextView) itemView.findViewById(R.id.tv_roll);
            seat_tv = (TextView) itemView.findViewById(R.id.tv_seat);
            imageView = (ImageView) itemView.findViewById((R.id.imageView));
        }
    }
}