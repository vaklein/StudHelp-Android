package com.example.p4_group12.Interface;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.R;
import com.example.p4_group12.Interface.adapter.CourseListAdapter;
import com.example.p4_group12.database.DatabaseContact;
import com.example.p4_group12.database.GetObjectFromDB;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public class CourseListActivity extends NavigationActivity{
    private RecyclerView courseRecyclerView;
    private RecyclerView.LayoutManager courseLayoutManager;
    private CourseListAdapter courseListAdapter;
    private SearchView searchView;
    private TextView mTextView;
    private MaterialToolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_search, contentFrameLayout);


        // ArrayList<Course> test = DatabaseContact.get_courses(); Request to the server
        ArrayList<Course> courseList = new ArrayList<>();
        HashSet<Integer> favoritesID = new HashSet<>();
        GetObjectFromDB courseQuery = new GetObjectFromDB(courseList, Course.class);

        mTextView = (TextView) findViewById(R.id.text);



        // Building the recycler view
        courseRecyclerView = findViewById(R.id.courseRecyclerView);
        searchView = findViewById(R.id.searchView);
        courseRecyclerView.setHasFixedSize(true);
        courseLayoutManager = new LinearLayoutManager(this);
        courseListAdapter = new CourseListAdapter(courseList, favoritesID);

        courseRecyclerView.setLayoutManager(courseLayoutManager);
        courseRecyclerView.setAdapter(courseListAdapter);

        courseQuery.getJSON("https://db.valentinklein.eu:8182/get_courses.php", courseListAdapter);
        DatabaseContact.getUserFavoriteCourseIds(GlobalVariables.getEmail(), favoritesID, courseListAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(true){
                    courseListAdapter.getFilter().filter(query);
                }else{
                    Toast.makeText(CourseListActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                courseListAdapter.getFilter().filter(newText);
                return false;
            }
        });



        // Creating the onClickListener for the courses
        courseListAdapter.setCourseClickListener(new CourseListAdapter.OnCourseClickListener() {
            @Override
            public void OnCourseClick(int position) {
                Course clickedCourse = courseList.get(position);
                // Toast.makeText(getApplication().getBaseContext(), clickedCourse.getName(), Toast.LENGTH_LONG).show();
                Intent advertisementsListAct = new Intent(getApplicationContext(), AdvertisementsListActivity.class);
                advertisementsListAct.putExtra("ClickedCourse", clickedCourse);
                startActivity(advertisementsListAct);
            }
        });



    }
}