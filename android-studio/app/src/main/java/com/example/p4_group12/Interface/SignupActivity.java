package com.example.p4_group12.Interface;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private Button signup;
    private TextInputEditText name;
    private TextInputEditText email;
    private TextInputEditText login;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private TextInputLayout nameField;
    private TextInputLayout emailField;
    private TextInputLayout loginField;
    private TextInputLayout passwordField;
    private TextInputLayout confirmPasswordField;
    private static final String PASSWORD_STRENGTH = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[*@#$%!]).{8,40})";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = (Button) findViewById(R.id.signup);
        name = (TextInputEditText) findViewById(R.id.name_text);
        email = (TextInputEditText) findViewById(R.id.email_text);
        login = (TextInputEditText) findViewById(R.id.login_text);
        password = (TextInputEditText) findViewById(R.id.password_text);
        confirmPassword = (TextInputEditText) findViewById(R.id.confirm_password_text);
        nameField = (TextInputLayout) findViewById(R.id.name);
        emailField = (TextInputLayout) findViewById(R.id.email);
        loginField = (TextInputLayout) findViewById(R.id.login);
        passwordField = (TextInputLayout) findViewById(R.id.password);
        confirmPasswordField = (TextInputLayout) findViewById(R.id.confirm_password);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameField.setErrorEnabled(false);
                emailField.setErrorEnabled(false);
                loginField.setErrorEnabled(false);
                passwordField.setErrorEnabled(false);
                confirmPasswordField.setErrorEnabled(false);
                if (isCorrectlyFil() && isPasswordPowerfull() &&isPasswordConfirmed()) {
                    new AsyncSignUp().execute(name.getText().toString(), email.getText().toString().toLowerCase(), login.getText().toString(), password.getText().toString());
                }
            }
        });
    }
    private boolean isCorrectlyFil() {
        // tout doit être complèter
        boolean filled = true;
        if (name.getText().toString().isEmpty()){
            filled = false;
            nameField.setError("Champs obligatoire");
        }
        if (email.getText().toString().isEmpty()){
            filled = false;
            emailField.setError("Champs obligatoire");
        }
        if (login.getText().toString().isEmpty()){
            filled = false;
            loginField.setError("Champs obligatoire");
        }
        if (password.getText().toString().isEmpty()){
            filled = false;
            passwordField.setError("Champs obligatoire");
        }
        if (confirmPassword.getText().toString().isEmpty()){
            filled = false;
            confirmPasswordField.setError("Champs obligatoire");
        }
        return filled;
    }

    private boolean isPasswordPowerfull(){
        Pattern passwordPattern = Pattern.compile(PASSWORD_STRENGTH);
        Matcher passwordMatcher = passwordPattern.matcher(password.getText().toString());
        if (!passwordMatcher.matches()){
            passwordField.setError("Votre mot de passe doit contenir au moins 8 caractères dont au moins : un chiffre, une majuscule, une minuscule et un caractère spéciale (@, #, !, ...)");
            return false;
        }
        return true;
    }

    private boolean isPasswordConfirmed() {
        if (password.getText().toString().equals(confirmPassword.getText().toString())){
            return true;
        }else{
            confirmPasswordField.setError("Mot de passe différent");
            return false;
        }
    }

    class AsyncSignUp extends AsyncTask<String, Void, String> { // Il faut lancer un autre thread car une requete sur le main thread peut faire crasher l'app

        // a modifier en executor si on veut update l'app, asynctask deprecated
        ProgressDialog pdLoading = new ProgressDialog(SignupActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://db.valentinklein.eu:8182/insert_user.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");  //POST request
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                        URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8");//Build form answer
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
            pdLoading.dismiss();
            try {
                JSONObject response = new JSONObject(result);
                JSONObject object = response.getJSONObject("response");
                if (object.getBoolean("error")) {
                    if (object.getString("error_msg").equals("EMAIL AND LOGIN ALREADY EXIST")) {
                        loginField.setError("Identifiant déjà utilisé");
                        emailField.setError("Email déjà utilisé");
                    }else if (object.getString("error_msg").equals("LOGIN ALREADY EXISTS")) {
                        loginField.setError("Identifiant déjà utilisé");
                    }else if (object.getString("error_msg").equals("EMAIL ALREADY EXISTS")) {
                        emailField.setError("Email déjà utilisé");
                    }else{
                        Toast.makeText(SignupActivity.this, "OOPs! Réessayer", Toast.LENGTH_LONG).show();
                    }
                } else if (object.getBoolean("created")) {
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SignupActivity.this.finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
