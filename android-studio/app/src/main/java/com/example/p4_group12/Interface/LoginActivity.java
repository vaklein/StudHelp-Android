package com.example.p4_group12.Interface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;


import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    public static final String PREFS_NAME = "Email";
    public static final String PREF_EMAIL = null;
    public static final String PREF_ARRAY = "Array";
    public static final String PREF_TOKEN_DATE_ARRAY = null;
    public static final String PREF_DATE = "data";
    public static final String PREF_COURSE_ARRAY_LIST = null;
    String date_courses_data = "test";

    // Test values
    //private Button rootButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingDialog = new LoadingDialog(this, "Connexion en cours...");

        SharedPreferences pref_email = getSharedPreferences(PREFS_NAME,MODE_PRIVATE); // We only get the email. We might need to get the API token or the password
        String already_email = pref_email.getString(PREF_EMAIL, null);
        SharedPreferences pref_date = getSharedPreferences(PREF_DATE,MODE_PRIVATE); // We only get the email. We might need to get the API token or the password
        String token_date_array = pref_date.getString(PREF_TOKEN_DATE_ARRAY, "1900-01-01 00:00:00");

        if (already_email != null) {
            loadingDialog.getDialog().show();

            API api =  API.setToken(getSharedPreferences(PREFS_NAME,MODE_PRIVATE));
            if (api.getUserWithEmail(already_email) == null) {
                SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
                sharedPreferences.edit().putString(PREF_EMAIL, null).apply();

                GlobalVariables.setUser(null);
                GlobalVariables.switchBooleanToken();

                Intent intentLoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentLoginActivity);
                finish();
            }
            Log.v("jerem", "result email:"+api.getUserWithEmail(already_email));
            Log.v("jerem", "result email 2:"+already_email);
            GlobalVariables.setUser(api.getUserWithEmail(already_email));
            date_courses_data = API.tokenUpdateCourses();

            // Doing all the synchronous queries
            Log.v("jerem", "result :"+date_courses_data);
            Log.v("jerem", "result list :"+pref_date.getString(PREF_TOKEN_DATE_ARRAY, null));
            SharedPreferences pref = getSharedPreferences(PREF_ARRAY,MODE_PRIVATE); // We only get the email. We might need to get the API token or the password
            Log.v("jerem", "result list :"+pref.getString(PREF_COURSE_ARRAY_LIST, null));

            try {
                loadData(token_date_array, date_courses_data);
            } catch (ParseException e) {
                e.printStackTrace();
            }

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
                sign_up.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(sign_up);
            }
        });
        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.getDialog().show();
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
                                Log.v("jeremE", jsonObject.getString("email"));
                                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                                        .edit()
                                        .putString(PREF_EMAIL, jsonObject.getString("email"))
                                        .apply();
                                API.saveToken(getSharedPreferences(PREFS_NAME, MODE_PRIVATE)); // saving the API key in the shared prefs
                            }
                            GlobalVariables.setUser(new User(jsonObject.getString("name"), jsonObject.getString("login"), jsonObject.getString("email"), jsonObject.getString("picture"), jsonObject.getString("description")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        date_courses_data = API.tokenUpdateCourses();
                        Log.v("jerem", "result : "+date_courses_data);

                        try {
                            loadData(token_date_array, date_courses_data);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //Intent edit_profil = new Intent(getApplicationContext(), ProfileActivity.class);
                        //startActivity(edit_profil);
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("FavList", false);
                        startActivity(intent);
                        loadingDialog.getDialog().cancel();
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

    private void loadData(String token_date_array, String date_courses_data) throws ParseException {
        if (date_courses_data == null) {

            Log.v("Jules", "is null");
        }
        Date date_local = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(token_date_array);
        Date date_database = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date_courses_data);
        Log.v("jerem1", date_local.toString());
        Log.v("jerem1", date_database.toString());
        if (date_local.before(date_database)){ // case where the user don't have the last version of courses
            ArrayList<Course> loadCourses = API.getInstance().getCourses();
            GlobalVariables.setCourses(loadCourses);
            // creating a new variable for gson.
            Gson gson = new Gson();
            // getting data from gson and storing it in a string.
            String json = gson.toJson(loadCourses);

            getSharedPreferences(PREF_ARRAY, MODE_PRIVATE)
                    .edit()
                    .putString(PREF_COURSE_ARRAY_LIST, json)
                    .apply();
            getSharedPreferences(PREF_DATE, MODE_PRIVATE)
                    .edit()
                    .putString(PREF_TOKEN_DATE_ARRAY, date_courses_data)
                    .apply();
        }
        else{
            SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE); // We only get the email. We might need to get the API token or the password
            // creating a variable for gson.
            Gson gson = new Gson();

            // below line is to get to string present from our
            // shared prefs if not present setting it as null.
            SharedPreferences pref_array = getSharedPreferences(PREF_ARRAY,MODE_PRIVATE); // We only get the email. We might need to get the API token or the password
            String json = pref_array.getString(PREF_COURSE_ARRAY_LIST, null);

            // below line is to get the type of our array list.
            Type type = new TypeToken<ArrayList<Course>>(){}.getType();

            // in below line we are getting data from gson
            // and saving it to our array list
            GlobalVariables.setCourses(gson.fromJson(json, type));
        }
    }
}