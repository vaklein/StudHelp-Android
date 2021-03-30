package com.example.p4_group12.Interface;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.Social_links;
import com.example.p4_group12.R;
import com.example.p4_group12.database.DatabaseContact;
import com.example.p4_group12.database.GetObjectFromDB;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;


public class AddAdvertisementActivity extends NavigationActivity {
    private TextInputEditText advertisementTitleText;
    private TextInputEditText advertisementDescriptionText;
    private TextInputLayout advertisementTitle;
    private TextInputLayout advertisementDescription;
    private Button submitAdvertisement;
    private TextInputLayout advertisementTypePicker;
    private AutoCompleteTextView advertisementTypePickerTextView;
    private Advertisement currentAdvertisement;
    private Course course;
    private ArrayList<String> types = new ArrayList<>(2);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_add_advertisement, contentFrameLayout);
        course = (Course) getIntent().getSerializableExtra("CurrentCourse");
        setTitleToolbar(course.getName());
        advertisementTitle = findViewById(R.id.advertisement_title);
        advertisementDescription = findViewById(R.id.advertisement_description);
        advertisementTitleText = findViewById(R.id.advertisement_title_text);
        advertisementDescriptionText = findViewById(R.id.advertisement_description_text);
        submitAdvertisement = findViewById(R.id.add_advertisement_button);
        advertisementTypePicker = findViewById(R.id.advertisement_type_picker);
        advertisementTypePickerTextView = findViewById(R.id.advertisement_type_picker_textview);
        currentAdvertisement = (Advertisement) getIntent().getSerializableExtra("ClickedAdvertisement");

        if (!GlobalVariables.getSocialNetwokCharged()) {
            ArrayList<Social_links> reseaux = new ArrayList<>();
            GetObjectFromDB.getJSON(BuildConfig.DB_URL + "get_social_links.php?UserEmail=" + GlobalVariables.getEmail(), reseaux, Social_links.class);
            Social_links s = reseaux.get(0);
            GlobalVariables.setDiscord(s.getDiscord());
            GlobalVariables.setTeams(s.getTeams());
            GlobalVariables.setFacebook(s.getFacebook());
            GlobalVariables.setSocialNetwokCharged(true);
        }
        if (!GlobalVariables.havaASocialNetwork()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Attention");
            builder.setMessage("Vous n'avez pas encore ajouté de réseau social. Vous pouvez ajouter des annonces mais les autres utilisateurs ne sauront pas vous contacter. Pour ajouter des réseaux sociaux, allez dans profil > modifier, ensuite ajoutez vos réseaux sociaux et confirmez vos modifications");
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                    dialog.dismiss();
                }
            });
            builder.show();
        }

        types.add("Offre");
        types.add("Demande");

        // Every step of dropdown menu done with : https://www.youtube.com/watch?v=Bdm-pR3Nqkw
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.type_picker_item, types);
        advertisementTypePickerTextView.setAdapter(arrayAdapter);

        submitAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.v("Jules", String.valueOf(isCorrectlyFilled()));
                if (isCorrectlyFilled()) {
                    Intent intent = new Intent();
                    DatabaseContact.insert_advertisement(course.getID(), advertisementTitleText.getText().toString(),
                            formatFieldForSqlPostRequest(advertisementDescriptionText.getText().toString()), GlobalVariables.getEmail(), advertisementTypePickerTextView.getText().toString());
                    setResult(1, intent);
                    finish();
                }
            }
        });

    }

    private boolean isCorrectlyFilled() {
        boolean filled = true;
        // Test the title
        if (advertisementTitleText.getText().toString().isEmpty()) {
            advertisementTitle.setError("Please fill this field");
            filled = false;
        } else {
            advertisementTitle.setErrorEnabled(false);
        }
        // Test the description
        if (advertisementDescriptionText.getText().toString().isEmpty()) {
            advertisementDescription.setError("Please fill this field");
            filled = false;
        } else {
            advertisementDescription.setErrorEnabled(false);
        }
        // Test the type
        if (advertisementTypePickerTextView.getText().toString().isEmpty() ||
                !types.contains(advertisementTypePickerTextView.getText().toString())) {
            advertisementTypePicker.setError("Please choose only a correct value");
            filled = false;
        } else {
            advertisementTypePicker.setErrorEnabled(false);
        }
        return filled;
    }

    private static String formatFieldForSqlPostRequest(String field){
        // We might want to replace other char if we find other bugs
        return field.replace("'", "''");
    }
}
