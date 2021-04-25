package com.example.p4_group12.Interface;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
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
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditPasswordActivity extends NavigationActivity {

    private Button edit_password;
    private TextInputEditText previous_password;
    private TextInputEditText new_password;
    private TextInputEditText password_confirmation;
    private TextInputLayout previous_passwordField;
    private TextInputLayout new_passwordField;
    private TextInputLayout password_confirmationField;
    private static final String PASSWORD_STRENGTH = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[*@#$%!]).{8,40})";
    private LoadingDialog loadingDialog;
    private API api;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_edit_password, contentFrameLayout);
        setTitleToolbar("Profil");

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
                try {
                    previous_passwordField.setErrorEnabled(false);
                    new_passwordField.setErrorEnabled(false);
                    password_confirmationField.setErrorEnabled(false);
                    password_confirmationField.setErrorEnabled(false);
                    if (isCorrectlyFill() && isSameNewPassword() && isPasswordPowerfull()) {
                        api = API.getInstance();
                        Boolean apiRetValue = api.updatePassword(GlobalVariables.getUser(), new_password.getText().toString(), password_confirmation.getText().toString(), previous_password.getText().toString());
                        if (apiRetValue == null) {
                            Toast.makeText(EditPasswordActivity.this, "Une erreur est survenue, veuillez réessayer", Toast.LENGTH_LONG).show();
                        } else if (!apiRetValue) {
                            previous_passwordField.setError("Mauvais mot de passe");
                        } else {
                            Intent edit_profil = new Intent(getApplicationContext(), ProfileActivity.class);
                            edit_profil.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(edit_profil);
                            EditPasswordActivity.this.finish();
                        }
                    }
                }catch (UnknownHostException e){
                    Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private boolean isCorrectlyFill() {
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
}
