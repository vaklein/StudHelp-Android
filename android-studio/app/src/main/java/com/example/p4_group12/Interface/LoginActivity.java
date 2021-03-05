package com.example.p4_group12.Interface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.p4_group12.R;
import com.example.p4_group12.database.DatabaseContact;
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

public class LoginActivity extends AppCompatActivity {
    private Button sign_up;
    private Button connexion;
    private TextInputEditText login;
    private TextInputEditText password;
    private TextInputLayout loginField;
    private TextInputLayout passwordField;
    private LoadingDialog loadingDialog;

    // Test values
    //private Button rootButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        sign_up = findViewById(R.id.sign_up);
        connexion = findViewById(R.id.connexion);
        login = (TextInputEditText) findViewById(R.id.logintext);
        password = (TextInputEditText) findViewById(R.id.passwordtext);
        loginField = (TextInputLayout) findViewById(R.id.login);
        passwordField = (TextInputLayout) findViewById(R.id.password);

        loadingDialog = new LoadingDialog(this, "Connexion en cours...");

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
                URL url = new URL("https://db.valentinklein.eu:8182/connect.php");
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
                    GlobalVariables.setUser(object.getString("email"));
                    Intent intent = new Intent(LoginActivity.this, CourseListActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}