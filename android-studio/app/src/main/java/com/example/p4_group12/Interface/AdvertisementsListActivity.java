package com.example.p4_group12.Interface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.Tag;
import com.example.p4_group12.Interface.adapter.AdvertisementListAdapter;
import com.example.p4_group12.Interface.fragments.AdvertisementProfileFragment;
import com.example.p4_group12.Interface.fragments.AdvertisementsListFragment;
import com.example.p4_group12.Interface.fragments.ContactsFragment;
import com.example.p4_group12.Interface.fragments.DataFragment;
import com.example.p4_group12.Interface.fragments.FileFragment;
import com.example.p4_group12.R;

import com.example.p4_group12.database.API;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.util.Log;
import android.widget.Toast;

import static java.util.Collections.sort;

public class AdvertisementsListActivity extends NavigationActivity implements TabLayout.OnTabSelectedListener{
    private RecyclerView advertisementRecyclerView;
    private RecyclerView.LayoutManager advertisementLayoutManager;
    private AdvertisementListAdapter advertisementListAdapter;
    private ArrayList<Advertisement> advertisementsListComplete;
    private ArrayList<Advertisement> advertisementsListToShow;
    private TextView courseCode;
    private TextView courseFac;
    private ChipGroup filters;
    private final List<Chip> filterChips = new ArrayList<>();
    private TextView mTextView;
    private FloatingActionButton newAdvertisementButton;
    private API api;
    private TextView noAdvertisement;
    private Course currentCourse;
    private FloatingActionButton mMainAddFab, mAddAdvertisementFab, mAddFileFab;
    private TextView mAddAdvertisementText, mAddFileText;
    private Animation mFabOpenAnim;
    private Animation mFabCloseAnim;

    private TabLayout tabLayout;
    private Fragment fragment = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private boolean isOpen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_course_advertisments_list, contentFrameLayout);


        currentCourse = (Course) getIntent().getSerializableExtra("ClickedCourse");
        if(currentCourse == null) Log.d("NULLWARNING", "Course is null in AdvertisementListActivity");
        setTitleToolbar("Annonces pour le cours " + currentCourse.getName());

        tabLayout = findViewById(R.id.tabs);
        Bundle bundle = new Bundle();
        bundle.putInt("course_id", currentCourse.getID());
        bundle.putString("code", currentCourse.getCode());
        bundle.putString("fac", currentCourse.getFaculty());
        fragment = new AdvertisementsListFragment();
        fragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        tabLayout.addOnTabSelectedListener(this);

        mMainAddFab = findViewById(R.id.plus_button);
        mAddAdvertisementFab = findViewById(R.id.new_advertisement_button);
        mAddFileFab = findViewById(R.id.new_file_button);

        mAddAdvertisementText = findViewById(R.id.add_advertisement_text);
        mAddFileText = findViewById(R.id.add_file_text);

        mFabOpenAnim = AnimationUtils.loadAnimation(AdvertisementsListActivity.this, R.anim.fab_open);
        mFabCloseAnim = AnimationUtils.loadAnimation(AdvertisementsListActivity.this, R.anim.fab_close);

        isOpen = false;

        mMainAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isOpen){

                    mAddAdvertisementFab.setAnimation(mFabCloseAnim);
                    mAddFileFab.setAnimation(mFabCloseAnim);

                    mAddAdvertisementText.setVisibility(View.INVISIBLE);
                    mAddFileText.setVisibility(View.INVISIBLE);

                    isOpen = false;
                } else {

                    mAddAdvertisementFab.setAnimation(mFabOpenAnim);
                    mAddFileFab.setAnimation(mFabOpenAnim);

                    mAddAdvertisementText.setVisibility(View.VISIBLE);
                    mAddFileText.setVisibility(View.VISIBLE);

                    isOpen = true;
                }

            }
        });
        // Gestion du bouton pour créer une nouvelle annonce
        mAddAdvertisementFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplication().getBaseContext(), clickedCourse.getName(), Toast.LENGTH_LONG).show();
                Intent newAdvertisement = new Intent(getApplicationContext(), AddAdvertisementActivity.class);
                newAdvertisement.putExtra("CurrentCourse", currentCourse);
                newAdvertisement.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(newAdvertisement, 1); // 1 for finish
                //startActivity(newAdvertisement);
            }
        });

        // Gestion du bouton pour créer une nouvelle synthese
        mAddFileFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplication().getBaseContext(), clickedCourse.getName(), Toast.LENGTH_LONG).show();
                Intent newAdvertisement = new Intent(getApplicationContext(), AddFileActivity.class);
                newAdvertisement.putExtra("CurrentCourse", currentCourse);
                newAdvertisement.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(newAdvertisement, 1); // 1 for finish
                //startActivity(newAdvertisement);
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Bundle bundle = new Bundle();
        switch (tab.getPosition()) {
            case 0:
                bundle.putInt("course_id", currentCourse.getID());
                bundle.putString("code", currentCourse.getCode());
                bundle.putString("fac", currentCourse.getFaculty());
                fragment = new AdvertisementsListFragment();
                fragment.setArguments(bundle);
                break;
            case 1:
                bundle.putInt("course_id", currentCourse.getID());
                fragment = new FileFragment();
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem changeFavorite = menu.findItem(R.id.action_favorite);
        changeFavorite.setVisible(true);

        try{
            HashSet<Integer> userFavorites = API.getInstance().getFavoriteCoursesIdsOfUser(GlobalVariables.getUser());

            if (userFavorites.contains(currentCourse.getID())) {
                changeFavorite.setChecked(true);
                changeFavorite.setIcon(R.drawable.ic_baseline_favorite_selected);
            } else {
                changeFavorite.setChecked(false);
                changeFavorite.setIcon(R.drawable.ic_baseline_favorite_not_selected);
            }
            return true;
        }catch (UnknownHostException e){
            finish();
            Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG);
        } finally {
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_favorite:
                if(item.isChecked()){
                    // Log.d("Gwen", "Adding " + currentCourse.getCode() + " to the favorites");
                    item.setChecked(false);
                    item.setIcon(R.drawable.ic_baseline_favorite_not_selected);
                    API.getInstance().removeFavoriteToUser(GlobalVariables.getUser(), currentCourse);
                }
                else{
                    // Log.d("Gwen", "Removing " + currentCourse.getCode() + " from the favorites");
                    item.setChecked(true);
                    item.setIcon(R.drawable.ic_baseline_favorite_selected);
                    API.getInstance().addNewFavoriteToUser(GlobalVariables.getUser(), currentCourse);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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