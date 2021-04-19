package com.example.p4_group12.Interface;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.example.p4_group12.database.UriUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;



public class AddFileActivity extends NavigationActivity {
    private TextInputEditText fileTitleText;
    private TextInputLayout fileTitle;
    private Course course;
    private API api;
    private Button addFileButton;
    private TextView requirements;
    private TextView fileText;
    private ImageButton delFile;
    private LinearLayout fileDelLayout;
    private File file;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_add_file, contentFrameLayout);
        course = (Course) getIntent().getSerializableExtra("CurrentCourse");
        setTitleToolbar("Nouvelle synthese dans " + course.getName());
        fileTitle = findViewById(R.id.file_title);
        fileTitleText = findViewById(R.id.file_title_text);
        addFileButton = findViewById(R.id.add_file_button);
        requirements = findViewById(R.id.file_requirements);
        fileText = findViewById(R.id.file_infos);
        delFile = findViewById(R.id.del_file);
        fileDelLayout = findViewById(R.id.file_and_del);


        //Requesting storage permission
        requestStoragePermission();
        this.api = API.getInstance();

        addFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchBrowseFileIntent();
            }
        });
        delFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (file != null){
                    file = null;
                    addFileButton.setVisibility(View.VISIBLE);
                    requirements.setVisibility(View.VISIBLE);
                    fileDelLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private static final int PICK_PDF_FILE = 2;
    private void dispatchBrowseFileIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        //intent.putExtra(DocumentsContract., pickerInitialUri);

        startActivityForResult(Intent.createChooser(intent, "Selectionnez le PDF"), PICK_PDF_FILE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String fullFilePath = UriUtils.getPathFromUri(this,data.getData());
            file = new File(fullFilePath);
            if (file.length() > 10485760) {//Si >10Mo
                file = null;
                Toast.makeText(this, "Fichier trop gros, 10Mo max", Toast.LENGTH_LONG).show();
            } else {
                fileText.setText(file.getName());
                addFileButton.setVisibility(View.GONE);
                requirements.setVisibility(View.GONE);
                fileDelLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private boolean isCorrectlyFilled() {
        boolean filled = true;
        // Test the title
        if (fileTitleText.getText().toString().isEmpty()) {
            fileTitle.setError("Merci de remplir ce champs");
            filled = false;
        } else {
            fileTitle.setErrorEnabled(false);
        }
        if (file == null) filled = false;

        return filled;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (isCorrectlyFilled()) {
            try {
                api.addCourseFile(Integer.toString(course.getID()), file, fileTitleText.getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            setResult(1, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    //Requesting permission
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
