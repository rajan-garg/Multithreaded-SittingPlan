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

/**
 * grid view adapter to show
 */
public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.MyViewHolder>{

    private ArrayList<Student> students;
    private Context context;

    /**
     *  Constructor grid view adapter to show
     * @param students array list of students to be inflated in grid view
     * @param context context of running activity
     */
    public GridViewAdapter(ArrayList<Student> students, Context context) {
        this.students = students;
        this.context = context;
    }

    /**
     * This function is called when grid view is inflated
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * This function is called when item is inflated
     * @param holder parent view
     * @param position position of current item
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Student student = students.get(position);
        holder.roll_tv.setText("Roll: " + student.getRoll());
        holder.seat_tv.setText("Seat: " + student.getSeat());
        holder.imageView.setImageBitmap(student.getImageBitmap());

    }

    /**
     * This function sets array list of students
     * @param students list of students
     */
    public void setStudents(ArrayList<Student> students) {
//        for(Student student:students)
//            Log.e("AdapterChanged(). r, s", student.getRoll() + ", " + student.getSeat());
        this.students = students;
        notifyDataSetChanged();
    }

    /**
     * clears array list of students
     */
    public void clearStudents() {
        this.students.clear();
        notifyDataSetChanged();
    }

    /**
     * returns list of students
     * @return list of students
     */
    public ArrayList<Student> getStudents() {
        return this.students;
    }

    /**
     *
     * @return number of students
     */
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