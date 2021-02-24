package com.example.p4_group12.Interface.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.R;

import java.util.ArrayList;


// Followed https://www.youtube.com/watch?v=17NbUcEts9c for the code and xml layout
public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {

    private ArrayList<Course> courseList;

    public static class CourseListViewHolder extends RecyclerView.ViewHolder {

        // Here goes the elements of each item of the recyclerview item
        private TextView codeTextView;
        private TextView courseNameTextView;
        private TextView teacherTextView;


        public CourseListViewHolder(@NonNull View itemView) {
            super(itemView);
            codeTextView = itemView.findViewById(R.id.code);
            courseNameTextView = itemView.findViewById(R.id.course_name);
            teacherTextView = itemView.findViewById(R.id.teacher);
        }
    }

    public CourseListAdapter(ArrayList<Course> courseList){
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Log.v("Gwen", "test");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        CourseListViewHolder clh = new CourseListViewHolder(v);
        return  clh;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListViewHolder holder, int position) {
        Course currentCourse = courseList.get(position);

        holder.codeTextView.setText(currentCourse.getCode());
        holder.courseNameTextView.setText(currentCourse.getName());
        holder.teacherTextView.setText(currentCourse.getTeacher());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
