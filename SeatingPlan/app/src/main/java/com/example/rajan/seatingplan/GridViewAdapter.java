package com.example.rajan.seatingplan;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by root on 7/9/17.
 */

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.MyViewHolder>{

    private ArrayList<Student> students;

    public GridViewAdapter(ArrayList<Student> students) {
        this.students = students;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Student student = students.get(position);
        holder.roll_tv.setText(student.getRoll());
        holder.seat_tv.setText(student.getSeat());
//        holder.imageView.setImageBitmap(student.getImageBitmap());
    }

    @Override
    public int getItemCount() {
        return students.size();
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