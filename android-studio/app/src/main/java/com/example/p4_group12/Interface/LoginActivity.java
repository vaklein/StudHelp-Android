
package com.example.p4_group12.Interface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    String token_date_array;

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
        token_date_array = pref_date.getString(PREF_TOKEN_DATE_ARRAY, "1900-01-01 00:00:00");

        if (already_email != null) {
            loadingDialog.getDialog().show();

            try {
                API api =  API.setToken(getSharedPreferences(PREFS_NAME,MODE_PRIVATE));
                if (api.getUserWithEmail(already_email) == null) {
                    SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
                    sharedPreferences.edit().putString(PREF_EMAIL, null).apply();

                    GlobalVariables.setUser(null);
                    GlobalVariables.revokeToken();

                    Intent intentLoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intentLoginActivity);
                    finish();
                }

                GlobalVariables.setUser(api.getUserWithEmail(already_email));
                date_courses_data = API.tokenUpdateCourses();

                // Doing all the synchronous queries
                SharedPreferences pref = getSharedPreferences(PREF_ARRAY,MODE_PRIVATE); // We only get the email. We might need to get the API token or the password


                loadData(token_date_array, date_courses_data);
                //Intent edit_profil = new Intent(getApplicationContext(), ProfileActivity.class);
                //startActivity(edit_profil);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("FavList", false);
                startActivity(intent);
                loadingDialog.getDialog().cancel();
                LoginActivity.this.finish();

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (UnknownHostException e){
                Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
                loadingDialog.getDialog().cancel();
            }
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
                new SyncGetJSON().execute(login.getText().toString(), password.getText().toString());
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

    private void loadData(String token_date_array, String date_courses_data) throws ParseException, UnknownHostException {
        Date date_local = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(token_date_array);
        Date date_database = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date_courses_data);
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
    class SyncGetJSON extends AsyncTask<String, Void, String> {
        UnknownHostException connectionException;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.getDialog().show();
        }


        @Override
        protected String doInBackground(String... params) {
            try {
                String data = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");

                // Sending the request
                URL url = new URL(BuildConfig.DB_URL + "/login");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");  //setting the request type
                httpURLConnection.setDoOutput(true);

                if(data.length() > 0){
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                }

                // Getting the answer from th DB
                InputStream IS = httpURLConnection.getResponseCode() / 100 == 2 ? httpURLConnection.getInputStream() : httpURLConnection.getErrorStream(); //DB answer
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS));
                String json;
                StringBuilder result = new StringBuilder();
                while ((json = bufferedReader.readLine()) != null) {
                    result.append(json + "\n");
                }
                IS.close();
                httpURLConnection.disconnect();
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (UnknownHostException e) {
                connectionException = e;
                return null;
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject = null;
            try {
                loginField.setErrorEnabled(false);
                passwordField.setErrorEnabled(false);
                if (isCorrectlyFil()) {
                    jsonObject = new JSONObject(s);
                    if(jsonObject == null) Toast.makeText(LoginActivity.this, "OOPs! Réessayer", Toast.LENGTH_LONG).show(); // pas certain que ça soit une bonne idée de lancer un toast ici https://stackoverflow.com/questions/3875184/cant-create-handler-inside-thread-that-has-not-called-looper-prepare
                    else if (jsonObject.has("message")) {
                        loginField.setError("Identifiant/Mot de passe incorrect");
                        passwordField.setError("Identifiant/Mot de passe incorrect");
                        loadingDialog.getDialog().cancel();
                    } else {
                        API.INSTANCE = new API(jsonObject.getString("token"));
                        JSONObject jsonObject_user = new JSONObject(jsonObject.getString("user"));
                        if (rememberMe.isChecked()) {
                            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                                    .edit()
                                    .putString(PREF_EMAIL, jsonObject_user.getString("email"))
                                    .apply();
                            API.saveToken(getSharedPreferences(PREFS_NAME, MODE_PRIVATE)); // saving the API key in the shared prefs
                        }
                        GlobalVariables.setUser(new User(jsonObject_user.getString("name"), jsonObject_user.getString("login"), jsonObject_user.getString("email"), jsonObject_user.getString("picture"), jsonObject_user.getString("description")));

                        date_courses_data = "null";
                        date_courses_data = API.tokenUpdateCourses();
                        loadData(token_date_array, date_courses_data);

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("FavList", false);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                }
            } catch (UnknownHostException e){
                Toast.makeText(LoginActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();
                loadingDialog.getDialog().cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            loadingDialog.getDialog().cancel();
        }
    }

    /*
    class SyncGetJSON_Already_Connected extends AsyncTask<String, Void, Object> {
        UnknownHostException connectionException;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.getDialog().show();
        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                // Sending the request
                URL url = new URL(BuildConfig.DB_URL + "/user/" + already_email);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");  //setting the request type
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");


                // Getting the answer from th DB
                InputStream IS = httpURLConnection.getResponseCode() / 100 == 2 ? httpURLConnection.getInputStream() : httpURLConnection.getErrorStream(); //DB answer
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS));
                JSONObject jsonObject = new JSONArray(bufferedReader).getJSONObject(0);
                IS.close();
                httpURLConnection.disconnect();
                return jsonObject;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (UnknownHostException e) {
                connectionException = e;
                return null;
            } catch (IOException e) {
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object s) {
            super.onPostExecute(s);
            try {
                API api =  API.setToken(getSharedPreferences(PREFS_NAME,MODE_PRIVATE));
                if (s == null) {
                    Log.v("ici", "ici");
                    SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
                    sharedPreferences.edit().putString(PREF_EMAIL, null).apply();

                    GlobalVariables.setUser(null);
                    GlobalVariables.revokeToken();

                    Intent intentLoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intentLoginActivity);
                    finish();
                }

                GlobalVariables.setUser((User) s);
                date_courses_data = API.tokenUpdateCourses();
                SharedPreferences pref = getSharedPreferences(PREF_ARRAY,MODE_PRIVATE); // We only get the email. We might need to get the API token or the password

                loadData(token_date_array, date_courses_data);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("FavList", false);
                startActivity(intent);
                loadingDialog.getDialog().cancel();
                LoginActivity.this.finish();

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (UnknownHostException e){
                Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
            }
            loadingDialog.getDialog().cancel();
        }
    }*/
}