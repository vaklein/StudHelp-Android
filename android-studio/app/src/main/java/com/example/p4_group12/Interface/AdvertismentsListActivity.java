package com.example.p4_group12.Interface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.Interface.adapter.AdvertisementListAdapter;
import com.example.p4_group12.Interface.adapter.CourseListAdapter;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.R;
import com.example.p4_group12.database.GetObjectFromDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import android.util.Log;
import android.widget.Toast;

public class AdvertismentsListActivity extends AppCompatActivity {
    private RecyclerView advertisementRecyclerView;
    private RecyclerView.LayoutManager advertisementLayoutManager;
    private AdvertisementListAdapter advertisementListAdapter;
    private TextView mTextView;
    private FloatingActionButton newAdvertisementButton;


    /**
     * Hardcoded implementation to get a list of courses
     * */
    public static ArrayList<Advertisement> get_advertisements() {
        ArrayList<Advertisement> advertisements = new ArrayList<>();

        advertisements.add(new Advertisement(1, "Jules", "Help me in ML please !!", "I really need some help to do the project in ML for another course. If you are good in ML please contact me I can help you in an other course if you want me to!"));

        return advertisements;
    }


    private Course currentCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisments_list);

        ArrayList<Advertisement> advertisementsList = new ArrayList<Advertisement>();
        GetObjectFromDB query = new GetObjectFromDB(advertisementsList, Advertisement .class);

        currentCourse = (Course) getIntent().getSerializableExtra("ClickedCourse");
        if(currentCourse == null) Log.d("NULLWARNING", "Course is null in AdvertismentListActivity");
        setTitle(currentCourse.getName());



        mTextView = (TextView) findViewById(R.id.text);

        advertisementRecyclerView = findViewById(R.id.advertisementRecyclerView);
        advertisementRecyclerView.setHasFixedSize(true);
        advertisementLayoutManager = new LinearLayoutManager(this);
        // advertisementListAdapter = new AdvertisementListAdapter(this.get_advertisements());
        advertisementListAdapter = new AdvertisementListAdapter(advertisementsList);

        advertisementRecyclerView.setLayoutManager(advertisementLayoutManager);
        advertisementRecyclerView.setAdapter(advertisementListAdapter);

        // Setting the query
        query.getJSON("https://db.valentinklein.eu:8182/get_course_advertisment.php?courseID="+Integer.toString(currentCourse.getID()), advertisementListAdapter);

        newAdvertisementButton = findViewById(R.id.new_advertisement_button);
        newAdvertisementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewAdvertisement = new Intent(getApplicationContext(), AddAdvertisementActivity.class);
                startActivity(NewAdvertisement);
            }
        });



    }
}