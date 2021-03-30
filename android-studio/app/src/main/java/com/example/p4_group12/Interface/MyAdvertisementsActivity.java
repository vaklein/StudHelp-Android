package com.example.p4_group12.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.Interface.adapter.AdvertisementListAdapter;
import com.example.p4_group12.R;
import com.example.p4_group12.database.GetObjectFromDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyAdvertisementsActivity extends NavigationActivity{
    private RecyclerView advertisementRecyclerView;
    private RecyclerView.LayoutManager advertisementLayoutManager;
    private AdvertisementListAdapter advertisementListAdapter;
    private TextView mTextView;
    private FloatingActionButton newAdvertisementButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_advertisments_list, contentFrameLayout);
        setTitleToolbar("Mes annonces");

        ArrayList<Advertisement> advertisementsList = new ArrayList<Advertisement>();
        GetObjectFromDB query = new GetObjectFromDB(advertisementsList, Advertisement .class);


        // doing the query
        GetObjectFromDB.getJSON("https://db.valentinklein.eu:8182/get_all_my_advertisements.php?UserEmail="+GlobalVariables.getEmail(), advertisementsList, Advertisement.class);

        mTextView = (TextView) findViewById(R.id.text);

        advertisementRecyclerView = findViewById(R.id.advertisementRecyclerView);
        advertisementRecyclerView.setHasFixedSize(true);
        advertisementLayoutManager = new LinearLayoutManager(this);
        // advertisementListAdapter = new AdvertisementListAdapter(this.get_advertisements());
        advertisementListAdapter = new AdvertisementListAdapter(advertisementsList);

        advertisementRecyclerView.setLayoutManager(advertisementLayoutManager);
        advertisementRecyclerView.setAdapter(advertisementListAdapter);
        advertisementListAdapter.setAdvertisementClickListener(new AdvertisementListAdapter.OnAdvertisementClickListener() {
            @Override
            public void OnAdvertisementClick(int position) {
                Advertisement clickedAdvertisement = advertisementsList.get(position);
                Intent advertisementView = new Intent(getApplicationContext(), AdvertisementView.class);
                advertisementView.putExtra("ClickedAdvertisement", clickedAdvertisement);
                startActivityForResult(advertisementView, 1);
            }
        });

        //Can't add new advertisements in this section -> invisible
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
