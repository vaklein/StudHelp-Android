package com.example.p4_group12.Interface.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.R;

import java.util.ArrayList;
import java.util.List;


// Followed https://www.youtube.com/watch?v=17NbUcEts9c for the code and xml layout
// Followed https://www.youtube.com/watch?v=bhhs4bwYyhc for the onClickListeners
public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {

    private ArrayList<Course> courseList;
    final private ArrayList<Course> allCourses;
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
        this.allCourses = courseList;
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

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Course> filteredList = new ArrayList<>();
                String queryString = constraint.toString().toLowerCase().trim();

                if (queryString.isEmpty()) {
                    filteredList.addAll(allCourses);
                } else {
                    for (Course course : allCourses) {
                        if (course.getCode().toLowerCase().trim().contains(queryString) ||
                                course.getName().toLowerCase().trim().contains(queryString) ||
                                course.getTeacher().toLowerCase().trim().contains(queryString)) {
                            filteredList.add(course);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                courseList = (ArrayList<Course>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
