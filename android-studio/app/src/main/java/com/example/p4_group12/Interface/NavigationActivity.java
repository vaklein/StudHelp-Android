package com.example.p4_group12.Interface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.Objects;

public class NavigationActivity extends AppCompatActivity{
    private DrawerLayout drawerLayout;
    protected MaterialToolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView toolbartitle;
    private Button deconnexion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String currAct = this.getClass().getSimpleName();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        // Sending the token when the user is logged in
        // At startup
        if(!GlobalVariables.tokenAlreadySent()){
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                // Fetching FCM registration token failed"
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();

                            // Sending the token to the DB
                            API.getInstance().sendToken(GlobalVariables.getUser().getEmail(), token);
                        }
                    });
            GlobalVariables.tokenSent();
        }


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //mis en commentaire car fait crasher l'app - Gwendal 10-03, je n'arrive pas a recreer le bug Lucas
        setTitle("");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView = findViewById(R.id.navigation);
        toolbartitle = findViewById(R.id.toolbar_title);
        deconnexion = findViewById(R.id.deconnexion);

        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME,MODE_PRIVATE);
                sharedPreferences.edit().putString(LoginActivity.PREF_EMAIL, null).apply();

                API.getInstance().logoutUser(sharedPreferences);

                GlobalVariables.setUser(null);
                GlobalVariables.revokeToken();

                Intent intentLoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                intentLoginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intentLoginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentLoginActivity);
                finish();
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_profil:
                        Intent intentprofile = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intentprofile);
                        if (!currAct.equals("HomeActivity")) finish();
                        break;
                    case R.id.nav_home:
                        Intent intentHome = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intentHome);
                        finish();
                        break;
                    /* Ça n'a plus beaucoup de sens d'afficher la liste des 42.500 cours...
                    case R.id.nav_courses:
                        Intent intentcourses = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(intentcourses);
                        finish();
                        break;*/
                    case R.id.nav_myadvertisements:
                        Intent intentmyadverts = new Intent(getApplicationContext(), MyAdvertisementsActivity.class);
                        startActivity(intentmyadverts);
                        if (!currAct.equals("HomeActivity")) finish();
                        break;
                    case R.id.nav_mybookmarks:
                        Intent intentMyBookmarks = new Intent(getApplicationContext(), MyBookmarksActivity.class);
                        startActivity(intentMyBookmarks);
                        if (!currAct.equals("HomeActivity")) finish();
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
