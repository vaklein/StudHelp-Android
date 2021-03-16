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
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import android.util.Log;
import android.widget.Toast;

public class AdvertisementsListActivity extends NavigationActivity {
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

        advertisements.add(new Advertisement(1, "Jules", "Help me in ML please !!", "I really need some help to do the project in ML for another course. If you are good in ML please contact me I can help you in an other course if you want me to!", "Request"));

        return advertisements;
    }


    private Course currentCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_advertisments_list, contentFrameLayout);


        ArrayList<Advertisement> advertisementsList = new ArrayList<Advertisement>();
        GetObjectFromDB query = new GetObjectFromDB(advertisementsList, Advertisement .class);

        currentCourse = (Course) getIntent().getSerializableExtra("ClickedCourse");
        if(currentCourse == null) Log.d("NULLWARNING", "Course is null in AdvertisementListActivity");
        setTitleToolbar(currentCourse.getName());


        // doing the query
        GetObjectFromDB.getJSON("https://db.valentinklein.eu:8182/get_course_advertisment.php?courseID="+Integer.toString(currentCourse.getID()), advertisementsList, Advertisement.class);

        mTextView = (TextView) findViewById(R.id.text);

        advertisementRecyclerView = findViewById(R.id.advertisementRecyclerView);
        advertisementRecyclerView.setHasFixedSize(true);
        advertisementLayoutManager = new LinearLayoutManager(this);
        // advertisementListAdapter = new AdvertisementListAdapter(this.get_advertisements());
        advertisementListAdapter = new AdvertisementListAdapter(advertisementsList);

        advertisementRecyclerView.setLayoutManager(advertisementLayoutManager);
        advertisementRecyclerView.setAdapter(advertisementListAdapter);


        newAdvertisementButton = findViewById(R.id.new_advertisement_button);
        newAdvertisementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplication().getBaseContext(), clickedCourse.getName(), Toast.LENGTH_LONG).show();
                Intent newAdvertisement = new Intent(getApplicationContext(), AddAdvertisementActivity.class);
                newAdvertisement.putExtra("CurrentCourse", currentCourse);
                startActivity(newAdvertisement);
            }
        });

        advertisementListAdapter.setAdvertisementClickListener(new AdvertisementListAdapter.OnAdvertisementClickListener() {
            @Override
            public void OnAdvertisementClick(int position) {
                Advertisement clickedAdvertisement = advertisementsList.get(position);
                Intent advertisementView = new Intent(getApplicationContext(), AdvertisementView.class);
                advertisementView.putExtra("ClickedAdvertisement", clickedAdvertisement);
                startActivity(advertisementView);
            }
        });

    }
}