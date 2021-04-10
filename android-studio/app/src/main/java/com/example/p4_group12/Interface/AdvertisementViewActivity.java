package com.example.p4_group12.Interface;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.R;
import com.example.p4_group12.database.DatabaseContact;
import com.example.p4_group12.database.GetObjectFromDB;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.jama.carouselview.CarouselScrollListener;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.max;

public class AdvertisementViewActivity extends NavigationActivity {
    private TextView advertisementTitle;
    private TextView advertisementDescription;
    private ChipGroup advertisementTags;
    private ImageView profilePicture;
    private Advertisement currentAdvertisement;
    private ImageButton deleteButton;
    private Button contactButton;
    private CarouselView carousel;

    /*
    * All infos about the carousel implementation are here :
    * https://github.com/jama5262/CarouselView
    *
    * Another version is here but it is based on the first link and we won't use it
    * https://androidexample365.com/a-super-simple-and-customizable-image-carousel-view-for-android/
    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_advertisement_view, contentFrameLayout);

        currentAdvertisement = (Advertisement) getIntent().getSerializableExtra("ClickedAdvertisement");
        if(currentAdvertisement == null) Log.d("NULLWARNING", "Course is null in AdvertisementListActivity");
        setTitleToolbar("");

        //Il faut get le user proprietaire de l'annonce et set les variables ci-dessous
        profilePicture = findViewById(R.id.profile_picture);
        profilePicture.setVisibility(View.VISIBLE);
        ArrayList<User> onlyUser = new ArrayList<>();
        GetObjectFromDB.getJSON(BuildConfig.DB_URL + "get_user_from_email.php?UserEmail="+currentAdvertisement.getEmailAddress(), onlyUser, User.class);
        setTitleToolbar(onlyUser.get(0).getName());

        advertisementTitle = findViewById(R.id.advertisement_title_view);
        advertisementDescription = findViewById(R.id.advertisement_description_view);
        advertisementTags = findViewById(R.id.advertisement_tags_view);
        advertisementTitle.setText(currentAdvertisement.getTitle());
        advertisementDescription.setText(currentAdvertisement.getDescription());

        for (String type : currentAdvertisement.getTags()) {
            Chip chip = new Chip(this);
            chip.setText(type);
            chip.setCheckable(false);
            advertisementTags.addView(chip);
        }

        contactButton = findViewById(R.id.contactAdvertiserButton);
        Log.v("Jules", "The current ad is " + String.valueOf(currentAdvertisement));
        Log.v("Jules", "The email of this ad is " + String.valueOf(currentAdvertisement.getEmailAddress()));
        if (GlobalVariables.getEmail().equals(currentAdvertisement.getEmailAddress())) {
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
                DatabaseContact.delete_advertisement(currentAdvertisement.getID());
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
        if(GlobalVariables.getEmail().equals(currentAdvertisement.getEmailAddress())){
            deleteButton.setVisibility(View.VISIBLE);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        carousel = findViewById(R.id.advertisement_view_carousel);

        if (currentAdvertisement.hasImages()) {
            carousel.setSize(currentAdvertisement.getImages().size());
            carousel.setCarouselViewListener(new CarouselViewListener() {
                @Override
                public void onBindView(View view, int position) {
                    ImageView imageView = view.findViewById(R.id.carousel_item_imageView);
                    Picasso.get().load(currentAdvertisement.getImages().get(position)).into(imageView);
                }
            });
            carousel.show();
        } else {
            carousel.setVisibility(View.GONE);
        }
    }

}
