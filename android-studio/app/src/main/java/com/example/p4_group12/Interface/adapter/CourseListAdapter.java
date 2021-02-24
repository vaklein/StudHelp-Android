package com.example.p4_group12.Interface.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.R;


// Followed https://www.youtube.com/watch?v=17NbUcEts9c for the code and xml layout
public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {

    public static class CourseListViewHolder extends RecyclerView.ViewHolder {

        // Here goes the elements of each item of the recycler view
        // TODO add the elements inside the xml file

        public CourseListViewHolder(@NonNull View itemView) {
            super(itemView);
            // Example to get the findViewById
            // var = itemView.findViewById(R.id.[]);
        }
    }

    @NonNull
    @Override
    public CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        CourseListViewHolder clh = new CourseListViewHolder(v);
        return  clh;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
