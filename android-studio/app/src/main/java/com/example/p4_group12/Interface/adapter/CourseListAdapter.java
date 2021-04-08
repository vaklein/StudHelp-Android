package com.example.p4_group12.Interface.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.Interface.GlobalVariables;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;

import java.util.ArrayList;
import java.util.HashSet;


// Followed https://www.youtube.com/watch?v=17NbUcEts9c for the code and xml layout
// Followed https://www.youtube.com/watch?v=bhhs4bwYyhc for the onClickListeners
public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {

    public ArrayList<Course> courseList;
    final private ArrayList<Course> allCourses;
    private OnCourseClickListener courseClickListener;
    private HashSet<Integer> favoritesID;
    private boolean showOnlyFav = false;

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
        private CheckBox favoriteCheckBox;
        private API api;


        public CourseListViewHolder(@NonNull View itemView, OnCourseClickListener courseClickListener) {
            super(itemView);
            codeTextView = itemView.findViewById(R.id.code);
            courseNameTextView = itemView.findViewById(R.id.course_name);
            teacherTextView = itemView.findViewById(R.id.teacher);
            favoriteCheckBox = itemView.findViewById(R.id.favoriteCourseCheckBox);

            this.api = API.getInstance();

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

    public CourseListAdapter(ArrayList<Course> courseList, HashSet<Integer> favoritesID){
        this.allCourses = new ArrayList<>(courseList);
        this.courseList = courseList;
        this.favoritesID = favoritesID;
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
        final Course currentCourse = courseList.get(position);

        holder.codeTextView.setText(currentCourse.getCode());
        holder.courseNameTextView.setText(currentCourse.getName());
        // TODO remove it from the layout
        // holder.teacherTextView.setText(currentCourse.getTeacher());
        if(favoritesID.contains(currentCourse.getID())) {
            holder.favoriteCheckBox.setChecked(true);
        }
        else holder.favoriteCheckBox.setChecked(false);
        holder.favoriteCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.favoriteCheckBox.isChecked()){
                    // Log.d("Gwen", "Adding " + currentCourse.getCode() + " to the favorites");
                    favoritesID.add(currentCourse.getID());
                    holder.api.addNewFavoriteToUser(GlobalVariables.getUser(), currentCourse);
                }
                else{
                    // Log.d("Gwen", "Removing " + currentCourse.getCode() + " from the favorites");
                    favoritesID.remove(currentCourse.getID());
                    holder.api.removeFavoriteToUser(GlobalVariables.getUser(), currentCourse);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Course> filteredCourses = new ArrayList<>();
                String queryString = constraint.toString().toLowerCase().trim();

                for (Course course : allCourses) {
                    if ((course.getCode().toLowerCase().trim().contains(queryString) ||
                            course.getName().toLowerCase().trim().contains(queryString)) &&
                            (!showOnlyFav  || favoritesID.contains(course.getID()))) {
                        filteredCourses.add(course);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredCourses;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<Course> filteredCourseIds = (ArrayList<Course>) results.values;
                courseList.clear();
                for(Course course: filteredCourseIds){
                    courseList.add(course);
                }
                notifyDataSetChanged();
            }
        };
    }



    public void favoriteFilter(String currentQuery){
        this.showOnlyFav = true;
        this.getFilter().filter(currentQuery);
    }

    public void resetFavoriteFilter(String currentQuery){
        this.showOnlyFav = false;
        this.getFilter().filter(currentQuery);
    }
}
