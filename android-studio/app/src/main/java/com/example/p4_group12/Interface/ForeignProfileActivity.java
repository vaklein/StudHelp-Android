package com.example.p4_group12.Interface;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.R;
import com.example.p4_group12.database.GetObjectFromDB;

import java.util.ArrayList;

public class ForeignProfileActivity extends NavigationActivity {
    private TextView name;
    private TextView login;
    private TextView email;
    private User foreignUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_foreign_profile, contentFrameLayout);

        name = (TextView) findViewById(R.id.foreign_user_profile_name);
        login = (TextView) findViewById(R.id.foreign_user_profil_login);
        email = (TextView) findViewById(R.id.foreign_user_profil_email);

        //String foreignUserEmail = (String) getIntent().getSerializableExtra("ForeignUser");
        /*ArrayList<User> onlyUser = new ArrayList<>();
        GetObjectFromDB.getJSON("https://db.valentinklein.eu:8182/get_user_from_email.php?UserEmail="+"jules.lesuisse@student.uclouvain.be", onlyUser, User.class);
        Log.v("Jules", onlyUser.toString());
        foreignUser = onlyUser.get(0);
        if(foreignUser == null) Log.d("NULLWARNING", "foreignUser is null in ForeignProfileActivity");
        */

        foreignUser = new User("Bond", "jamesbond", "jamesbond@killing.you");
        setTitleToolbar("Contact profile");
        name.setText(String.valueOf(foreignUser.getName()));
        login.setText(String.valueOf(foreignUser.getLogin()));
        email.setText(String.valueOf(foreignUser.getEmail()));

        // TODO : Add the contact link below ! :)
    }
}
