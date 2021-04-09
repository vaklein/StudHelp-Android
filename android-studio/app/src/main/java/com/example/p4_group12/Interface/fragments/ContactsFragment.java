package com.example.p4_group12.Interface.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Social_links;
import com.example.p4_group12.Interface.GlobalVariables;
import com.example.p4_group12.R;
import com.example.p4_group12.database.GetObjectFromDB;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class ContactsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_contacts, container, false);

        MaterialCardView discordlayout = result.findViewById(R.id.discord_profil_champ);
        MaterialCardView facebooklayout = result.findViewById(R.id.facebook_profil_champ);
        MaterialCardView teamslayout = result.findViewById(R.id.teams_profil_champ);

        TextView noNetworkString = result.findViewById(R.id.no_network_profil);
        TextView facebooktext = result.findViewById(R.id.facebook_text);
        TextView teamstext = result.findViewById(R.id.teams_text);
        TextView discordtext = result.findViewById(R.id.discord_text);

        String emailValue = this.getArguments().getString("email");
        //ArrayList<String> reseaux = DatabaseContact.get_social_links(GlobalVariables.getEmail());
        Social_links s;
        if (this.getArguments().getString("type").equals("foreign")){
            noNetworkString.setText(getText(R.string.no_social_network_other_user));
            ArrayList<Social_links> reseaux = new ArrayList<>();
            GetObjectFromDB.getJSON(BuildConfig.DB_URL + "get_social_links.php?UserEmail="+emailValue,reseaux,Social_links.class);
            s = reseaux.get(0);
        }else {
            if (!GlobalVariables.getSocialNetwokCharged()) {
                ArrayList<Social_links> reseaux = new ArrayList<>();
                GetObjectFromDB.getJSON(BuildConfig.DB_URL + "get_social_links.php?UserEmail=" + GlobalVariables.getEmail(), reseaux, Social_links.class);
                s = reseaux.get(0);
                GlobalVariables.setSocial_links(s);
                GlobalVariables.setSocialNetwokCharged(true);
            } else {
                s = GlobalVariables.getSocial_links();
            }
        }

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
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
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
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
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
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Facebook contact", s.getFacebook());
                clipboard.setPrimaryClip(clip);
            }
        });
        return result;
    }
}
