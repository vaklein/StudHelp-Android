package com.example.p4_group12.Interface.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


// Followed https://www.youtube.com/watch?v=17NbUcEts9c for the code and xml layout
public class AdvertisementListAdapter extends RecyclerView.Adapter<AdvertisementListAdapter.AdvertisementListViewHolder> {

    private ArrayList<Advertisement> advertisementList;
    private OnAdvertisementClickListener advertisementClickListener;
    private final HashMap<String, User> usersMemory = new HashMap<>();

    public interface OnAdvertisementClickListener {
        void OnAdvertisementClick(int position);
    }

    public void setAdvertisementClickListener(OnAdvertisementClickListener advertisementClickListener) {
        this.advertisementClickListener = advertisementClickListener;
    }

    public static class AdvertisementListViewHolder extends RecyclerView.ViewHolder {

        // Here goes the elements of each item of the recyclerview item
        private TextView usernameTextView;
        private TextView advertisementTitleTextView;
        private TextView advertisementDescriptionTextView;
        private TextView advertisementDateTextView;


        public AdvertisementListViewHolder(@NonNull View itemView, OnAdvertisementClickListener advertisementClickListener) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username);
            advertisementTitleTextView = itemView.findViewById(R.id.advertisement_title_view);
            advertisementDescriptionTextView = itemView.findViewById(R.id.advertisement_description_recycler);
            advertisementDateTextView = itemView.findViewById(R.id.advertisement_item_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (advertisementClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            advertisementClickListener.OnAdvertisementClick(position);
                        }
                    }
                }
            });
        }
    }

    public AdvertisementListAdapter(ArrayList<Advertisement> advertisementList){
        this.advertisementList = advertisementList;
    }

    @NonNull
    @Override
    public AdvertisementListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Log.v("Gwen", "test");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.advertisement_item, parent, false);
        AdvertisementListViewHolder clh = new AdvertisementListViewHolder(v, this.advertisementClickListener);
        return  clh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdvertisementListViewHolder holder, int position) {
        Advertisement currentAdvertisement = advertisementList.get(position);

        User user = usersMemory.getOrDefault(currentAdvertisement.getEmailAddress(), null);
        if (user == null) {
            Log.v("Jules", "User email is : " + currentAdvertisement.getEmailAddress());
            user = API.getInstance().getUserWithEmail(currentAdvertisement.getEmailAddress());
            if (user == null) {
                Log.v("Jules" , "Problem ID is " + currentAdvertisement.getID());
            }
            assert user != null;
            usersMemory.put(user.getEmail(), user);
        }

        holder.usernameTextView.setText(user.getName());
        holder.advertisementTitleTextView.setText(currentAdvertisement.getTitle());
        holder.advertisementDescriptionTextView.setText(currentAdvertisement.getDescription());
        Log.v("Dateee", DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE).format(currentAdvertisement.getCreationDate()));
        holder.advertisementDateTextView.setText("Créée le " + DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.FRANCE).format(currentAdvertisement.getCreationDate()));
    }

    @Override
    public int getItemCount() {
        return advertisementList.size();
    }
}
