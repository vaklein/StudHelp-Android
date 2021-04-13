package com.example.p4_group12.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Tag;
import com.example.p4_group12.Interface.adapter.AdvertisementListAdapter;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MyAdvertisementsActivity extends NavigationActivity{
    private RecyclerView advertisementRecyclerView;
    private RecyclerView.LayoutManager advertisementLayoutManager;
    private AdvertisementListAdapter advertisementListAdapter;
    private TextView courseCode;
    private TextView courseFac;
    private ChipGroup filters;
    private final List<Chip> filterChips = new ArrayList<>();
    private final ArrayList<String> tagNames = new ArrayList<>();
    private TextView mTextView;
    private FloatingActionButton newAdvertisementButton;
    private API api;

    public MyAdvertisementsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_advertisments_list, contentFrameLayout);
        setTitleToolbar("Mes annonces");

        api = API.getInstance();
        ArrayList<Advertisement> advertisementsList = api.getAdvertisementsOfUser(GlobalVariables.getUser());
        mTextView = (TextView) findViewById(R.id.text);
        filters = findViewById(R.id.advertisement_list_filter_chip_group);
        courseCode = findViewById(R.id.advertisement_course_card_view_code);
        courseFac = findViewById(R.id.advertisement_course_card_view_fac);
        advertisementRecyclerView = findViewById(R.id.advertisementRecyclerView);
        advertisementRecyclerView.setHasFixedSize(true);
        advertisementLayoutManager = new LinearLayoutManager(this);
        // advertisementListAdapter = new AdvertisementListAdapter(this.get_advertisements());
        advertisementListAdapter = new AdvertisementListAdapter(advertisementsList);
        advertisementRecyclerView.setLayoutManager(advertisementLayoutManager);
        advertisementRecyclerView.setAdapter(advertisementListAdapter);

        // Gestion des champs textes affichés
        courseCode.setText(R.string.myadvertisements_hint);
        courseFac.setVisibility(View.GONE);

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
        filters.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = group.findViewById(checkedId);
                if (chip != null) {
                    chip.setChecked(!chip.isChecked());
                    // TODO : ici il faut update la liste des publications affichées
                }
            }
        });

        // Gestion du clique sur une annonce
        advertisementListAdapter.setAdvertisementClickListener(new AdvertisementListAdapter.OnAdvertisementClickListener() {
            @Override
            public void OnAdvertisementClick(int position) {
                Advertisement clickedAdvertisement = advertisementsList.get(position);
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

        // Can't add new advertisements in this section -> invisible
        newAdvertisementButton = findViewById(R.id.new_advertisement_button);
        newAdvertisementButton.setVisibility(View.INVISIBLE);
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

}
