package com.example.p4_group12.Interface;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.R;

public class AdvertisementView extends AppCompatActivity {
    private TextView advertisementTitle;
    private TextView advertisementOwner;
    private TextView advertisementDescription;
    private Advertisement currentAdvertisement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement_view);

        currentAdvertisement = (Advertisement) getIntent().getSerializableExtra("ClickedAdvertisement");
        if(currentAdvertisement == null) Log.d("NULLWARNING", "Course is null in AdvertisementListActivity");
        setTitle("Advertisement");

        advertisementTitle = findViewById(R.id.advertisement_title_view);
        advertisementOwner = findViewById(R.id.advertisement_owner_view);
        advertisementDescription = findViewById(R.id.advertisement_description_view);

        advertisementTitle.setText(currentAdvertisement.getTitle());
        advertisementOwner.setText(currentAdvertisement.getUsername());
        advertisementDescription.setText(currentAdvertisement.getDescription());
    }
}
