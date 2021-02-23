package com.example.p4_group12;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class SignupActivity extends AppCompatActivity {

    private Button signup;
    private TextInputEditText name;
    private TextInputEditText email;
    private TextInputEditText login;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;

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

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCorrectlyFil()) {

                    goToMenu();
                }
            }
        });
    }
    private boolean isCorrectlyFil() {
        // tout doit être complèter
        if (name.getText().toString().isEmpty() || email.getText().toString().isEmpty()
                || login.getText().toString().isEmpty() || password.getText().toString().isEmpty()
                || confirmPassword.getText().toString().isEmpty()) {
            //errorText.setText("erreur : veuillez complèter toute les informations");
            return false;
        }
        return true;
    }

    private void goToMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // activity done
    }

}
