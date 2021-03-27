package com.example.p4_group12.Interface;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p4_group12.DAO.Social_links;
import com.example.p4_group12.R;
import com.example.p4_group12.database.DatabaseContact;
import com.example.p4_group12.database.GetObjectFromDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

public class ProfileActivity extends NavigationActivity {

    private FloatingActionButton edit;
    private TextView name;
    private TextView login;
    private TextView email;
    private LinearLayout discordlayout;
    private LinearLayout teamslayout;
    private LinearLayout facebooklayout;
    private TextView noNetworkString;
    private TextInputEditText facebooktext;
    private TextInputEditText discordtext;
    private TextInputEditText teamstext;
    private ArrayList<TextInputEditText> textreseaux ;
    private ArrayList<LinearLayout> affichagereseaux ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_profile, contentFrameLayout);
        setTitleToolbar("Profil");

        name = (TextView) findViewById(R.id.user_profile_name);
        login = (TextView) findViewById(R.id.user_profil_login);
        email = (TextView) findViewById(R.id.user_profil_email);
        edit = findViewById(R.id.floating_action_button);
        discordlayout = findViewById(R.id.discord_profil_champ);
        facebooklayout = findViewById(R.id.facebook_profil_champ);
        teamslayout = findViewById(R.id.teams_profil_champ);
        noNetworkString = findViewById(R.id.no_network_profil);
        facebooktext = findViewById(R.id.facebook_text);
        discordtext = findViewById(R.id.discord_text);
        teamstext = findViewById(R.id.teams_text);

        //ArrayList<String> reseaux = DatabaseContact.get_social_links(GlobalVariables.getEmail());
        ArrayList<Social_links> reseaux = new ArrayList<>();
        GetObjectFromDB.getJSON("https://db.valentinklein.eu:8182/get_social_links.php?UserEmail="+GlobalVariables.getEmail(),reseaux,Social_links.class);
        Social_links s=reseaux.get(0);
        if(!s.getDiscord().equals("")){
            discordtext.setText(s.getDiscord());
            discordlayout.setVisibility(View.VISIBLE);
            noNetworkString.setVisibility(View.GONE);
        }
        if(!s.getTeams().equals("")){
            teamstext.setText(s.getTeams());
            teamslayout.setVisibility(View.VISIBLE);
            noNetworkString.setVisibility(View.GONE);
        }
        if(!s.getFacebook().equals("")){
            facebooktext.setText(s.getFacebook());
            facebooklayout.setVisibility(View.VISIBLE);
            noNetworkString.setVisibility(View.GONE);
        }


        name.setText(String.valueOf(GlobalVariables.getName()));
        login.setText(String.valueOf(GlobalVariables.getLogin()));
        email.setText(String.valueOf(GlobalVariables.getEmail()));

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_profil = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(edit_profil);
            }
        });
    }
}
