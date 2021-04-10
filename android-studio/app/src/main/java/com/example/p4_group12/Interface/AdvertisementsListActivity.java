package com.example.p4_group12.Interface;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.Interface.adapter.AdvertisementListAdapter;
import com.example.p4_group12.R;

import com.example.p4_group12.database.API;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import android.util.Log;

public class AdvertisementsListActivity extends NavigationActivity {
    private RecyclerView advertisementRecyclerView;
    private RecyclerView.LayoutManager advertisementLayoutManager;
    private AdvertisementListAdapter advertisementListAdapter;
    private TextView mTextView;
    private FloatingActionButton newAdvertisementButton;
    private API api;


    private TextView noAdvertisment;
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
        ArrayList<Advertisement> advertisementsList = api.getCourseAdvertisements(currentCourse);

        mTextView = (TextView) findViewById(R.id.text);
        noAdvertisment = findViewById(R.id.no_advertisements);
        if(advertisementsList.size()==0){
            noAdvertisment.setVisibility(View.VISIBLE);
        }
        advertisementRecyclerView = findViewById(R.id.advertisementRecyclerView);
        advertisementRecyclerView.setHasFixedSize(true);
        advertisementLayoutManager = new LinearLayoutManager(this);
        // advertisementListAdapter = new AdvertisementListAdapter(this.get_advertisements());
        advertisementListAdapter = new AdvertisementListAdapter(advertisementsList);

        advertisementRecyclerView.setLayoutManager(advertisementLayoutManager);
        advertisementRecyclerView.setAdapter(advertisementListAdapter);


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

        advertisementListAdapter.setAdvertisementClickListener(new AdvertisementListAdapter.OnAdvertisementClickListener() {
            @Override
            public void OnAdvertisementClick(int position) {
                Advertisement clickedAdvertisement = advertisementsList.get(position);
                Intent advertisementView = new Intent(getApplicationContext(), AdvertisementViewActivity.class);
                advertisementView.putExtra("ClickedAdvertisement", clickedAdvertisement);
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
}