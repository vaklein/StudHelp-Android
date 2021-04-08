package com.example.p4_group12.Interface;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.Social_links;
import com.example.p4_group12.R;
import com.example.p4_group12.database.DatabaseContact;
import com.example.p4_group12.database.GetObjectFromDB;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AddAdvertisementActivity extends NavigationActivity {
    private TextInputEditText advertisementTitleText;
    private TextInputEditText advertisementDescriptionText;
    private TextInputLayout advertisementTitle;
    private TextInputLayout advertisementDescription;
    private Button submitAdvertisement;
    private Advertisement currentAdvertisement;
    private Course course;
    private TextView chipGroupError;

    private ChipGroup typeChipGroup;
    List<String> types = new ArrayList<>();
    List<Chip> typeChips = new ArrayList<>();

    private ChipGroup cycleChipGroup;
    List<String> cycles = new ArrayList<>();
    List<Chip> cycleChips = new ArrayList<>();

    private ChipGroup objectChipGroup;
    List<String> objects = new ArrayList<>();
    List<Chip> objectChips = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_add_advertisement, contentFrameLayout);
        course = (Course) getIntent().getSerializableExtra("CurrentCourse");
        setTitleToolbar("Nouvelle annonce dans " + course.getName());
        advertisementTitle = findViewById(R.id.advertisement_title);
        advertisementDescription = findViewById(R.id.advertisement_description);
        advertisementTitleText = findViewById(R.id.advertisement_title_text);
        advertisementDescriptionText = findViewById(R.id.advertisement_description_text);
        submitAdvertisement = findViewById(R.id.add_advertisement_button);
        currentAdvertisement = (Advertisement) getIntent().getSerializableExtra("ClickedAdvertisement");

        chipGroupError = findViewById(R.id.chip_group_unckecked_error);
        typeChipGroup = findViewById(R.id.add_advertisement_type_chip_group);
        cycleChipGroup = findViewById(R.id.add_advertisement_cycle_chip_group);
        objectChipGroup = findViewById(R.id.add_advertisement_object_chip_group);

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
            builder.setMessage("Vous n'avez pas encore ajouté de réseau social. Vous pouvez ajouter des annonces mais les autres utilisateurs ne sauront pas vous contacter." +
                    " Pour ajouter des réseaux sociaux, sélectionnez \"Ajouter des réseaux\".");
            builder.setPositiveButton("J'ai compris !", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Ajouter des réseaux  ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent modifyProfile = new Intent(getApplicationContext(),EditProfileActivity.class);
                    startActivity(modifyProfile);
                }
            });
            builder.show();
        }

        // Types
        types.add("Offre");
        types.add("Demande");
        for (String type : types) {
            Chip chip = new Chip(this);
            chip.setText(type);
            chip.setCheckable(true);
            typeChips.add(chip);
            typeChipGroup.addView(chip);
        }
        typeChipGroup.setSingleSelection(true);
        typeChipGroup.setSelectionRequired(true);
        typeChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = group.findViewById(checkedId);
                if (chip != null) {
                    chip.setChecked(!chip.isChecked());
                }
            }
        });

        // Cycles
        cycles.add("Bachelier");
        cycles.add("Master");
        for (String type : cycles) {
            Chip chip = new Chip(this);
            chip.setText(type);
            chip.setCheckable(true);
            cycleChips.add(chip);
            cycleChipGroup.addView(chip);
        }
        cycleChipGroup.setSelectionRequired(true);
        cycleChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = group.findViewById(checkedId);
                if (chip != null) {
                    chip.setChecked(!chip.isChecked());
                }
            }
        });

        // Object
        objects.add("Livre/Syllabus");
        objects.add("Synthèse");
        objects.add("Aide");
        objects.add("Matériel");
        objects.add("Autres");
        for (String type : objects) {
            Chip chip = new Chip(this);
            chip.setText(type);
            chip.setCheckable(true);
            objectChips.add(chip);
            objectChipGroup.addView(chip);
        }
        objectChipGroup.setSelectionRequired(true);
        objectChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = group.findViewById(checkedId);
                if (chip != null) {
                    chip.setChecked(!chip.isChecked());
                }
            }
        });

        submitAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int checkedTypeID = typeChipGroup.getCheckedChipId();

                List<Integer> checkedCyclesIDs = cycleChipGroup.getCheckedChipIds();

                List<Integer> checkedObjectsIDs = objectChipGroup.getCheckedChipIds();

                if (isCorrectlyFilled(checkedTypeID, checkedCyclesIDs, checkedObjectsIDs)) {

                    // TODO : move the next line but for the moment it ensures to still be able to add the type
                    String type = (String) ((Chip) typeChipGroup.findViewById(checkedTypeID)).getText();

                    DatabaseContact.insert_advertisement(course.getID(), advertisementTitleText.getText().toString(),
                            formatFieldForSqlPostRequest(advertisementDescriptionText.getText().toString()), GlobalVariables.getEmail(), type);
                    // TODO : Should insert the tags here
                    /*
                    for (int i : checkedCyclesIDs) {
                        // This is the text of the checked chips
                        (String) ((Chip) cycleChipGroup.findViewById(i)).getText();
                    }
                    for (int i : checkedObjectsIDs){
                        // This is the text of the checked chips
                        (String) ((Chip) objectChipGroup.findViewById(i)).getText();
                    }
                    */

                    Intent intent = new Intent();
                    setResult(1, intent);
                    finish();
                }
            }
        });

    }

    @SuppressLint("ResourceAsColor")
    private boolean isCorrectlyFilled(int checkedType, List<Integer> checkedCycles, List<Integer> checkedObjects) {
        boolean filled = true;
        // Test the title
        if (advertisementTitleText.getText().toString().isEmpty()) {
            advertisementTitle.setError("Merci de remplir ce champs");
            filled = false;
        } else {
            advertisementTitle.setErrorEnabled(false);
        }
        // Test the description
        if (advertisementDescriptionText.getText().toString().isEmpty()) {
            advertisementDescription.setError("Merci de remplir ce champs");
            filled = false;
        } else {
            advertisementDescription.setErrorEnabled(false);
        }
        // Test the tags
        boolean typeIsOk = true;
        boolean cycleIsOk = true;
        boolean objectIsOk = true;
        if (checkedType == View.NO_ID) {
            filled = false;
            typeIsOk = false;
        }
        if (checkedCycles.isEmpty()) {
            filled = false;
            cycleIsOk = false;
        }
        if (checkedObjects.isEmpty()) {
            filled = false;
            objectIsOk = false;
        }
        if (!typeIsOk || !cycleIsOk || !objectIsOk) {
            chipGroupError.setVisibility(View.VISIBLE);
            chipGroupError.setText("Merci de sélectionner au moins une pastille par catégorie");
        }
        if (typeIsOk && cycleIsOk && objectIsOk) {
            chipGroupError.setVisibility(View.GONE);
        }
        return filled;
    }

    private static String formatFieldForSqlPostRequest(String field){
        // We might want to replace other char if we find other bugs
        return field.replace("'", "''");
    }
}
