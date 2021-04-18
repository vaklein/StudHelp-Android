package com.example.p4_group12.Interface;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class EditProfileActivity extends NavigationActivity {

    private Button edit_password;
    private MaterialButton edit_picture_button;
    private ImageView edit_picture;
    private File new_picture;
    private TextInputEditText new_name;
    private TextInputEditText new_login;
    private TextInputEditText new_description;
    private TextInputLayout new_nameField;
    private TextInputLayout new_loginField;
    private LoadingDialog loadingDialog;
    private TextInputLayout phone;
    private TextInputEditText phone_text;
    private TextInputLayout public_email;
    private TextInputEditText public_email_text;
    private TextInputLayout discord;
    private TextInputEditText discord_text;
    private TextInputLayout teams;
    private TextInputEditText teams_text;
    private API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = API.getInstance();
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_edit_profile, contentFrameLayout);
        setTitleToolbar("Profil");
        edit_password = findViewById(R.id.edit_password);

        if (GlobalVariables.getUser().getPicture() != "null") {
            edit_picture = (ImageView) findViewById(R.id.user_profile_photo);
            Picasso.get().load(BuildConfig.STORAGE_URL + GlobalVariables.getUser().getPicture()).transform(new CropCircleTransformation()).into(edit_picture);
        }
        edit_picture_button = findViewById(R.id.edit_profile_picture);
        new_name = (TextInputEditText) findViewById(R.id.name_text);
        new_name.setText(GlobalVariables.getUser().getName());
        new_login = (TextInputEditText) findViewById(R.id.login_text);
        new_login.setText(GlobalVariables.getUser().getLogin());
        new_description = (TextInputEditText) findViewById(R.id.description_text);
        if (GlobalVariables.getUser().getDescription() == "null")
            new_description.setHint(getResources().getString(R.string.descriptionHint1)
                                    + "\n" + getResources().getString(R.string.descriptionHint2)
                                    + "\n" + getResources().getString(R.string.descriptionHint3)
                                    + "\n" + getResources().getString(R.string.descriptionHint4));
        else new_description.setText(GlobalVariables.getUser().getDescription());
        new_nameField = (TextInputLayout) findViewById(R.id.name);
        new_loginField = (TextInputLayout) findViewById(R.id.teams);
        loadingDialog = new LoadingDialog(this, "Modification en cours...");

        if(GlobalVariables.getUser().getSocial_links() == null){
            GlobalVariables.getUser().setSocial_links(api.getSocialLinksOfUser(GlobalVariables.getUser()));
        }

        phone_text = (TextInputEditText) findViewById(R.id.phone_text);
        phone_text.setText(GlobalVariables.getUser().getSocial_links().getPhone());
        public_email_text = (TextInputEditText) findViewById(R.id.email_public_text);
        public_email_text.setText(GlobalVariables.getUser().getSocial_links().getPublicEmail());
        discord_text = findViewById(R.id.discord_text);
        discord_text.setText(GlobalVariables.getUser().getSocial_links().getDiscord());
        teams_text = findViewById(R.id.teams_text);
        teams_text.setText(GlobalVariables.getUser().getSocial_links().getTeams());

        edit_picture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking the permission
                /*


                 */
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });
        edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_pw = new Intent(getApplicationContext(), EditPasswordActivity.class);
                startActivity(edit_pw);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            //the image URI
            Uri selectedImage = data.getData();

            //calling the upload file method after choosing the file
            new_picture = new File(getRealPathFromURI(selectedImage));
            Picasso.get().load(new_picture).transform(new CropCircleTransformation()).into(edit_picture);
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        GlobalVariables.getUser().getSocial_links().setAllSocialLinks(phone_text.getText().toString(), public_email_text.getText().toString(), teams_text.getText().toString(), discord_text.getText().toString());
        api.updateSocialLinks(GlobalVariables.getUser());
        if (new_picture != null){
            try {
                api.setProfilePicture(GlobalVariables.getUser(), new_picture);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        new_loginField.setErrorEnabled(false);
        if (!new_name.getText().toString().isEmpty() || !new_login.getText().toString().isEmpty()) {
            String requestName = new_name.getText().toString().equals(GlobalVariables.getUser().getName()) ? null : new_name.getText().toString() ;
            String requestLogin = new_login.getText().toString().equals(GlobalVariables.getUser().getLogin()) ? null : new_login.getText().toString();
            String requestDescription = new_description.getText().toString().equals(GlobalVariables.getUser().getDescription()) ? null : new_description.getText().toString();
            Boolean apiResponse = api.editNameAndLoginAndDescription(GlobalVariables.getUser(), requestName, requestLogin, requestDescription);

            if(apiResponse == null){ // error
                Toast.makeText(EditProfileActivity.this, "Une erreur est survenue lors de la modification de votre nom, veuilliez réessayer", Toast.LENGTH_LONG).show();
            }else if(apiResponse){
                Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                if (new_name !=null) GlobalVariables.getUser().setName(new_name.getText().toString());
                if(new_login !=null) GlobalVariables.getUser().setLogin(new_login.getText().toString());
                if(new_description.getText().toString().isEmpty()) GlobalVariables.getUser().setDescription("null");
                else GlobalVariables.getUser().setDescription(new_description.getText().toString());
                startActivity(profile);
                EditProfileActivity.this.finish();
            }else{
                new_loginField.setError("Identifiant déjà utilisé");
            }
        }
        return super.onOptionsItemSelected(item);
    }
}