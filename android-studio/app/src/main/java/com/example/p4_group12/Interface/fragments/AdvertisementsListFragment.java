package com.example.p4_group12.Interface.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
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
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.Tag;
import com.example.p4_group12.Interface.AddAdvertisementActivity;
import com.example.p4_group12.Interface.AddFileActivity;
import com.example.p4_group12.Interface.AdvertisementViewActivity;
import com.example.p4_group12.Interface.AdvertisementsListActivity;
import com.example.p4_group12.Interface.GlobalVariables;
import com.example.p4_group12.Interface.adapter.AdvertisementListAdapter;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AdvertisementsListFragment extends Fragment {
    private RecyclerView advertisementRecyclerView;
    private RecyclerView.LayoutManager advertisementLayoutManager;
    private AdvertisementListAdapter advertisementListAdapter;
    private ArrayList<Advertisement> advertisementsListComplete;
    private ArrayList<Advertisement> advertisementsListToShow;
    private HashSet<Integer> bookmarksIds;
    private TextView courseCode;
    private TextView courseFac;
    private ChipGroup filters;
    private final List<Chip> filterChips = new ArrayList<>();
    private TextView mTextView;
    private FloatingActionButton newAdvertisementButton;
    private API api;
    private TextView noAdvertisement;
    private int course_id;
    private String fac;
    private String code;
    private Course currentCourse;
    private FloatingActionButton mMainAddFab, mAddAdvertisementFab, mAddFileFab;
    private TextView mAddAdvertisementText, mAddFileText;
    private Animation mFabOpenAnim;
    private Animation mFabCloseAnim;

    private boolean isOpen;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_advertisements_list, container, false);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) result.findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_course_advertisments_list, contentFrameLayout);

        api = API.getInstance();
        if (api == null) Log.v("Jules", "API is null in AdvertisementListActivity");
        Log.v("AdvertisementLoading", "start");
        course_id = this.getArguments().getInt("course_id");

        try {
            advertisementsListComplete = api.getCourseAdvertisements(course_id);
            Log.v("AdvertisementLoading", "finish");
            advertisementsListToShow = (ArrayList<Advertisement>) advertisementsListComplete.clone();
            bookmarksIds = api.getBookmarksIdsOfUser(GlobalVariables.getUser());

            filters = result.findViewById(R.id.advertisement_list_filter_chip_group);
            courseCode = result.findViewById(R.id.advertisement_course_card_view_code);
            courseFac = result.findViewById(R.id.advertisement_course_card_view_fac);
            mTextView = (TextView) result.findViewById(R.id.text);
            noAdvertisement = result.findViewById(R.id.no_advertisements);
            if (advertisementsListToShow.size() == 0) {
                noAdvertisement.setVisibility(View.VISIBLE);
                result.findViewById(R.id.advertisement_list_filter_title).setVisibility(View.GONE);
                filters.setVisibility(View.GONE);
            }
            advertisementRecyclerView = result.findViewById(R.id.advertisementRecyclerView);
            //advertisementRecyclerView.setHasFixedSize(true);
            advertisementLayoutManager = new LinearLayoutManager(getContext());
            advertisementRecyclerView.setLayoutManager(advertisementLayoutManager);
            advertisementListAdapter = new AdvertisementListAdapter(advertisementsListToShow, bookmarksIds);
            advertisementRecyclerView.setAdapter(advertisementListAdapter);

            // Gestion des champs textes affichés
            code = this.getArguments().getString("code");
            fac = this.getArguments().getString("fac");
            courseCode.setText(code);
            courseFac.setText(fac);

            // Gestion des filtres de recherche
            for (String type : Tag.getAllTagsName()) {
                Chip chip = new Chip(getContext());
                chip.setText(type);
                chip.setCheckable(true);
                chip.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        Log.v("Jules", "checked chip is " + chip.getText().toString());
                        List<Integer> checkedChipIds = filters.getCheckedChipIds();
                        Log.v("Jules", "Number of checked chips : " + checkedChipIds.size());
                        if (checkedChipIds.isEmpty()) {
                            advertisementsListToShow.clear();
                            advertisementsListToShow.addAll(advertisementsListComplete);
                            advertisementListAdapter.notifyDataSetChanged();
                            if (!advertisementsListComplete.isEmpty()) {
                                noAdvertisement.setVisibility(View.GONE);
                            }
                            return;
                        }
                        List<String> checkedChipStrings = new ArrayList<>();
                        for (int i : checkedChipIds) {
                            checkedChipStrings.add((String) ((Chip) filters.findViewById(i)).getText());
                        }
                        Log.v("Jules", "LIST OF CHECKED CHIPS :  " + checkedChipStrings.toString());
                        advertisementsListToShow.clear();
                        advertisementsListToShow.addAll(filterListOnCheckedChips(advertisementsListComplete, checkedChipStrings));
                        Log.v("Jules", "Advertisements titles to show : " + advertisementsListToShow.toString());
                        advertisementListAdapter.notifyDataSetChanged();
                        if (advertisementsListToShow.size() == 0) {
                            noAdvertisement.setVisibility(View.VISIBLE);
                            noAdvertisement.setText("Aucune annonce ne correspond à votre recherche");
                        } else {
                            noAdvertisement.setVisibility(View.GONE);
                        }
                    }
                });
                filterChips.add(chip);
                filters.addView(chip);
            }


            // Gestion du clic sur une annonce du recyclerview
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
                    advertisementView.putExtra("contactable", 1);
                    advertisementView.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(advertisementView, 1);
                }
            });
        } catch (UnknownHostException e){
            Toast.makeText(getContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 1
        if(requestCode == 1) {
            Fragment fragment = new AdvertisementsListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("course_id", course_id);
            bundle.putString("code", code);
            bundle.putString("fac", fac);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }
    }

    private List<Advertisement> filterListOnCheckedChips(List<Advertisement> ads, List<String> checkedChipStrings) {
        List<Advertisement> filteredList = new ArrayList<>();
        for (Advertisement ad : ads) {
            boolean addAd = true;
            for (String chipTag : checkedChipStrings) {
                if (!ad.getTagValues().contains(chipTag)) {
                    addAd = false;
                    break;
                }
            }
            if (addAd) {
                filteredList.add(ad);
            }
        }
        return filteredList;
    }
}
