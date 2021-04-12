package com.example.p4_group12.Interface.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.p4_group12.Interface.GlobalVariables;
import com.example.p4_group12.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DataFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_data, container, false);
        TextView login = (TextView) result.findViewById(R.id.user_profil_login);
        TextView email = (TextView) result.findViewById(R.id.user_profil_email);
        TextView description = (TextView) result.findViewById(R.id.user_profil_description);
        String loginValue = this.getArguments().getString("login");
        String emailValue = this.getArguments().getString("email");
        String descriptionValue = this.getArguments().getString("description");
        login.setText(loginValue);
        if (descriptionValue == "null") result.findViewById(R.id.descriptionCard).setVisibility(View.GONE);
        else description.setText(descriptionValue);
        if (emailValue == null) result.findViewById(R.id.emailCard).setVisibility(View.GONE);
        else email.setText(emailValue);
        return result;
    }

}
