package com.example.p4_group12.Interface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.R;
import com.example.p4_group12.database.GetObjectFromDB;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String PREF_EMAIL = null;
    // Test values
    //private Button rootButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingDialog = new LoadingDialog(this, "Connexion en cours...");

        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String already_email = pref.getString(PREF_EMAIL, null);
        if (already_email != null) {
            loadingDialog.getDialog().show();
            ArrayList<User> onlyUser = new ArrayList<>();
            GetObjectFromDB.getJSON(BuildConfig.DB_URL + "get_user_from_email.php?UserEmail="+already_email, onlyUser, User.class);
            User user = onlyUser.get(0);
            GlobalVariables.setLogin(user.getLogin());
            GlobalVariables.setEmail(user.getEmail());
            GlobalVariables.setName(user.getName());
            //Intent edit_profil = new Intent(getApplicationContext(), ProfileActivity.class);
            //startActivity(edit_profil);
            Intent intent = new Intent(LoginActivity.this, CourseListActivity.class);
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


        /*
        // TEST ELEMENTS
        this.rootButton = findViewById(R.id.root_button);
        rootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CourseList = new Intent(getApplicationContext(), CourseListActivity.class);
                startActivity(CourseList);
            }
        });
        // TEST END
        */

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
                    new AsyncLogin().execute(login.getText().toString(),password.getText().toString());
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
    class AsyncLogin extends AsyncTask<String, Void, String> { // Il faut lancer un autre thread car une requete sur le main thread peut faire crasher l'app

        // a modifier en executor si on veut update l'app, asynctask deprecated
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.getDialog().show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(BuildConfig.DB_URL + "connect.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");  //POST request
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");//Build form answer
                bufferedWriter.write(data); //Send data
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream(); //DB answer
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
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loadingDialog.getDialog().cancel();
            try {
                JSONObject response = new JSONObject(result);
                JSONObject object = response.getJSONObject("response");
                if (object.getBoolean("error")) {
                    loginField.setError("Identifiant/Mot de passe incorrect");
                    passwordField.setError("Identifiant/Mot de passe incorrect");
                } else if (object.getBoolean("Logged")) {
                    getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                            .edit()
                            .putString(PREF_EMAIL, object.getString("email"))
                            .apply();
                    GlobalVariables.setLogin(login.getText().toString());
                    GlobalVariables.setEmail(object.getString("email"));
                    GlobalVariables.setName(object.getString("name"));
                    //Intent edit_profil = new Intent(getApplicationContext(), ProfileActivity.class);
                    //startActivity(edit_profil);
                    Intent intent = new Intent(LoginActivity.this, CourseListActivity.class);
                    intent.putExtra("FavList", false);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }else{
                    Toast.makeText(LoginActivity.this, "OOPs! Réessayer", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}