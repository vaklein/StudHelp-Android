package com.example.p4_group12.Interface;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p4_group12.R;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditPasswordActivity extends AppCompatActivity {

    private Button edit_password;
    private TextInputEditText previous_password;
    private TextInputEditText new_password;
    private TextInputEditText password_confirmation;
    private TextInputLayout previous_passwordField;
    private TextInputLayout new_passwordField;
    private TextInputLayout password_confirmationField;
    private static final String PASSWORD_STRENGTH = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[*@#$%!]).{8,40})";
    private LoadingDialog loadingDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);


        edit_password = findViewById(R.id.backup);
        previous_password = (TextInputEditText) findViewById(R.id.previous_password_text);
        new_password = (TextInputEditText) findViewById(R.id.new_password_text);
        password_confirmation = (TextInputEditText) findViewById(R.id.password_confirmation_text);
        previous_passwordField = (TextInputLayout) findViewById(R.id.previous_password);
        new_passwordField = (TextInputLayout) findViewById(R.id.new_password);
        password_confirmationField = (TextInputLayout) findViewById(R.id.password_confirmation);

        loadingDialog = new LoadingDialog(this, "Modification en cours...");
        edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previous_passwordField.setErrorEnabled(false);
                new_passwordField.setErrorEnabled(false);
                password_confirmationField.setErrorEnabled(false);
                password_confirmationField.setErrorEnabled(false);
                if (isCorrectlyFil() && isSameNewPassword() && isPasswordPowerfull()) {
                    new AsyncLogin().execute(String.valueOf(GlobalVariables.getEmail()), previous_password.getText().toString(), new_password.getText().toString());
                }
            }
        });

    }
    private boolean isCorrectlyFil() {
        // tout doit être complèté
        boolean filled = true;
        if (previous_password.getText().toString().isEmpty()) {
            filled = false;
            previous_passwordField.setError("Champs obligatoire");
        }
        if (new_password.getText().toString().isEmpty()) {
            filled = false;
            new_passwordField.setError("Champs obligatoire");
        }
        if (password_confirmation.getText().toString().isEmpty()) {
            filled = false;
            password_confirmationField.setError("Champs obligatoire");
        }
        return filled;
    }
    private boolean isSameNewPassword() {
        boolean same = true;
        if (! new_password.getText().toString().equals(password_confirmation.getText().toString())){
            same = false;
            new_passwordField.setError("Les deux mots de passe doivent être identiques");
            password_confirmationField.setError("Les deux mots de passe doivent être identiques");
        }
        return same;
    }
    private boolean isPasswordPowerfull(){
        Pattern passwordPattern = Pattern.compile(PASSWORD_STRENGTH);
        Matcher passwordMatcher = passwordPattern.matcher(new_password.getText().toString());
        if (!passwordMatcher.matches()){
            new_passwordField.setError("Votre mot de passe doit contenir au moins 8 caractères dont au moins : un chiffre, une majuscule, une minuscule et un caractère spéciale (@, #, !, ...)");
            return false;
        }
        return true;
    }
    class AsyncLogin extends AsyncTask<String, Void, String> { // Il faut lancer un autre thread car une requete sur le main thread peut faire crasher l'app
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.getDialog().show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://db.valentinklein.eu:8182/update_password.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");  //POST request
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8") + "&" +
                        URLEncoder.encode("oldpassword", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8")+ "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");
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
                    previous_passwordField.setError("Mauvais mot de passe");
                }
                else if (object.getBoolean("effet")) {
                    Intent edit_profil = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(edit_profil);
                    EditPasswordActivity.this.finish();
                }else{
                    Toast.makeText(EditPasswordActivity.this, "Une erreur est survenue, veuilliez réessayer", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
