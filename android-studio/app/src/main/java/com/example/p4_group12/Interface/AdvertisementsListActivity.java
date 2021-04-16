package com.example.p4_group12.Interface;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.Tag;
import com.example.p4_group12.Interface.adapter.AdvertisementListAdapter;
import com.example.p4_group12.R;

import com.example.p4_group12.database.API;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import static java.util.Collections.sort;

public class AdvertisementsListActivity extends NavigationActivity {
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
    private API api;


    private TextView noAdvertisement;
    private Course currentCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_advertisments_list, contentFrameLayout);

        currentCourse = (Course) getIntent().getSerializableExtra("ClickedCourse");
        if(currentCourse == null) Log.d("NULLWARNING", "Course is null in AdvertisementListActivity");
        setTitleToolbar(currentCourse.getName());

        api = API.getInstance();
        if (api == null) Log.v("Jules", "API is : null" );
        advertisementsListComplete = api.getCourseAdvertisements(currentCourse);
        advertisementsListToShow = (ArrayList<Advertisement>) advertisementsListComplete.clone();

        filters = findViewById(R.id.advertisement_list_filter_chip_group);
        courseCode = findViewById(R.id.advertisement_course_card_view_code);
        courseFac = findViewById(R.id.advertisement_course_card_view_fac);
        mTextView = (TextView) findViewById(R.id.text);
        noAdvertisement = findViewById(R.id.no_advertisements);
        if(advertisementsListToShow.size()==0){
            noAdvertisement.setVisibility(View.VISIBLE);
        }
        advertisementRecyclerView = findViewById(R.id.advertisementRecyclerView);
        //advertisementRecyclerView.setHasFixedSize(true);
        advertisementLayoutManager = new LinearLayoutManager(this);
        advertisementRecyclerView.setLayoutManager(advertisementLayoutManager);
        advertisementListAdapter = new AdvertisementListAdapter(advertisementsListToShow);
        advertisementRecyclerView.setAdapter(advertisementListAdapter);

        // Gestion des champs textes affichés
        courseCode.setText(currentCourse.getCode());
        courseFac.setText(currentCourse.getFaculty());

        // Gestion des filtres de recherche
        for (String type : Tag.getAllTagsName()) {
            Chip chip = new Chip(this);
            chip.setText(type);
            chip.setCheckable(true);
            chip.setOnClickListener(new View.OnClickListener() {
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
                }
            });
            filterChips.add(chip);
            filters.addView(chip);
        }
        Log.v("Jules", "Advertisement list activity");

        // Gestion du bouton pour créer une nouvelle annonce
        newAdvertisementButton = findViewById(R.id.new_advertisement_button);
        newAdvertisementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplication().getBaseContext(), clickedCourse.getName(), Toast.LENGTH_LONG).show();
                Intent newAdvertisement = new Intent(getApplicationContext(), AddAdvertisementActivity.class);
                newAdvertisement.putExtra("CurrentCourse", currentCourse);
                startActivityForResult(newAdvertisement, 1); // 1 for finish
                //startActivity(newAdvertisement);
            }
        });

        // Gestion du clique sur une annonce du recyclerview
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
                startActivityForResult(advertisementView, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 1
        if(requestCode == 1) {
            Intent advertisementList = new Intent(getApplicationContext(), AdvertisementsListActivity.class);
            advertisementList.putExtra("ClickedCourse", currentCourse);
            startActivity(advertisementList);
            finish();
        }
    }

    private List<Advertisement> filterListOnCheckedChips(List<Advertisement> ads, List<String> checkedChipStrings) {
        List<Advertisement> filteredList = new ArrayList<>();
        for (Advertisement ad : ads) {
            for (Tag tag : ad.getTags()) {
                if (checkedChipStrings.contains(tag.getTagValue())) {
                    filteredList.add(ad);
                }
            }
        }
        return filteredList;
    }

}