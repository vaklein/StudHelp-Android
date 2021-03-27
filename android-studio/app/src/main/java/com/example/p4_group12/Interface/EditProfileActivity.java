package com.example.p4_group12.Interface;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

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


public class EditProfileActivity extends NavigationActivity {

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
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_edit_profile, contentFrameLayout);
        setTitleToolbar("Profil");
        edit_password = findViewById(R.id.edit_password);
        backup_profil = findViewById(R.id.backup);
        new_name = (TextInputEditText) findViewById(R.id.name_text);
        new_name.setText(GlobalVariables.getName());
        new_login = (TextInputEditText) findViewById(R.id.login_text);
        new_login.setText(GlobalVariables.getLogin());
        new_nameField = (TextInputLayout) findViewById(R.id.name);
        new_loginField = (TextInputLayout) findViewById(R.id.teams);
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
                if (!new_name.getText().toString().isEmpty() || !new_login.getText().toString().isEmpty()) {
                    new AsyncLogin().execute(String.valueOf(GlobalVariables.getEmail()), GlobalVariables.getLogin(),new_name.getText().toString(),new_login.getText().toString());
                }
            }
        });
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
                URL url = new URL("https://db.valentinklein.eu:8182/update_login_and_name.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");  //POST request
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8") + "&" +
                        URLEncoder.encode("oldlogin", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&" +
                        URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8");//Build form answer
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
                    new_loginField.setError("Identifiant déjà utilisé");
                }
                else if (object.getBoolean("effet")) {
                    Intent edit_profil = new Intent(getApplicationContext(), ProfileActivity.class);
                    if (!new_name.getText().toString().isEmpty()) GlobalVariables.setName(new_name.getText().toString());
                    if(!new_login.getText().toString().isEmpty()) GlobalVariables.setLogin(new_login.getText().toString());
                    startActivity(edit_profil);
                    EditProfileActivity.this.finish();
                }else{
                    if (!object.getBoolean("effetLogin") && ! object.getBoolean("effetName")) Toast.makeText(EditProfileActivity.this, "Une erreur est survenue, veuilliez réessayer", Toast.LENGTH_LONG).show();
                    else if (! object.getBoolean("effetLogin")) Toast.makeText(EditProfileActivity.this, "Une erreur est survenue lors de la modification de votre identifiant, veuilliez réessayer", Toast.LENGTH_LONG).show();
                    else Toast.makeText(EditProfileActivity.this, "Une erreur est survenue lors de la modification de votre nom, veuilliez réessayer", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}