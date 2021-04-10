package com.example.p4_group12.Interface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;


import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private Button sign_up;
    private Button connexion;
    private TextInputEditText login;
    private TextInputEditText password;
    private TextInputLayout loginField;
    private TextInputLayout passwordField;
    private LoadingDialog loadingDialog;
    // to remember that the user is already connected
    private Switch rememberMe;
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String PREF_EMAIL = null;
    // Test values
    //private Button rootButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingDialog = new LoadingDialog(this, "Connexion en cours...");

        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE); // We only get the email. We might need to get the API token or the password
        String already_email = pref.getString(PREF_EMAIL, null);
        if (already_email != null) {
            loadingDialog.getDialog().show();

            API api =  API.setToken(getSharedPreferences(PREFS_NAME,MODE_PRIVATE));
            GlobalVariables.setUser(api.getUserWithEmail(already_email));


            // Doing all the synchronous queries
            ArrayList<Course> loadCourses = api.getCourses();
            GlobalVariables.setCourses(loadCourses);

            //Intent edit_profil = new Intent(getApplicationContext(), ProfileActivity.class);
            //startActivity(edit_profil);
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("FavList", false);
            startActivity(intent);
            loadingDialog.getDialog().cancel();
            LoginActivity.this.finish();
        }

        sign_up = findViewById(R.id.sign_up);
        connexion = findViewById(R.id.connexion);
        login = (TextInputEditText) findViewById(R.id.logintext);
        password = (TextInputEditText) findViewById(R.id.passwordtext);
        loginField = (TextInputLayout) findViewById(R.id.teams);
        passwordField = (TextInputLayout) findViewById(R.id.password);
        rememberMe = findViewById(R.id.rememberme);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_up = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(sign_up);
            }
        });
        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginField.setErrorEnabled(false);
                passwordField.setErrorEnabled(false);
                if (isCorrectlyFil()) {
                    JSONObject jsonObject = API.loginUser(login.getText().toString(), password.getText().toString());

                    if(jsonObject == null) Toast.makeText(LoginActivity.this, "OOPs! Réessayer", Toast.LENGTH_LONG).show();
                    else if (jsonObject.has("message")) {
                        loginField.setError("Identifiant/Mot de passe incorrect");
                        passwordField.setError("Identifiant/Mot de passe incorrect");
                    } else {
                        try {
                            if (rememberMe.isChecked()) {
                                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                                        .edit()
                                        .putString(PREF_EMAIL, jsonObject.getString("email"))
                                        .apply();
                                API.saveToken(getSharedPreferences(PREFS_NAME, MODE_PRIVATE)); // saving the API key in the shared prefs
                            }
                            GlobalVariables.setUser(new User(jsonObject.getString("name"), jsonObject.getString("login"), jsonObject.getString("email"), jsonObject.getString("password")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Intent edit_profil = new Intent(getApplicationContext(), ProfileActivity.class);
                        //startActivity(edit_profil);
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("FavList", false);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                }
            }
        });
    }
    private boolean isCorrectlyFil() {
        // tout doit être complèter
        boolean filled = true;
        if (login.getText().toString().isEmpty()) {
            filled = false;
            loginField.setError("Champs obligatoire");
        }
        if (password.getText().toString().isEmpty()) {
            filled = false;
            passwordField.setError("Champs obligatoire");
        }
        return filled;
    }

}