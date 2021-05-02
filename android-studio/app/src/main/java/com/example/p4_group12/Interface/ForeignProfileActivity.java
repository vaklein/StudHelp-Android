package com.example.p4_group12.Interface;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.Interface.fragments.AdvertisementProfileFragment;
import com.example.p4_group12.Interface.fragments.ContactsFragment;
import com.example.p4_group12.Interface.fragments.DataFragment;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.net.UnknownHostException;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ForeignProfileActivity extends NavigationActivity implements TabLayout.OnTabSelectedListener {

    private FloatingActionButton edit;
    private TextView name;
    private ImageView picture;
    private API api;

    private User foreignUser;

    private TabLayout tabLayout;
    private Fragment fragment = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_profile, contentFrameLayout);
        setTitleToolbar("Profil");
        try {
        api = API.getInstance();

        String foreignUserEmail = (String) getIntent().getSerializableExtra("ForeignUser");


            foreignUser = api.getUserWithEmail(foreignUserEmail);


        if(foreignUser == null) Log.d("NULLWARNING", "foreignUser is null in ForeignProfileActivity");

        tabLayout = findViewById(R.id.tabs);
        Bundle bundle = new Bundle();
        bundle.putString("login", String.valueOf(foreignUser.getLogin()));
        bundle.putString("email", null);
        bundle.putString("description", foreignUser.getDescription());
        fragment = new DataFragment();
        fragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        tabLayout.addOnTabSelectedListener(this);
        if (foreignUser.getPicture() != "null") {
            picture = (ImageView) findViewById(R.id.user_profile_photo);
            Picasso.get().load(BuildConfig.STORAGE_URL + foreignUser.getPicture()).transform(new CropCircleTransformation()).into(picture);
        }
        name = (TextView) findViewById(R.id.user_profile_name);
        name.setText(String.valueOf(foreignUser.getName()));
        edit = findViewById(R.id.floating_action_button);
        edit.setVisibility(View.GONE);
        } catch (UnknownHostException e){
            ForeignProfileActivity.this.finish();
            Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Bundle bundle = new Bundle();
        switch (tab.getPosition()) {
            case 0:
                bundle.putString("login", foreignUser.getLogin());
                bundle.putString("email", null);
                bundle.putString("description", foreignUser.getDescription());
                fragment = new DataFragment();
                fragment.setArguments(bundle);
                break;
            case 1:
                bundle.putString("email", foreignUser.getEmail());
                bundle.putString("type", "foreign");
                fragment = new ContactsFragment();
                fragment.setArguments(bundle);
                break;
            case 2:
                bundle.putString("email", foreignUser.getEmail());
                fragment = new AdvertisementProfileFragment();
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
