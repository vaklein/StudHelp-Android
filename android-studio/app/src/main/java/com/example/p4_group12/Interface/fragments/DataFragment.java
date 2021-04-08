package com.example.p4_group12.Interface.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.p4_group12.Interface.GlobalVariables;
import com.example.p4_group12.R;

public class DataFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_data, container, false);
        TextView login = (TextView) result.findViewById(R.id.user_profil_login);
        TextView email = (TextView) result.findViewById(R.id.user_profil_email);
        login.setText(String.valueOf(GlobalVariables.getLogin()));
        email.setText(String.valueOf(GlobalVariables.getEmail()));
        return result;
    }
}
