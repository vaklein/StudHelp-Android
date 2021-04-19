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

import static android.view.View.GONE;

public class MyBookmarksActivity extends NavigationActivity{

    private RecyclerView bookamrksRecyclerView;
    private RecyclerView.LayoutManager bookmarksLayoutManager;
    private AdvertisementListAdapter bookmarksListAdapter;
    private ArrayList<Advertisement> bookmarksListComplete;
    private ArrayList<Advertisement> bookmarksListToShow;

    private ChipGroup filters;
    private final List<Chip> filterChips = new ArrayList<>();

    private TextView mTextView;
    private FloatingActionButton newAdvertisementButton;
    private TextView noAdvertisement;


    private API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_advertisments_list, contentFrameLayout);
        setTitleToolbar(getResources().getString(R.string.my_bookmarks));

        api = API.getInstance();
        if(api != null){
            bookmarksListComplete = api.getBookmarksOfUser(GlobalVariables.getUser());
            bookmarksListToShow = (ArrayList<Advertisement>) bookmarksListComplete.clone();
        }

        filters = findViewById(R.id.advertisement_list_filter_chip_group);

        mTextView = (TextView) findViewById(R.id.text);
        noAdvertisement = findViewById(R.id.no_advertisements);
        if (bookmarksListToShow.size() == 0) {
            noAdvertisement.setVisibility(View.VISIBLE);
            noAdvertisement.setText(R.string.no_bookmarks);
            findViewById(R.id.advertisement_list_filter_title).setVisibility(GONE);
            filters.setVisibility(GONE);
        }

        bookamrksRecyclerView = findViewById(R.id.advertisementRecyclerView);
        bookmarksLayoutManager = new LinearLayoutManager(this);
        bookamrksRecyclerView.setLayoutManager(bookmarksLayoutManager);
        bookmarksListAdapter = new AdvertisementListAdapter(bookmarksListToShow);
        bookamrksRecyclerView.setAdapter(bookmarksListAdapter);

        // Gestion des champs textes affichés
        findViewById(R.id.advertisement_course_card_view_code).setVisibility(GONE);
        findViewById(R.id.advertisement_course_card_view_fac).setVisibility(GONE);

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
                        bookmarksListToShow.clear();
                        bookmarksListToShow.addAll(bookmarksListComplete);
                        bookmarksListAdapter.notifyDataSetChanged();
                        return;
                    }
                    List<String> checkedChipStrings = new ArrayList<>();
                    for (int i : checkedChipIds) {
                        checkedChipStrings.add((String) ((Chip) filters.findViewById(i)).getText());
                    }
                    Log.v("Jules", "LIST OF CHECKED CHIPS :  " + checkedChipStrings.toString());
                    bookmarksListToShow.clear();
                    bookmarksListToShow.addAll(filterListOnCheckedChips(bookmarksListComplete, checkedChipStrings));
                    Log.v("Jules", "Advertisements titles to show : " + bookmarksListToShow.toString());
                    bookmarksListAdapter.notifyDataSetChanged();
                    if(bookmarksListToShow.size()==0){
                        noAdvertisement.setVisibility(View.VISIBLE);
                        noAdvertisement.setText("Aucune annonce ne correspond à votre recherche");
                    } else {
                        noAdvertisement.setVisibility(GONE);
                    }
                }
            });
            filterChips.add(chip);
            filters.addView(chip);
        }

        // Can't add new advertisements in this section -> invisible
        newAdvertisementButton = findViewById(R.id.new_advertisement_button);
        newAdvertisementButton.setVisibility(View.INVISIBLE);

        // Gestion du clic sur une annonce
        bookmarksListAdapter.setAdvertisementClickListener(new AdvertisementListAdapter.OnAdvertisementClickListener() {
            @Override
            public void OnAdvertisementClick(int position) {
                Advertisement clickedAdvertisement = bookmarksListToShow.get(position);
                Intent advertisementView = new Intent(getApplicationContext(), AdvertisementViewActivity.class);
                advertisementView.putExtra("ClickedAdvertisement", clickedAdvertisement);
                int i = 0;
                for (Tag tag : clickedAdvertisement.getTags()) {
                    advertisementView.putExtra("tag"+i, tag);
                    i++;
                }
                advertisementView.putExtra("Number of tags", i);
                advertisementView.putExtra("contactable", 1);
                startActivity(advertisementView);
                //startActivityForResult(advertisementView, 1); a décommenter si on peut select/deselect le signet
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 1
        if(requestCode == 1) {
            Intent MyBookmarksActivity = new Intent(getApplicationContext(), MyBookmarksActivity.class);
            startActivity(MyBookmarksActivity);
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
