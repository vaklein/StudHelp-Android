package com.example.p4_group12.Interface.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.File;
import com.example.p4_group12.DAO.Tag;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.Interface.AdvertisementViewActivity;
import com.example.p4_group12.Interface.GlobalVariables;
import com.example.p4_group12.Interface.adapter.AdvertisementListAdapter;
import com.example.p4_group12.Interface.adapter.FileListAdapter;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;

import java.util.ArrayList;
import java.util.HashSet;

public class FileFragment extends Fragment {
    private TextView advertisement;
    private RecyclerView advertisementRecyclerView;
    private RecyclerView.LayoutManager advertisementLayoutManager;
    private FileListAdapter advertisementListAdapter;
    private ArrayList<File> advertisementsListComplete;
    private int course_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_advertisement, container, false);

        API api = API.getInstance();
        course_id = this.getArguments().getInt("course_id");
        Log.v("jerem", "frag foreign : " + course_id);

        advertisement = result.findViewById(R.id.no_advertisements_frag);
        advertisementsListComplete = api.getCourseFiles(course_id);
        Log.v("vale", "fign : " + advertisementsListComplete.toString());
        if (advertisementsListComplete.isEmpty()) {
            advertisement.setVisibility(View.VISIBLE);
            advertisement.setText("Ce cours n'a pas encore de synthèse, n'hésitez pas à en ajouter!");
        }

        advertisementRecyclerView = result.findViewById(R.id.advertisementRecyclerView);
        advertisementLayoutManager = new LinearLayoutManager(getActivity());
        advertisementRecyclerView.setLayoutManager(advertisementLayoutManager);
        advertisementListAdapter = new FileListAdapter(advertisementsListComplete);
        advertisementRecyclerView.setAdapter(advertisementListAdapter);
        advertisementListAdapter.setFileClickListener(new FileListAdapter.OnFileClickListener() {
            @Override
            public void OnFileClick(int position) {
                File clickedFile = advertisementsListComplete.get(position);
                Uri path = Uri.parse(BuildConfig.STORAGE_URL + clickedFile.getFile());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        return result;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 1
        if(requestCode == 1) {
            Fragment fragment = new FileFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("course_id", course_id);
            fragment.setArguments(bundle);
            Log.v("jerem", "frag test refresh :" + course_id);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }
    }
}
