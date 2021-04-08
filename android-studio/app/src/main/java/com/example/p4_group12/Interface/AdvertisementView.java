package com.example.p4_group12.Interface;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.example.p4_group12.database.DatabaseContact;
import com.example.p4_group12.database.GetObjectFromDB;

import java.util.ArrayList;

public class AdvertisementView extends NavigationActivity {
    private TextView advertisementTitle;
    private TextView advertisementOwner;
    private TextView advertisementDescription;
    private TextView advertisementType;
    private ImageView profilePicture;
    private Advertisement currentAdvertisement;
    private ImageButton deleteButton;
    private Button contactButton;
    private API api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_advertisement_view, contentFrameLayout);

        currentAdvertisement = (Advertisement) getIntent().getSerializableExtra("ClickedAdvertisement");
        if(currentAdvertisement == null) Log.d("NULLWARNING", "Course is null in AdvertisementListActivity");
        setTitleToolbar("");

        api = API.getInstance();

        //Il faut get le user proprietaire de l'annonce et set les variables ci-dessous
        profilePicture = findViewById(R.id.profile_picture);
        profilePicture.setVisibility(View.VISIBLE);
        User onlyUser = api.getUserWithEmail(currentAdvertisement.getEmailAddress());
        setTitleToolbar(onlyUser.getName());

        advertisementTitle = findViewById(R.id.advertisement_title_view);
        advertisementDescription = findViewById(R.id.advertisement_description_view);
        advertisementType = findViewById(R.id.advertisement_type_view);
        advertisementTitle.setText(currentAdvertisement.getTitle());
        advertisementDescription.setText(currentAdvertisement.getDescription());
        advertisementType.setText(currentAdvertisement.getType());

        contactButton = findViewById(R.id.contactAdvertiserButton);
        Log.v("Jules", "The current ad is " + String.valueOf(currentAdvertisement));
        Log.v("Jules", "The email of this ad is " + String.valueOf(currentAdvertisement.getEmailAddress()));
        if (GlobalVariables.getUser().getEmail().equals(currentAdvertisement.getEmailAddress())) {
            Log.v("Jules", "There should be no button as the user is the owner");
            contactButton.setVisibility(View.GONE);
        } else {
            Log.v("Jules", "The button must be there the user is not the owner");
            contactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent foreignProfile = new Intent(getApplicationContext(), ForeignProfileActivity.class);
                    foreignProfile.putExtra("ForeignUser", currentAdvertisement.getEmailAddress());
                    startActivity(foreignProfile);
                }
            });
        }

        // delete advertisement
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Etes vous sûr de vouloir supprimer cette annonce ?");
        builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                api.deleteAdvertisment(currentAdvertisement);
                setResult(1, intent);
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
        if(GlobalVariables.getUser().getEmail().equals(currentAdvertisement.getEmailAddress())){
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
