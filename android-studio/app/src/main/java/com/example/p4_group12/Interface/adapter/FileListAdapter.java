package com.example.p4_group12.Interface.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.File;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.Interface.GlobalVariables;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;


// Followed https://www.youtube.com/watch?v=17NbUcEts9c for the code and xml layout
public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileListViewHolder> {

    private ArrayList<File> fileList;
    private OnFileClickListener fileClickListener;
    private final HashMap<String, User> usersMemory = new HashMap<>();

    public interface OnFileClickListener {
        void OnFileClick(int position);
    }

    public void setFileClickListener(OnFileClickListener fileClickListener) {
        this.fileClickListener = fileClickListener;
    }

    public static class FileListViewHolder extends RecyclerView.ViewHolder {

        // Here goes the elements of each item of the recyclerview item
        private TextView usernameTextView;
        private TextView fileTitleTextView;
        private TextView fileDateTextView;
        private ImageButton shareButton;
        private API api;


        public FileListViewHolder(@NonNull View itemView, OnFileClickListener fileClickListener) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username);
            fileTitleTextView = itemView.findViewById(R.id.file_title_view);
            fileDateTextView = itemView.findViewById(R.id.file_item_date);
            shareButton = itemView.findViewById(R.id.shareButton);
            api = API.getInstance();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fileClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            fileClickListener.OnFileClick(position);
                        }
                    }
                }
            });
        }
    }

    public FileListAdapter(ArrayList<File> fileList) {
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public FileListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Log.v("Gwen", "test");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);
        FileListViewHolder clh = new FileListViewHolder(v, this.fileClickListener);
        return  clh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FileListViewHolder holder, int position) {
        File currentFile = fileList.get(position);


        try {
            holder.usernameTextView.setText(API.getInstance().getUserWithEmail(currentFile.getEmail()).getName());
        } catch(UnknownHostException e){
            Toast.makeText(holder.itemView.getContext(), R.string.no_connection, Toast.LENGTH_LONG);
            return; // TODO smth better
        }

        holder.fileTitleTextView.setText(currentFile.getTitle());

        Date now = new Date();
        long timeDiff = now.getTime() - currentFile.getCreated_at().getTime();
        long oneHour = 3600000;
        Log.v("Jules", "timediff is : " + timeDiff);
        if (timeDiff < oneHour) { // Less than an hour
            int minutes = (int) (timeDiff/60000);
            if (minutes == 0) {
                holder.fileDateTextView.setText("À l'instant");
            } else if (minutes == 1) {
                holder.fileDateTextView.setText("Il y a " + minutes + " minute");
            } else {
                holder.fileDateTextView.setText("Il y a " + minutes + " minutes");
            }
        } else if (timeDiff < oneHour*24) {
            int hours = (int) (timeDiff/oneHour);
            if (hours == 1) {
                holder.fileDateTextView.setText("Il y a " + hours + " heure");
            } else {
                holder.fileDateTextView.setText("Il y a " + hours + " heures");
            }
        } else {
            holder.fileDateTextView.setText("Le " + DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.FRANCE).format(currentFile.getCreated_at()));
        }

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }
}
