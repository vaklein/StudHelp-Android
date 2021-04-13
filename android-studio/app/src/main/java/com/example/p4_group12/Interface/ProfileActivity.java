package com.example.p4_group12.Interface;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Social_links;
import com.example.p4_group12.Interface.fragments.ContactsFragment;
import com.example.p4_group12.Interface.fragments.DataFragment;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ProfileActivity extends NavigationActivity implements TabLayout.OnTabSelectedListener {

    private FloatingActionButton edit;
    private TextView name;
    private ImageView picture;

    private API api;

    private TabLayout tabLayout;
    private Fragment fragment = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_profile, contentFrameLayout);
        setTitleToolbar("Profil");

        api = API.getInstance();

        tabLayout = findViewById(R.id.tabs);
        Bundle bundle = new Bundle();
        bundle.putString("login", GlobalVariables.getUser().getLogin());
        bundle.putString("email", GlobalVariables.getUser().getEmail());
        bundle.putString("description", GlobalVariables.getUser().getDescription());
        fragment = new DataFragment();
        fragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        tabLayout.addOnTabSelectedListener(this);

        if (GlobalVariables.getUser().getPicture() != "null") {
            picture = (ImageView) findViewById(R.id.user_profile_photo);
            Picasso.get().load(BuildConfig.STORAGE_URL + GlobalVariables.getUser().getPicture()).transform(new CropCircleTransformation()).into(picture);
        }
        name = (TextView) findViewById(R.id.user_profile_name);
        edit = findViewById(R.id.floating_action_button);

        name.setText(String.valueOf(GlobalVariables.getUser().getName()));
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_profil = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(edit_profil);
            }
        });


    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Bundle bundle = new Bundle();
        switch (tab.getPosition()) {
            case 0:
                bundle.putString("login", GlobalVariables.getUser().getLogin());
                bundle.putString("email", GlobalVariables.getUser().getEmail());
                bundle.putString("description", GlobalVariables.getUser().getDescription());
                fragment = new DataFragment();
                fragment.setArguments(bundle);
                break;
            case 1:
                bundle.putString("email", GlobalVariables.getUser().getEmail());
                bundle.putString("type", "user");
                fragment = new ContactsFragment();
                fragment.setArguments(bundle);
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
