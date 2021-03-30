package com.example.p4_group12.Interface;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Social_links;
import com.example.p4_group12.R;
import com.example.p4_group12.database.DatabaseContact;
import com.example.p4_group12.database.GetObjectFromDB;
import com.google.android.material.card.MaterialCardView;
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
    private MaterialCardView discordlayout;
    private MaterialCardView teamslayout;
    private MaterialCardView facebooklayout;
    private TextView noNetworkString;
    private TextView facebooktext;
    private TextView discordtext;
    private TextView teamstext;



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
        if (!GlobalVariables.getSocialNetwokCharged()) {
            ArrayList<Social_links> reseaux = new ArrayList<>();
            GetObjectFromDB.getJSON(BuildConfig.DB_URL + "get_social_links.php?UserEmail=" + GlobalVariables.getEmail(), reseaux, Social_links.class);
            Social_links s = reseaux.get(0);
            GlobalVariables.setDiscord(s.getDiscord());
            GlobalVariables.setTeams(s.getTeams());
            GlobalVariables.setFacebook(s.getFacebook());
            GlobalVariables.setSocialNetwokCharged(true);
        }

        if(!GlobalVariables.getDiscord().equals("")){
            SpannableString content = new SpannableString(GlobalVariables.getDiscord());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            discordtext.setText(content);
            discordlayout.setVisibility(View.VISIBLE);
            noNetworkString.setVisibility(View.GONE);
        }
        discordtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Discord contact", GlobalVariables.getDiscord());
                clipboard.setPrimaryClip(clip);
            }
        });
        if(!GlobalVariables.getTeams().equals("")){
            SpannableString content = new SpannableString(GlobalVariables.getTeams());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            teamstext.setText(content);
            teamslayout.setVisibility(View.VISIBLE);
            noNetworkString.setVisibility(View.GONE);
        }
        teamstext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Teams contact", GlobalVariables.getTeams());
                clipboard.setPrimaryClip(clip);
            }
        });
        if(!GlobalVariables.getFacebook().equals("")){
            SpannableString content = new SpannableString(GlobalVariables.getFacebook());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            facebooktext.setText(content);
            facebooklayout.setVisibility(View.VISIBLE);
            noNetworkString.setVisibility(View.GONE);
        }
        facebooktext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Facebook contact", GlobalVariables.getFacebook());
                clipboard.setPrimaryClip(clip);
            }
        });




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
