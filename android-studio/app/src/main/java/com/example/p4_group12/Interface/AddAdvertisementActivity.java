package com.example.p4_group12.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.R;
import com.example.p4_group12.database.DatabaseContact;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;


public class AddAdvertisementActivity extends AppCompatActivity {
    private TextInputEditText advertisementTitleText;
    private TextInputEditText advertisementDescriptionText;
    private TextInputLayout advertisementTitle;
    private TextInputLayout advertisementDescription;
    private Button submitAdvertisement;
    private TextInputLayout advertisementTypePicker;
    private AutoCompleteTextView advertisementTypePickerTextView;
    private Course course;
    private ArrayList<String> types = new ArrayList<>(2);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advertisement);

        advertisementTitle = findViewById(R.id.advertisement_title);
        advertisementDescription = findViewById(R.id.advertisement_description);
        advertisementTitleText = findViewById(R.id.advertisement_title_text);
        advertisementDescriptionText = findViewById(R.id.advertisement_description_text);
        submitAdvertisement = findViewById(R.id.add_advertisement_button);
        advertisementTypePicker = findViewById(R.id.advertisement_type_picker);
        advertisementTypePickerTextView = findViewById(R.id.advertisement_type_picker_textview);

        types.add("Request");
        types.add("Offer");

        // Every step of dropdown menu done with : https://www.youtube.com/watch?v=Bdm-pR3Nqkw
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.type_picker_item, types);
        advertisementTypePickerTextView.setAdapter(arrayAdapter);

        submitAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Jules", String.valueOf(isCorrectlyFilled()));
                if (isCorrectlyFilled()) {
                    course = (Course) getIntent().getSerializableExtra("CurrentCourse");
                    DatabaseContact.insert_advertisement(course.getID(), advertisementTitleText.getText().toString(), advertisementDescriptionText.getText().toString(), GlobalVariables.getEmail());
                    Intent advertisementList = new Intent(getApplicationContext(), AdvertisementsListActivity.class);
                    advertisementList.putExtra("ClickedCourse", course);
                    startActivity(advertisementList);
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
}
