package com.example.p4_group12.Interface;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Social_links;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.Interface.fragments.ContactsFragment;
import com.example.p4_group12.Interface.fragments.DataFragment;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
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

public class ForeignProfileActivity extends NavigationActivity implements TabLayout.OnTabSelectedListener {

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
    private TextView textreseauxsociaux;
    private ArrayList<TextInputEditText> textreseaux ;
    private ArrayList<LinearLayout> affichagereseaux ;
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

        api = API.getInstance();

        String foreignUserEmail = (String) getIntent().getSerializableExtra("ForeignUser");

        User foreignUser = api.getUserWithEmail(foreignUserEmail);

        if(foreignUser == null) Log.d("NULLWARNING", "foreignUser is null in ForeignProfileActivity");

        tabLayout = findViewById(R.id.tabs);
        Bundle bundle = new Bundle();
        bundle.putString("login", String.valueOf(foreignUser.getLogin()));
        bundle.putString("email", null);
        fragment = new DataFragment();
        fragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        tabLayout.addOnTabSelectedListener(this);
        name = (TextView) findViewById(R.id.user_profile_name);
        name.setText(String.valueOf(foreignUser.getName()));
        edit = findViewById(R.id.floating_action_button);
        edit.setVisibility(View.GONE);

  /*
        login = (TextView) findViewById(R.id.user_profil_login);
        email = (TextView) findViewById(R.id.user_profil_email);
        discordlayout = findViewById(R.id.discord_profil_champ);
        facebooklayout = findViewById(R.id.facebook_profil_champ);
        teamslayout = findViewById(R.id.teams_profil_champ);
        facebooktext = findViewById(R.id.facebook_text);
        discordtext = findViewById(R.id.discord_text);
        teamstext = findViewById(R.id.teams_text);


        //ArrayList<String> reseaux = DatabaseContact.get_social_links(GlobalVariables.getEmail());
        Social_links s= api.getSocialLinksOfUser(foreignUser);
        if(!s.getDiscord().equals("")){
            SpannableString content = new SpannableString(s.getDiscord());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            discordtext.setText(content);
            discordlayout.setVisibility(View.VISIBLE);
            noNetworkString.setVisibility(View.GONE);
        }
        discordtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Discord contact", s.getDiscord());
                clipboard.setPrimaryClip(clip);
            }
        });
        if(!s.getTeams().equals("")){
            SpannableString content = new SpannableString(s.getTeams());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            teamstext.setText(content);
            teamslayout.setVisibility(View.VISIBLE);
            noNetworkString.setVisibility(View.GONE);
        }
        teamstext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Teams contact", s.getTeams());
                clipboard.setPrimaryClip(clip);
            }
        });
        if(!s.getFacebook().equals("")){
            SpannableString content = new SpannableString(s.getFacebook());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            facebooktext.setText(content);
            facebooklayout.setVisibility(View.VISIBLE);
            noNetworkString.setVisibility(View.GONE);
        }
        facebooktext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Facebook contact", s.getFacebook());
                clipboard.setPrimaryClip(clip);
            }
        });



        login.setText(String.valueOf(foreignUser.getLogin()));
        email.setText(String.valueOf(foreignUser.getEmail()));


*/
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Bundle bundle = new Bundle();
        switch (tab.getPosition()) {
            case 0:
                bundle.putString("login", String.valueOf(foreignUser.getLogin()));
                bundle.putString("email", null);
                fragment = new DataFragment();
                fragment.setArguments(bundle);
                break;

            case 1:
                bundle.putString("email", String.valueOf(foreignUser.getEmail()));
                bundle.putString("type", "foreign");
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
