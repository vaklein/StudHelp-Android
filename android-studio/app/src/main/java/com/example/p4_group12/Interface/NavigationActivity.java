package com.example.p4_group12.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.p4_group12.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class NavigationActivity extends AppCompatActivity{
    private DrawerLayout drawerLayout;
    protected MaterialToolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView toolbartitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar); //mis en commentaire car fait crasher l'app - Gwendal 10-03, je n'arrive pas a recreer le bug Lucas
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView = findViewById(R.id.navigation);
        toolbartitle = findViewById(R.id.toolbar_title);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_profil:
                        Intent intentprofile = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intentprofile);
                        break;
                    case R.id.nav_courses:
                        Intent intentcourses = new Intent(getApplicationContext(), CourseListActivity.class);
                        startActivity(intentcourses);
                        break;
                    case R.id.nav_myadvertisements:
                        Intent intentmyadverts = new Intent(getApplicationContext(), MyAdvertisementsActivity.class);
                        startActivity(intentmyadverts);
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

    public void setTitleToolbar(String s){
        toolbartitle.setText(s);
    }
}
