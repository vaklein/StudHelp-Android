package com.example.p4_group12.Interface.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.R;

import java.util.ArrayList;


// Followed https://www.youtube.com/watch?v=17NbUcEts9c for the code and xml layout
public class AdvertisementListAdapter extends RecyclerView.Adapter<AdvertisementListAdapter.CourseListViewHolder> {

    private ArrayList<Advertisement> advertisementList;

    public static class CourseListViewHolder extends RecyclerView.ViewHolder {

        // Here goes the elements of each item of the recyclerview item
        private TextView usernameTextView;
        private TextView advertisementTitleTextView;
        private TextView advertisementDescriptionTextView;


        public CourseListViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username);
            advertisementTitleTextView = itemView.findViewById(R.id.advertisement_title_view);
            advertisementDescriptionTextView = itemView.findViewById(R.id.advertisement_description_view);
        }
    }

    public AdvertisementListAdapter(ArrayList<Advertisement> advertisementList){
        this.advertisementList = advertisementList;
    }

    @NonNull
    @Override
    public CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Log.v("Gwen", "test");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.advertisement_item, parent, false);
        CourseListViewHolder clh = new CourseListViewHolder(v);
        return  clh;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListViewHolder holder, int position) {
        Advertisement currentAdvertisement = advertisementList.get(position);

        holder.usernameTextView.setText(currentAdvertisement.getUsername());
        holder.advertisementTitleTextView.setText(currentAdvertisement.getTitle());
        holder.advertisementDescriptionTextView.setText(currentAdvertisement.getDescription());
    }

    @Override
    public int getItemCount() {
        return advertisementList.size();
    }
}
