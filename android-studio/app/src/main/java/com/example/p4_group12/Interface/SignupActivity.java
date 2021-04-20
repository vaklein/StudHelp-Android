package com.example.p4_group12.Interface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

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
    private LoadingDialog loadingDialog;

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
        loginField = (TextInputLayout) findViewById(R.id.teams);
        passwordField = (TextInputLayout) findViewById(R.id.password);
        confirmPasswordField = (TextInputLayout) findViewById(R.id.confirm_password);

        loadingDialog = new LoadingDialog(this, "Inscription en cours...");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameField.setErrorEnabled(false);
                emailField.setErrorEnabled(false);
                loginField.setErrorEnabled(false);
                passwordField.setErrorEnabled(false);
                confirmPasswordField.setErrorEnabled(false);
                if (isCorrectlyFil() && isPasswordPowerfull() && isPasswordConfirmed()) {
                    User user = new User(name.getText().toString(), login.getText().toString(), email.getText().toString(), "null", "null");

                    loadingDialog.getDialog().show();
                    JSONObject apiResponse = API.registerUser(user,password.getText().toString(), confirmPassword.getText().toString());
                    // TODO create a line in SOCIAL_LINKS (might be better to do that in the back end)

                    try {
                        if(apiResponse.has("error")){ // error while trying to create the new user
                            handleError(apiResponse);
                        }else{
                            GlobalVariables.setUser(user);
                            SharedPreferences pref_date = getSharedPreferences(LoginActivity.PREF_DATE,MODE_PRIVATE); // We only get the email. We might need to get the API token or the password
                            String token_date_array = pref_date.getString(LoginActivity.PREF_TOKEN_DATE_ARRAY, "1900-01-01 00:00:00");
                            String date_courses_data = API.tokenUpdateCourses();
                            ArrayList<Course> loadCourses = API.getInstance().getCourses();
                            GlobalVariables.setCourses(loadCourses);
                            // creating a new variable for gson.
                            Gson gson = new Gson();
                            // getting data from gson and storing it in a string.
                            String json = gson.toJson(loadCourses);

                            getSharedPreferences(LoginActivity.PREF_ARRAY, MODE_PRIVATE)
                                    .edit()
                                    .putString(LoginActivity.PREF_COURSE_ARRAY_LIST, json)
                                    .apply();
                            getSharedPreferences(LoginActivity.PREF_DATE, MODE_PRIVATE)
                                    .edit()
                                    .putString(LoginActivity.PREF_TOKEN_DATE_ARRAY, date_courses_data)
                                    .apply();

                            // TODO make the connection permanent
                            Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                            intent.putExtra("FavList", false);
                            startActivity(intent);
                            loadingDialog.getDialog().cancel();
                            SignupActivity.this.finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        loadingDialog.getDialog().cancel();
                    }
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

    private void handleError(JSONObject object) throws JSONException {
        if (object.getString("error_msg").equals("EMAIL AND LOGIN ALREADY EXIST")) {
            loginField.setError("Identifiant déjà utilisé");
            emailField.setError("Email déjà utilisé");
        }else if (object.getString("error_msg").equals("LOGIN ALREADY EXISTS")) {
            loginField.setError("Identifiant déjà utilisé");
        }else if (object.getString("error_msg").equals("EMAIL ALREADY EXISTS")) {
            emailField.setError("Email déjà utilisé");
        }
    }
}
