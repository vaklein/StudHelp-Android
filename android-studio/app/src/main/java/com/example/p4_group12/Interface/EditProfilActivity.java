package com.example.p4_group12.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p4_group12.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class EditProfilActivity extends AppCompatActivity {

    private Button edit_password;
    private FloatingActionButton edit_picture;
    private Button backup_profil;
    private TextInputEditText new_name;
    private TextInputEditText new_login;
    private TextInputLayout new_nameField;
    private TextInputLayout new_loginField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        edit_picture = findViewById(R.id.edit);
        edit_password = findViewById(R.id.edit_password);
        backup_profil = findViewById(R.id.backup);
        new_name = (TextInputEditText) findViewById(R.id.name_text);
        new_login = (TextInputEditText) findViewById(R.id.login_text);
        new_nameField = (TextInputLayout) findViewById(R.id.name);
        new_loginField = (TextInputLayout) findViewById(R.id.name);

        edit_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : laisser l'utilisateru rentrer dans ces photos pour changer la ppt
            }
        });


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
                new_loginField.setErrorEnabled(false);
                boolean correct = true;
                if (! new_name.getText().toString().isEmpty()){
                    //TODO set the new nam in the BDD
                }
                if (! new_login.getText().toString().isEmpty()){
                    //TODO  check if the new login isn't already took + set if not + set correct to false if already took
                }
                if (correct){
                    Intent edit_pw = new Intent(getApplicationContext(), ProfilActivity.class);
                    startActivity(edit_pw);
                    finish();
                }
                else {
                    new_loginField.setError("Identifiant déjà prit");
                }
            }
        });
    }
}