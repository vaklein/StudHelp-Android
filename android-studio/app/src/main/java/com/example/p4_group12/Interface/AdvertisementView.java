package com.example.p4_group12.Interface;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.R;
import com.example.p4_group12.database.DatabaseContact;

public class AdvertisementView extends NavigationActivity {
    private TextView advertisementTitle;
    private TextView advertisementOwner;
    private TextView advertisementDescription;
    private TextView advertisementType;
    private Advertisement currentAdvertisement;
    private ImageButton deleteButton;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Etes vous sûr de vouloir supprimer cette annonce ?");
        builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DatabaseContact.delete_advertisement(currentAdvertisement.getID());
                Intent myadvertisementList = new Intent(getApplicationContext(), MyAdvertisementsActivity.class);
                startActivity(myadvertisementList);
                finish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NON", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        deleteButton = findViewById(R.id.delete_button);
        if(GlobalVariables.getEmail().equals(currentAdvertisement.getUsername())){
            deleteButton.setVisibility(View.VISIBLE);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }


}
