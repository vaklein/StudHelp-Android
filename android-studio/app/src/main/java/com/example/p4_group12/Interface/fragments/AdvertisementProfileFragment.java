package com.example.p4_group12.Interface.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Tag;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.Interface.AdvertisementViewActivity;
import com.example.p4_group12.Interface.GlobalVariables;
import com.example.p4_group12.Interface.adapter.AdvertisementListAdapter;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;

public class AdvertisementProfileFragment extends Fragment {
    private TextView advertisement;
    private RecyclerView advertisementRecyclerView;
    private RecyclerView.LayoutManager advertisementLayoutManager;
    private AdvertisementListAdapter advertisementListAdapter;
    private ArrayList<Advertisement> advertisementsListComplete;
    private ArrayList<Advertisement> advertisementsListToShow;
    private String emailValue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_advertisement, container, false);

        try {
            API api = API.getInstance();
            Log.v("jerem", "frag foreign 1 : ");
            emailValue = this.getArguments().getString("email");
            Log.v("jerem", "frag foreign : " + emailValue);

            User user = api.getUserWithEmail(emailValue);
            advertisementsListComplete = api.getAdvertisementsOfUser(user);

            HashSet<Integer> bookmarksIDs = api.getBookmarksIdsOfUser(GlobalVariables.getUser());

            advertisement = result.findViewById(R.id.no_advertisements_frag);

            advertisementsListToShow = (ArrayList<Advertisement>) advertisementsListComplete.clone();
            if (!emailValue.equals(GlobalVariables.getUser().getEmail()))
                advertisement.setText(R.string.no_advertisment);
            if (advertisementsListComplete.isEmpty()) advertisement.setVisibility(View.VISIBLE);

            advertisementRecyclerView = result.findViewById(R.id.advertisementRecyclerView);
            advertisementLayoutManager = new LinearLayoutManager(getActivity());
            advertisementRecyclerView.setLayoutManager(advertisementLayoutManager);
            advertisementListAdapter = new AdvertisementListAdapter(advertisementsListToShow, bookmarksIDs, true);
            advertisementRecyclerView.setAdapter(advertisementListAdapter);
            advertisementListAdapter.setAdvertisementClickListener(new AdvertisementListAdapter.OnAdvertisementClickListener() {
                @Override
                public void OnAdvertisementClick(int position) {
                    Advertisement clickedAdvertisement = advertisementsListToShow.get(position);
                    Intent advertisementView = new Intent(getActivity(), AdvertisementViewActivity.class);
                    advertisementView.putExtra("ClickedAdvertisement", clickedAdvertisement);
                    int i = 0;
                    for (Tag tag : clickedAdvertisement.getTags()) {
                        advertisementView.putExtra("tag" + i, tag);
                        i++;
                    }
                    advertisementView.putExtra("Number of tags", i);
                    advertisementView.putExtra("contactable", 0);
                    advertisementView.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(advertisementView, 1);
                }
            });
        }catch (UnknownHostException e){
            Toast.makeText(getContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
        }
        return result;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 1
        if(requestCode == 1) {
            Fragment fragment = new AdvertisementProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putString("email", emailValue);
            fragment.setArguments(bundle);
            Log.v("jerem", "frag test refresh :" + emailValue);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }
    }
}