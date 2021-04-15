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
import java.util.List;

import android.util.Log;

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
    private final ArrayList<String> tagNames = new ArrayList<>();
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

        this.api = API.getInstance();
        advertisementsListComplete = api.getCourseAdvertisements(currentCourse);
        advertisementsListToShow = advertisementsListComplete;

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
        tagNames.add("Offre");
        tagNames.add("Demande");
        tagNames.add("Bachelier");
        tagNames.add("Master");
        tagNames.add("Livre/Syllabus");
        tagNames.add("Synthèse");
        tagNames.add("Aide");
        tagNames.add("Matériel");
        tagNames.add("Autres");
        for (String type : tagNames) {
            Chip chip = new Chip(this);
            chip.setText(type);
            chip.setCheckable(true);
            filterChips.add(chip);
            filters.addView(chip);
        }
        Log.v("Jules", "Advertisement list activity");
        filters.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Log.v("Jules", "CHECKED MEC");
                Chip chip = group.findViewById(checkedId);
                if (chip != null) {
                    chip.setChecked(!chip.isChecked());
                    /*
                    Log.v("Jules", "checked chip is " + chip.getText().toString());
                    List<Integer> checkedChipIds = filters.getCheckedChipIds();
                    List<String> checkedChipStrings = new ArrayList<>();
                    for (int i : checkedChipIds) {
                        checkedChipStrings.add((String) ((Chip) filters.findViewById(i)).getText());
                    }
                    Log.v("Jules", "LIST OF CHECKED CHIPS :  " + Arrays.toString(checkedChipStrings.toArray()));
                    for (Advertisement ad : advertisementsListComplete) {
                        if (!advertisementsListToShow.contains(ad) && isThereCommonTag(checkedChipStrings, ad.getTags())) {
                            advertisementsListToShow.add(ad);
                            advertisementListAdapter.notifyItemInserted(advertisementsListToShow.size()-1);
                        } else if (!advertisementsListToShow.contains(ad) && isThereCommonTag(checkedChipStrings, ad.getTags())) {
                            int i = advertisementsListToShow.indexOf(ad);
                            advertisementsListToShow.remove(ad);
                            advertisementListAdapter.notifyItemRemoved(i);
                        }
                    }
                    */
                }
            }
        });

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

    private boolean isThereCommonTag(List<String> checkedChipStrings, List<Tag> tags) {
        for (String checkedTag : checkedChipStrings) {
            for (Tag tag : tags) {
                if (tag.getTagValue().equals(checkedTag)) {
                    return true;
                }
            }
        }
        return false;
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

}