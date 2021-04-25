package com.example.p4_group12.Interface.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.p4_group12.DAO.Social_links;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.Interface.EditProfileActivity;
import com.example.p4_group12.Interface.GlobalVariables;
import com.example.p4_group12.Interface.ProfileActivity;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.google.android.material.card.MaterialCardView;

import java.net.UnknownHostException;

public class ContactsFragment extends Fragment {

    private Social_links s;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_contacts, container, false);

        MaterialCardView discordlayout = result.findViewById(R.id.discord_profil_champ);
        MaterialCardView phonelayout = result.findViewById(R.id.phone_profil_champ);
        MaterialCardView publicemaillayout = result.findViewById(R.id.email_profil_champ);
        MaterialCardView teamslayout = result.findViewById(R.id.teams_profil_champ);

        TextView noNetworkString = result.findViewById(R.id.no_network_profil);
        TextView phonetext = result.findViewById(R.id.phone_text);
        TextView publicemailtext = result.findViewById(R.id.email_public_frag_text);
        TextView teamstext = result.findViewById(R.id.teams_text);
        TextView discordtext = result.findViewById(R.id.discord_text);

        User user = new User(this.getArguments().getString("email"));

        try {
            if (this.getArguments().getString("type").equals("foreign")){
                s = API.getInstance().getSocialLinksOfUser(user);
            }else {
                if (GlobalVariables.getUser().getSocial_links() == null) {
                    s = API.getInstance().getSocialLinksOfUser(GlobalVariables.getUser());
                    GlobalVariables.getUser().setSocial_links(s);
                } else {
                    s = GlobalVariables.getUser().getSocial_links();
                }
            }

            if(!s.getDiscord().equals("") && s.getDiscord() != null){
                SpannableString content = new SpannableString(s.getDiscord());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                discordtext.setText(content);
                discordlayout.setVisibility(View.VISIBLE);
                noNetworkString.setVisibility(View.GONE);
            }

            if(!s.getTeams().equals("") && s.getTeams() != null){
                SpannableString content = new SpannableString(s.getTeams());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                teamstext.setText(content);
                teamslayout.setVisibility(View.VISIBLE);
                noNetworkString.setVisibility(View.GONE);
            }

            if(!s.getPhone().equals("") && s.getPhone() != null){
                SpannableString content = new SpannableString(s.getPhone());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                phonetext.setText(content);
                phonelayout.setVisibility(View.VISIBLE);
                noNetworkString.setVisibility(View.GONE);
            }

            if(!s.getPublicEmail().equals("") && s.getPublicEmail() != null){
                SpannableString content = new SpannableString(s.getPublicEmail());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                publicemailtext.setText(content);
                publicemaillayout.setVisibility(View.VISIBLE);
                noNetworkString.setVisibility(View.GONE);
            }
        } catch (UnknownHostException e){
            getActivity().finish();
            Toast.makeText(getContext(), R.string.no_connection, Toast.LENGTH_LONG);
        }



        discordtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Discord contact", s.getDiscord());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Copié !", Toast.LENGTH_LONG).show();
            }
        });

        teamstext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://teams.microsoft.com/l/chat/0/0?users="+s.getTeams()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        phonetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Facebook contact", s.getPhone());
                clipboard.setPrimaryClip(clip);
            }
        });

        publicemailtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { s.getPublicEmail() });
                startActivity(intent);
            }
        });
        return result;
    }
}
