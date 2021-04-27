package com.example.p4_group12.Interface;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Tag;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.jama.carouselview.CarouselScrollListener;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.squareup.picasso.Picasso;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import static java.lang.Integer.getInteger;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import static java.lang.Integer.max;

public class AdvertisementViewActivity extends NavigationActivity {
    private TextView advertisementTitle;
    private TextView advertisementDescription;
    private ChipGroup advertisementTags;
    private ImageView profilePicture;
    private Advertisement currentAdvertisement;
    private ImageButton deleteButton;
    private Button contactButton;
    private API api;
    private CarouselView carousel;
    private TextView lastUpdateDate;
    private Boolean bookmarkChecked = false;
    private int contactable;

    /*
    * All infos about the carousel implementation are here :
    * https://github.com/jama5262/CarouselView
    *
    * Another version is here but it is based on the first link and we won't use it
    * https://androidexample365.com/a-super-simple-and-customizable-image-carousel-view-for-android/
    */

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_advertisement_view, contentFrameLayout);

        currentAdvertisement = (Advertisement) getIntent().getSerializableExtra("ClickedAdvertisement");
        int n = (int) getIntent().getSerializableExtra("Number of tags");
        contactable = (int) getIntent().getSerializableExtra("contactable"); // to know if we have to show the button "contacter". 1 = to show and 0 = not show
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            tags.add((Tag) getIntent().getSerializableExtra("tag"+i));
        }
        currentAdvertisement.setTags(tags);
        if(currentAdvertisement == null) Log.d("NULLWARNING", "Course is null in AdvertisementListActivity");

        api = API.getInstance();

        //Il faut get le user proprietaire de l'annonce et set les variables ci-dessous
        try{
            User onlyUser = api.getUserWithEmail(currentAdvertisement.getEmailAddress());
            setTitleToolbar(onlyUser.getName());
            profilePicture = findViewById(R.id.profile_picture);
            profilePicture.setVisibility(View.VISIBLE);
            if (onlyUser.getPicture() != "null") {
                Picasso.get().load(BuildConfig.STORAGE_URL + onlyUser.getPicture()).transform(new CropCircleTransformation()).into(profilePicture);
            }
        } catch (UnknownHostException e){
            AdvertisementViewActivity.this.finish();
            Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
        }



        advertisementTitle = findViewById(R.id.advertisement_title_view);
        advertisementDescription = findViewById(R.id.advertisement_description_view);
        advertisementTags = findViewById(R.id.advertisement_tags_view);
        lastUpdateDate = findViewById(R.id.advertisement_view_last_update_date_text_view);
        advertisementTitle.setText(currentAdvertisement.getTitle());
        advertisementDescription.setText(currentAdvertisement.getDescription());
        lastUpdateDate.setText("Dernière modification le "+ DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM, DateFormat.SHORT, Locale.FRANCE).format(currentAdvertisement.getLastUpdateDate()));

        for (Tag tag : currentAdvertisement.getTags()) {
            Chip chip = new Chip(this);
            chip.setText(tag.getTagValue());
            chip.setCheckable(false);
            advertisementTags.addView(chip);
        }

        contactButton = findViewById(R.id.contactAdvertiserButton);
        if (GlobalVariables.getUser().getEmail().equals(currentAdvertisement.getEmailAddress())) {
            contactButton.setText(R.string.updateAdvertisement);
            contactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent updateAdvertisement = new Intent(getApplicationContext(), EditAdvertisementActivity.class);
                    updateAdvertisement.putExtra("toEditAdvertisement", currentAdvertisement);
                    updateAdvertisement.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(updateAdvertisement,2);
                }
            });
            //contactButton.setVisibility(View.GONE);
        } else {
            if (contactable == 0) contactButton.setVisibility(View.INVISIBLE);
            contactButton.setText(R.string.contacter_l_annonceur);
            contactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent foreignProfile = new Intent(getApplicationContext(),  ForeignProfileActivity.class);
                    foreignProfile.putExtra("ForeignUser", currentAdvertisement.getEmailAddress());
                    Log.v("jerem", "Foreign : "+currentAdvertisement.getEmailAddress());
                    foreignProfile.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(foreignProfile);
                }
            });
        }

        /* List of 4 images for development purpose
        List<String> devImages = new ArrayList<>();
        devImages.add("advertisements/ArBqYBaleyuw0aCD1SEtwlBrKKSXI58zw88HOE0m.jpg");
        devImages.add("advertisements/ArBqYBaleyuw0aCD1SEtwlBrKKSXI58zw88HOE0m.jpg");
        devImages.add("advertisements/ArBqYBaleyuw0aCD1SEtwlBrKKSXI58zw88HOE0m.jpg");
        devImages.add("advertisements/ArBqYBaleyuw0aCD1SEtwlBrKKSXI58zw88HOE0m.jpg");
        currentAdvertisement.setImages(devImages);
        */
        try {
            currentAdvertisement.setImages(api.getAdvertisementPictures(currentAdvertisement.getID()));
            carousel = findViewById(R.id.advertisement_view_carousel);
            if (currentAdvertisement.hasImages()) {
                // As long as we don't find a way to stop autoplay when the user swipe the carousel the user experience is better without autoplay
                carousel.setAutoPlay(false);
                carousel.setSize(currentAdvertisement.getImages().size());
                carousel.setCarouselViewListener(new CarouselViewListener() {
                    @Override
                    public void onBindView(View view, int position) {
                        ImageView imageView = view.findViewById(R.id.carousel_item_imageView);
                        Picasso.get().load(BuildConfig.STORAGE_URL + currentAdvertisement.getImages().get(position)).into(imageView);
                    }
                });
                carousel.show();
            } else {
                carousel.setVisibility(View.GONE);
            }
        }catch (UnknownHostException e){
            Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem register = menu.findItem(R.id.action_delete);
        if(GlobalVariables.getUser().getEmail().equals(currentAdvertisement.getEmailAddress())) {
            register.setVisible(true);
        }
        MenuItem bookmark = menu.findItem(R.id.action_bookmark);
        try {
            if(api.getBookmarksIdsOfUser(GlobalVariables.getUser()).contains(currentAdvertisement.getID())) {
                bookmark.setIcon(R.drawable.ic_baseline_bookmark_selected);
                bookmarkChecked = true;
            }
        } catch (UnknownHostException e) {
            Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.advertisement_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirmation");
                builder.setMessage("Etes vous sûr de vouloir supprimer cette annonce ?");
                builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Intent intent = new Intent();
                            api.deleteAdvertisment(currentAdvertisement);
                            setResult(1, intent);
                            finish();
                            dialog.dismiss();
                        }catch (UnknownHostException e){
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.action_bookmark:
                if (bookmarkChecked){
                    api.removeBookmarkForUser(GlobalVariables.getUser(), currentAdvertisement);
                    item.setIcon(R.drawable.ic_baseline_bookmark_not_selected);
                    bookmarkChecked = false;
                    Intent intent = new Intent();
                    setResult(1, intent);
                }else{
                    api.addBookmarkForUser(GlobalVariables.getUser(), currentAdvertisement);
                    item.setIcon(R.drawable.ic_baseline_bookmark_selected);
                    bookmarkChecked = true;
                    Intent intent = new Intent();
                    setResult(1, intent);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 1
        if(resultCode == 1){
            Intent advertisementList = new Intent(getApplicationContext(), AdvertisementViewActivity.class);
            advertisementList.putExtra("ClickedAdvertisement", currentAdvertisement);
            startActivity(advertisementList);
            finish();
        }
        if(resultCode == 2) {
            Intent advertisementView = new Intent(getApplicationContext(), AdvertisementViewActivity.class);
            Advertisement ad = (Advertisement) data.getSerializableExtra("Advertisement");
            int n = (int) data.getSerializableExtra("Number of tags");
            List<Tag> tags = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                tags.add((Tag) data.getSerializableExtra("tag"+i));
            }
            advertisementView.putExtra("ClickedAdvertisement", ad);
            int i = 0;
            for (Tag tag : tags) {
                advertisementView.putExtra("tag"+i, tag);
                i++;
            }
            advertisementView.putExtra("Number of tags", i);
            advertisementView.putExtra("contactable", contactable);
            startActivity(advertisementView);
            finish();
        }
    }
}
