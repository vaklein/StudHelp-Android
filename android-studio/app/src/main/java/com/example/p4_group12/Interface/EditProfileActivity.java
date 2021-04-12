package com.example.p4_group12.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class EditProfileActivity extends NavigationActivity {

    private Button edit_password;
    private FloatingActionButton edit_picture;
    private Button backup_profil;
    private TextInputEditText new_name;
    private TextInputEditText new_login;
    private TextInputLayout new_nameField;
    private TextInputLayout new_loginField;
    private LoadingDialog loadingDialog;
    private TextInputLayout phone;
    private TextInputEditText phone_text;
    private TextInputLayout public_email;
    private TextInputEditText public_email_text;
    private TextInputLayout discord;
    private TextInputEditText discord_text;
    private TextInputLayout teams;
    private TextInputEditText teams_text;
    private API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = API.getInstance();
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_edit_profile, contentFrameLayout);
        setTitleToolbar("Profil");
        edit_password = findViewById(R.id.edit_password);
        backup_profil = findViewById(R.id.backup);
        new_name = (TextInputEditText) findViewById(R.id.name_text);
        new_name.setText(GlobalVariables.getUser().getName());
        new_login = (TextInputEditText) findViewById(R.id.login_text);
        new_login.setText(GlobalVariables.getUser().getLogin());
        new_nameField = (TextInputLayout) findViewById(R.id.name);
        new_loginField = (TextInputLayout) findViewById(R.id.teams);
        loadingDialog = new LoadingDialog(this, "Modification en cours...");

        if(GlobalVariables.getUser().getSocial_links() == null){
            GlobalVariables.getUser().setSocial_links(api.getSocialLinksOfUser(GlobalVariables.getUser()));
        }

        phone_text = (TextInputEditText) findViewById(R.id.phone_text);
        phone_text.setText(GlobalVariables.getUser().getSocial_links().getPhone());
        public_email_text = (TextInputEditText) findViewById(R.id.email_public_text);
        public_email_text.setText(GlobalVariables.getUser().getSocial_links().getPublicEmail());
        discord_text = findViewById(R.id.discord_text);
        discord_text.setText(GlobalVariables.getUser().getSocial_links().getDiscord());
        teams_text = findViewById(R.id.teams_text);
        teams_text.setText(GlobalVariables.getUser().getSocial_links().getTeams());


        edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_pw = new Intent(getApplicationContext(), EditPasswordActivity.class);
                startActivity(edit_pw);
            }
        });
        backup_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariables.getUser().getSocial_links().setAllSocialLinks(phone_text.getText().toString(), public_email_text.getText().toString(), teams_text.getText().toString(), discord_text.getText().toString());
                API.getInstance().updateSocialLinks(GlobalVariables.getUser());

                new_loginField.setErrorEnabled(false);
                if (!new_name.getText().toString().isEmpty() || !new_login.getText().toString().isEmpty()) {
                    Boolean apiResponse = API.getInstance().editNameAndLogin(GlobalVariables.getUser(), new_name.getText().toString(), new_login.getText().toString());

                    if(apiResponse == null){ // error
                        Toast.makeText(EditProfileActivity.this, "Une erreur est survenue lors de la modification de votre nom, veuilliez réessayer", Toast.LENGTH_LONG).show();
                    }else if(apiResponse){
                        Intent edit_profil = new Intent(getApplicationContext(), ProfileActivity.class);
                        if (!new_name.getText().toString().isEmpty()) GlobalVariables.getUser().setName(new_name.getText().toString());
                        if(!new_login.getText().toString().isEmpty()) GlobalVariables.getUser().setLogin(new_login.getText().toString());
                        startActivity(edit_profil);
                        EditProfileActivity.this.finish();
                    }else{
                        new_loginField.setError("Identifiant déjà utilisé");
                    }
                }

            }
        });
    }
}