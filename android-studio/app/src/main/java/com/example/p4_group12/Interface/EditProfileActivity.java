package com.example.p4_group12.Interface;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p4_group12.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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


public class EditProfileActivity extends AppCompatActivity {

    private Button edit_password;
    private FloatingActionButton edit_picture;
    private Button backup_profil;
    private TextInputEditText new_name;
    private TextInputEditText new_login;
    private TextInputLayout new_nameField;
    private TextInputLayout new_loginField;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        edit_password = findViewById(R.id.edit_password);
        backup_profil = findViewById(R.id.backup);
        new_name = (TextInputEditText) findViewById(R.id.name_text);
        new_login = (TextInputEditText) findViewById(R.id.login_text);
        new_nameField = (TextInputLayout) findViewById(R.id.name);
        new_loginField = (TextInputLayout) findViewById(R.id.name);
        loadingDialog = new LoadingDialog(this, "Modification en cours...");




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
                    Intent edit_pw = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(edit_pw);
                    finish();
                }
                else {
                    new_loginField.setError("Identifiant déjà prit");
                }
            }
        });
    }
    /*
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
                    GlobalVariables.setEmail(object.getString("email"));
                    GlobalVariables.setName(object.getString("name"));
                    GlobalVariables.setLogin(login.getText().toString());
                    Intent edit_profil = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(edit_profil);
                    //Intent intent = new Intent(LoginActivity.this, CourseListActivity.class);
                    //startActivity(intent);
                    LoginActivity.this.finish();
                }else{
                    Toast.makeText(LoginActivity.this, "OOPs! Réessayer", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

     */
}