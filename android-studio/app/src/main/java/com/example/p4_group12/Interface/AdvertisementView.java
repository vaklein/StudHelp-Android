package com.example.p4_group12.Interface;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.R;

public class AdvertisementView extends NavigationActivity {
    private TextView advertisementTitle;
    private TextView advertisementOwner;
    private TextView advertisementDescription;
    private TextView advertisementType;
    private Advertisement currentAdvertisement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_advertisement_view, contentFrameLayout);

        currentAdvertisement = (Advertisement) getIntent().getSerializableExtra("ClickedAdvertisement");
        if(currentAdvertisement == null) Log.d("NULLWARNING", "Course is null in AdvertisementListActivity");
        setTitleToolbar(currentAdvertisement.getTitle());

        advertisementTitle = findViewById(R.id.advertisement_title_view);
        advertisementOwner = findViewById(R.id.advertisement_owner_view);
        advertisementDescription = findViewById(R.id.advertisement_description_view);
        advertisementType = findViewById(R.id.advertisement_type_view);

        advertisementTitle.setText(currentAdvertisement.getTitle());
        advertisementOwner.setText(currentAdvertisement.getUsername());
        advertisementDescription.setText(currentAdvertisement.getDescription());
        advertisementType.setText(currentAdvertisement.getType());
    }
}
