package com.example.p4_group12.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.R;
import com.example.p4_group12.database.DatabaseContact;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class AddAdvertisementActivity extends AppCompatActivity {
    private TextInputEditText advertisementTitleText;
    private TextInputEditText advertisementDescriptionText;
    private TextInputLayout advertisementTitle;
    private TextInputLayout advertisementDescription;
    private Button submitAdvertisement;
    private Course course;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advertisement);

        advertisementTitle = findViewById(R.id.advertisement_title);
        advertisementDescription = findViewById(R.id.advertisement_description);
        advertisementTitleText = findViewById(R.id.advertisement_title_text);
        advertisementDescriptionText = findViewById(R.id.advertisement_description_text);

        this.submitAdvertisement = findViewById(R.id.add_advertisement_button);
        submitAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                course = (Course) getIntent().getSerializableExtra("CurrentCourse");
                Log.v("Julius", Integer.toString(course.getID()));
                DatabaseContact.insert_advertisement(course.getID(), advertisementTitleText.getText().toString(), advertisementDescriptionText.getText().toString(), GlobalVariables.getUser());
                Intent advertisementList = new Intent(getApplicationContext(), AdvertisementsListActivity.class);
                advertisementList.putExtra("ClickedCourse", course);
                startActivity(advertisementList);
            }
        });

    }
}
