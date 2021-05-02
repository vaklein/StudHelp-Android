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

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.Tag;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EditAdvertisementActivity extends NavigationActivity{
    private TextInputEditText advertisementTitleText;
    private TextInputEditText advertisementDescriptionText;
    private TextInputLayout advertisementTitle;
    private TextInputLayout advertisementDescription;
    private Course course;
    private API api;
    private TextView chipGroupError;
    private Advertisement toEditAdvertisement;
    private List<String> tagStrings = new ArrayList<>();
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
        toEditAdvertisement = (Advertisement) getIntent().getSerializableExtra("toEditAdvertisement");
        setTitleToolbar("Modifier votre annonce");
        advertisementTitle = findViewById(R.id.advertisement_title);
        advertisementDescription = findViewById(R.id.advertisement_description);
        advertisementTitleText = findViewById(R.id.advertisement_title_text);
        advertisementTitleText.setText(toEditAdvertisement.getTitle());
        advertisementDescriptionText = findViewById(R.id.advertisement_description_text);
        advertisementDescriptionText.setText(toEditAdvertisement.getDescription());
        chipGroupError = findViewById(R.id.chip_group_unckecked_error);
        typeChipGroup = findViewById(R.id.add_advertisement_type_chip_group);
        cycleChipGroup = findViewById(R.id.add_advertisement_cycle_chip_group);
        objectChipGroup = findViewById(R.id.add_advertisement_object_chip_group);
        typeChipGroup.setSingleSelection(true);
        typeChipGroup.setSelectionRequired(true);
        objectChipGroup.setSelectionRequired(true);
        cycleChipGroup.setSelectionRequired(true);
        addPictureButton = findViewById(R.id.add_picture_button);
        picture = findViewById(R.id.add_advertisment_picture);
        addPictureFromGalery = findViewById(R.id.add_picturefromgalery_button);

        this.api = API.getInstance();

        try {
            if (GlobalVariables.getUser().getSocial_links() == null) {
                GlobalVariables.getUser().setSocial_links(api.getSocialLinksOfUser(GlobalVariables.getUser()));
            }
        } catch (UnknownHostException e){
            finish();
            Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG);
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
                    modifyProfile.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(modifyProfile);
                }
            });
            builder.show();
        }

        for (Tag tag : toEditAdvertisement.getTags()) {
            tagStrings.add(tag.getTagValue());
        }

        // Types
        types.add("Offre");
        types.add("Demande");
        for (String type : types) {
            Chip chip = new Chip(this);
            chip.setText(type);
            chip.setCheckable(true);
            if (tagStrings.contains(type)) {
                chip.setChecked(true);
            }
            typeChips.add(chip);
            typeChipGroup.addView(chip);
        }
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
            if (tagStrings.contains(type)) {
                chip.setChecked(true);
            }
            cycleChips.add(chip);
            cycleChipGroup.addView(chip);
        }
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
            if (tagStrings.contains(type)) {
                chip.setChecked(true);
            }
            objectChips.add(chip);
            objectChipGroup.addView(chip);
        }
        objectChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = group.findViewById(checkedId);
                if (chip != null) {
                    chip.setChecked(!chip.isChecked());
                }
            }
        });

        if(toEditAdvertisement.hasImages()) {
            Picasso.get().load(BuildConfig.STORAGE_URL + toEditAdvertisement.getImages().get(0)).into(picture);
        }
        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();

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

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            this.imageBitmap = (Bitmap) extras.get("data");
            picture.setImageBitmap(this.imageBitmap);
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
            // Update default fields of an ad


            // Get back the initial tags
            Tag initType = null;
            List<Tag> initCycles = new ArrayList<>();
            List<Tag> initObjects = new ArrayList<>();
            for (Tag tag : toEditAdvertisement.getTags()) {
                switch (tag.getTagType()) {
                    case "type":
                        initType = tag;
                        break;
                    case "cycle":
                        initCycles.add(tag);
                        break;
                    case "object":
                        initObjects.add(tag);
                        break;
                }
            }

            List<Tag> newCycles = new ArrayList<>();
            List<Tag> newObjects = new ArrayList<>();
            List<Tag> updatedTags = new ArrayList<>();

            Tag newType = new Tag(-1, toEditAdvertisement.getID(), "type", (String) ((Chip) typeChipGroup.findViewById(checkedTypeID)).getText());
            updatedTags.add(newType);
            for (int i : checkedCyclesIDs) {
                Tag t = new Tag(-1, toEditAdvertisement.getID(), "cycle", (String) ((Chip) cycleChipGroup.findViewById(i)).getText());
                newCycles.add(t);
                updatedTags.add(t);
            }
            for (int i : checkedObjectsIDs) {
                Tag t = new Tag(-1, toEditAdvertisement.getID(), "object", (String) ((Chip) objectChipGroup.findViewById(i)).getText());
                newObjects.add(t);
                updatedTags.add(t);
            }

            // Print the initial tags and the new ones
            assert initType != null;
            StringBuilder s = new StringBuilder(initType.getTagValue());
            for (Tag t : initCycles) { s.append(" ").append(t.getTagValue()); }
            for (Tag t : initObjects) { s.append(" ").append(t.getTagValue()); }
            StringBuilder s1 = new StringBuilder(newType.getTagValue());
            for (Tag t : newCycles) { s1.append(" ").append(t.getTagValue()); }
            for (Tag t : newObjects) { s1.append(" ").append(t.getTagValue()); }

            try {
                // Update type
                if (!initType.equals(newType)) {
                    api.addNewTag(newType);
                    api.removeTag(initType);
                }

                // Remove unchecked cycles
                for (Tag initCycle : initCycles) {
                    if (!newCycles.contains(initCycle)) {
                        api.removeTag(initCycle);
                    }
                }
                // Remove unchecked objects
                for (Tag initObject : initObjects) {
                    if (!newObjects.contains(initObject)) {
                        api.removeTag(initObject);
                    }
                }

                // Add new cycles
                for (Tag newCycle : newCycles) {
                    if (!initCycles.contains(newCycle)) {
                        api.addNewTag(newCycle);
                    }
                }
                // Add new objects
                for (Tag newObject : newObjects) {
                    if (!initObjects.contains(newObject)) {
                        api.addNewTag(newObject);
                    }
                }


                toEditAdvertisement.setTags(updatedTags);
                toEditAdvertisement.setTitle(advertisementTitleText.getText().toString());
                toEditAdvertisement.setDescription(advertisementDescriptionText.getText().toString());
                api.updateAdvertisement(toEditAdvertisement);

                Intent intent = new Intent();
                intent.putExtra("Advertisement", toEditAdvertisement);
                int i = 0;
                for (Tag tag : toEditAdvertisement.getTags()) {
                    intent.putExtra("tag" + i, tag);
                    i++;
                }

                if (imageBitmap != null) {
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
                        api.setAdvertisementPicture(toEditAdvertisement.getID(), test);
                    } catch (IOException | ExecutionException | InterruptedException | JSONException e) {
                        e.printStackTrace();
                    }

                }

                intent.putExtra("Number of tags", i);
                setResult(2, intent);
                finish();
            }catch(UnknownHostException e){
                Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
