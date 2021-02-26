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
// Followed https://www.youtube.com/watch?v=bhhs4bwYyhc for the onClickListeners
public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {

    private ArrayList<Course> courseList;
    private OnCourseClickListener courseClickListener;

    public interface OnCourseClickListener {
        void OnCourseClick(int position);
    }

    public void setCourseClickListener(OnCourseClickListener courseClickListener){
        this.courseClickListener = courseClickListener;
    }

    public static class CourseListViewHolder extends RecyclerView.ViewHolder {

        // Here goes the elements of each item of the recyclerview item
        private TextView codeTextView;
        private TextView courseNameTextView;
        private TextView teacherTextView;


        public CourseListViewHolder(@NonNull View itemView, OnCourseClickListener courseClickListener) {
            super(itemView);
            codeTextView = itemView.findViewById(R.id.code);
            courseNameTextView = itemView.findViewById(R.id.course_name);
            teacherTextView = itemView.findViewById(R.id.teacher);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(courseClickListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            courseClickListener.OnCourseClick(position);
                        }
                    }
                }
            });
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
        CourseListViewHolder clh = new CourseListViewHolder(v, this.courseClickListener);
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
