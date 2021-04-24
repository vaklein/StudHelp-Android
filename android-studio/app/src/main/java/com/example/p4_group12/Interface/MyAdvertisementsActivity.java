package com.example.p4_group12.Interface;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.Tag;
import com.example.p4_group12.Interface.adapter.AdvertisementListAdapter;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MyAdvertisementsActivity extends NavigationActivity{
    private RecyclerView advertisementRecyclerView;
    private RecyclerView.LayoutManager advertisementLayoutManager;
    private AdvertisementListAdapter advertisementListAdapter;
    private ArrayList<Advertisement> advertisementsListComplete;
    private ArrayList<Advertisement> advertisementsListToShow;
    private TextView courseCode;
    private TextView courseFac;
    private ChipGroup filters;
    private final List<Chip> filterChips = new ArrayList<>();
    private TextView mTextView;
    private FloatingActionButton newAdvertisementButton;
    private TextView noAdvertisement;
    private API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_advertisments_list, contentFrameLayout);
        setTitleToolbar("Mes annonces");

        api = API.getInstance();
        if (api == null) Log.v("Jules", "API is null in MyAdvertisementActivity");
        advertisementsListComplete = api.getAdvertisementsOfUser(GlobalVariables.getUser());
        Log.d("Gwen", Integer.toString(advertisementsListComplete.size()));
        advertisementsListToShow = (ArrayList<Advertisement>) advertisementsListComplete.clone();
        HashSet<Integer> bookmarksIds = api.getBookmarksIdsOfUser(GlobalVariables.getUser());

        filters = findViewById(R.id.advertisement_list_filter_chip_group);
        courseCode = findViewById(R.id.advertisement_course_card_view_code);
        courseFac = findViewById(R.id.advertisement_course_card_view_fac);
        mTextView = (TextView) findViewById(R.id.text);
        noAdvertisement = findViewById(R.id.no_advertisements);
        noAdvertisement.setText(R.string.no_private_advertisment);
        if (advertisementsListToShow.size() == 0) {
            noAdvertisement.setVisibility(View.VISIBLE);
            findViewById(R.id.advertisement_list_filter_title).setVisibility(View.GONE);
            filters.setVisibility(View.GONE);
        }
        advertisementRecyclerView = findViewById(R.id.advertisementRecyclerView);
        //advertisementRecyclerView.setHasFixedSize(true);
        advertisementLayoutManager = new LinearLayoutManager(this);
        advertisementRecyclerView.setLayoutManager(advertisementLayoutManager);
        advertisementListAdapter = new AdvertisementListAdapter(advertisementsListToShow, bookmarksIds, true);
        advertisementRecyclerView.setAdapter(advertisementListAdapter);

        // Gestion des champs textes affichés
        courseCode.setText(R.string.myadvertisements_hint);
        courseFac.setVisibility(View.GONE);

        // Gestion des filtres de recherche
        for (String type : Tag.getAllTagsName()) {
            Chip chip = new Chip(this);
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
                    if(advertisementsListToShow.size()==0){
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

        // Can't add new advertisements in this section -> invisible
        newAdvertisementButton = findViewById(R.id.plus_button);
        newAdvertisementButton.setVisibility(View.GONE);

        // Gestion du clic sur une annonce
        advertisementListAdapter.setAdvertisementClickListener(new AdvertisementListAdapter.OnAdvertisementClickListener() {
            @Override
            public void OnAdvertisementClick(int position) {
                Advertisement clickedAdvertisement = advertisementsListToShow.get(position);
                Intent advertisementView = new Intent(getApplicationContext(), AdvertisementViewActivity.class);
                advertisementView.putExtra("ClickedAdvertisement", clickedAdvertisement);
                int i = 0;
                for (Tag tag : clickedAdvertisement.getTags()) {
                    advertisementView.putExtra("tag"+i, tag);
                    i++;
                }
                advertisementView.putExtra("Number of tags", i);
                advertisementView.putExtra("contactable", 1);
                advertisementView.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(advertisementView, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 1
        if(requestCode == 1) {
            Intent MyadvertisementList = new Intent(getApplicationContext(), MyAdvertisementsActivity.class);
            startActivity(MyadvertisementList);
            finish();
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
