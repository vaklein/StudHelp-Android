package com.example.p4_group12.Interface;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.Tag;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class AddAdvertisementActivity extends NavigationActivity {
    private TextInputEditText advertisementTitleText;
    private TextInputEditText advertisementDescriptionText;
    private TextInputLayout advertisementTitle;
    private TextInputLayout advertisementDescription;
    private Advertisement currentAdvertisement;
    private Course course;
    private API api;
    private TextView chipGroupError;
    private Button addPictureButton;
    Bitmap imageBitmap;
    ImageView picture;
    private Button addPictureFromGalery;

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
        currentAdvertisement = (Advertisement) getIntent().getSerializableExtra("ClickedAdvertisement");
        chipGroupError = findViewById(R.id.chip_group_unckecked_error);
        typeChipGroup = findViewById(R.id.add_advertisement_type_chip_group);
        cycleChipGroup = findViewById(R.id.add_advertisement_cycle_chip_group);
        objectChipGroup = findViewById(R.id.add_advertisement_object_chip_group);
        addPictureButton = findViewById(R.id.add_picture_button);
        picture = findViewById(R.id.add_advertisment_picture);
        addPictureFromGalery = findViewById(R.id.add_picturefromgalery_button);
        this.api = API.getInstance();

        if (GlobalVariables.getUser().getSocial_links() == null) {
            GlobalVariables.getUser().setSocial_links(api.getSocialLinksOfUser(GlobalVariables.getUser()));
        }
        if (!GlobalVariables.getUser().hasASocialNetwork()){
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
                    startActivityForResult(modifyProfile,2);
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


        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, 1);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }

            }
        });

        addPictureFromGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");

                startActivityForResult(photoPickerIntent, 99);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) { //retour de la prise de photo
            Bundle extras = data.getExtras();
            this.imageBitmap = (Bitmap) extras.get("data");
            picture.setImageBitmap(this.imageBitmap);
        }
        if(requestCode == 2) { // retour de la modification des reseaux pcq on en avait pas
            Intent advertisement = new Intent(getApplicationContext(), AddAdvertisementActivity.class);
            advertisement.putExtra("CurrentCourse", course);
            advertisement.putExtra("ClickedAdvertisement", currentAdvertisement);
            startActivity(advertisement);
            finish();
        }
        if (resultCode == RESULT_OK && requestCode == 99) { //retour de l'upload de photo
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                imageBitmap = BitmapFactory.decodeStream(imageStream);
                picture.setImageBitmap(imageBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int checkedTypeID = typeChipGroup.getCheckedChipId();

        List<Integer> checkedCyclesIDs = cycleChipGroup.getCheckedChipIds();

        List<Integer> checkedObjectsIDs = objectChipGroup.getCheckedChipIds();

        if (isCorrectlyFilled(checkedTypeID, checkedCyclesIDs, checkedObjectsIDs)) {
            int advertisementId = api.addNewAdvertisement(course.getID(), advertisementTitleText.getText().toString(), advertisementDescriptionText.getText().toString(), GlobalVariables.getUser().getEmail(), "Types are deprecated");
            api.addNewTag(new Tag(-1, advertisementId, "type", (String) ((Chip) typeChipGroup.findViewById(checkedTypeID)).getText()));
            for (int i : checkedCyclesIDs) {
                api.addNewTag(new Tag(-1, advertisementId, "cycle", (String) ((Chip) cycleChipGroup.findViewById(i)).getText()));
            }
            for (int i : checkedObjectsIDs){
                api.addNewTag(new Tag(-1, advertisementId, "object", (String) ((Chip) objectChipGroup.findViewById(i)).getText()));
            }

            if(imageBitmap != null) {
                File test = null;
                try {
                    test = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try (FileOutputStream out = new FileOutputStream(test)) {
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    api.setAdvertisementPicture(advertisementId,test);
                } catch (IOException | ExecutionException | InterruptedException | JSONException e) {
                    e.printStackTrace();
                }
            }

            Intent intent = new Intent();
            setResult(1, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
