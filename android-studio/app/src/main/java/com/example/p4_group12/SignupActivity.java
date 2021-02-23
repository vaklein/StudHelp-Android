package com.example.p4_group12;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p4_group12.database.DatabaseContact;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
                if (isCorrectlyFil() && isPasswordConfirmed()) {
                    DatabaseContact.insert_user(login.getText().toString(), password.getText().toString(), name.getText().toString(), email.getText().toString().toLowerCase());
                    goToLogin();
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
    private boolean isPasswordConfirmed() {
        if (password.getText().toString().equals(confirmPassword.getText().toString())){
            return true;
        }else{
            confirmPasswordField.setError("Mot de passe différent");
            return false;
        }
    }
    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // activity done
    }

}
