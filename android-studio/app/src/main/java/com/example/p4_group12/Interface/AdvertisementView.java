package com.example.p4_group12.Interface;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    private ImageView profilePicture;
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
        setTitleToolbar("");
        setSupportActionBar(toolbar);

        profilePicture = findViewById(R.id.profile_picture);
        profilePicture.setVisibility(View.VISIBLE);

        advertisementTitle = findViewById(R.id.advertisement_title_view);
        advertisementOwner = findViewById(R.id.advertisementOwner);
        advertisementDescription = findViewById(R.id.advertisement_description_view);
        advertisementType = findViewById(R.id.advertisement_type_view);

        advertisementTitle.setText(currentAdvertisement.getTitle());
        advertisementOwner.setVisibility(View.VISIBLE);
        advertisementOwner.setText(currentAdvertisement.getUsername());
        advertisementDescription.setText(currentAdvertisement.getDescription());
        advertisementType.setText(currentAdvertisement.getType());

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.del:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirmation");
                builder.setMessage("Etes vous sûr de vouloir supprimer cette annonce ?");
                builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
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
                builder.show();
                return true;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if(GlobalVariables.getEmail().equals(currentAdvertisement.getUsername())){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.del_ad, menu);
            return super.onCreateOptionsMenu(menu);
        }
        return false;
    }

}
