package com.example.p4_group12.Interface;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
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
import androidx.core.app.ActivityCompat;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    Bitmap imageBitmap;
    private static final int STORAGE_PERMISSION_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = API.getInstance();
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_edit_profile, contentFrameLayout);
        setTitleToolbar("Profil");

        edit_password = findViewById(R.id.edit_password);
        edit_picture = (ImageView) findViewById(R.id.user_profile_photo);
        if (GlobalVariables.getUser().getPicture() != "null") {
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

        try{
            if(GlobalVariables.getUser().getSocial_links() == null){
                GlobalVariables.getUser().setSocial_links(api.getSocialLinksOfUser(GlobalVariables.getUser()));
            }
        } catch (UnknownHostException e){
            finish();
            Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG);
        }



        phone_text = (TextInputEditText) findViewById(R.id.phone_text);
        phone_text.setText(GlobalVariables.getUser().getSocial_links().getPhone());
        public_email_text = (TextInputEditText) findViewById(R.id.email_public_text);
        public_email_text.setText(GlobalVariables.getUser().getSocial_links().getPublicEmail());
        discord_text = findViewById(R.id.discord_text);
        discord_text.setText(GlobalVariables.getUser().getSocial_links().getDiscord());
        teams_text = findViewById(R.id.teams_text);
        teams_text.setText(GlobalVariables.getUser().getSocial_links().getTeams());
        //requestStoragePermission();
        edit_picture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, 1);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
                /* upload photo
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 99);*/
            }
        });
        edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_pw = new Intent(getApplicationContext(), EditPasswordActivity.class);
                edit_pw.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(edit_pw);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) { //retour de la prise de photo
            Bundle extras = data.getExtras();
            this.imageBitmap = (Bitmap) extras.get("data");
            try {
                Uri uri = Uri.fromFile(File.createTempFile("temp_file_name", ".jpg", this.getCacheDir()));
                OutputStream outputStream = this.getContentResolver().openOutputStream(uri);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
                Picasso.get().load(uri).transform(new CropCircleTransformation()).into(edit_picture);
            } catch (Exception e) {
                Log.e("LoadBitmapByPicasso", e.getMessage());
            }
        }
        if (resultCode == RESULT_OK && requestCode == 99) { //retour de l'upload de photo
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                imageBitmap = BitmapFactory.decodeStream(imageStream);
            } catch(IOException e){
                e.printStackTrace();
            }
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
        try {
            GlobalVariables.getUser().getSocial_links().setAllSocialLinks(phone_text.getText().toString(), public_email_text.getText().toString(), teams_text.getText().toString(), discord_text.getText().toString());
            api.updateSocialLinks(GlobalVariables.getUser());
            if (imageBitmap != null) {
                new_picture = null;
                try {
                    new_picture = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try (FileOutputStream out = new FileOutputStream(new_picture)) {
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                String requestName = new_name.getText().toString().equals(GlobalVariables.getUser().getName()) ? null : new_name.getText().toString();
                String requestLogin = new_login.getText().toString().equals(GlobalVariables.getUser().getLogin()) ? null : new_login.getText().toString();
                String requestDescription = new_description.getText().toString().equals(GlobalVariables.getUser().getDescription()) ? null : new_description.getText().toString();
                Boolean apiResponse = api.editNameAndLoginAndDescription(GlobalVariables.getUser(), requestName, requestLogin, requestDescription);

                if (apiResponse == null) { // error
                    Toast.makeText(EditProfileActivity.this, "Une erreur est survenue lors de la modification de votre nom, veuilliez réessayer", Toast.LENGTH_LONG).show();
                } else if (apiResponse) {

                    if (new_name != null)
                        GlobalVariables.getUser().setName(new_name.getText().toString());
                    if (new_login != null)
                        GlobalVariables.getUser().setLogin(new_login.getText().toString());
                    if (new_description.getText().toString().isEmpty())
                        GlobalVariables.getUser().setDescription("null");
                    else
                        GlobalVariables.getUser().setDescription(new_description.getText().toString());

                    EditProfileActivity.this.finish();
                } else {
                    new_loginField.setError("Identifiant déjà utilisé");
                }
            }
        }catch (UnknownHostException e){
            Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission acceptee, vous pouvez ajouter une synthese", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops vous venez de refuser", Toast.LENGTH_LONG).show();
            }
        }
    }
}