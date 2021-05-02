package com.example.p4_group12.Interface;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.R;
import com.example.p4_group12.Interface.adapter.CourseListAdapter;
import com.example.p4_group12.database.API;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;

public class SearchActivity extends NavigationActivity{
    private RecyclerView courseRecyclerView;
    private RecyclerView.LayoutManager courseLayoutManager;
    private CourseListAdapter courseListAdapter;
    private SearchView searchView;
    private TextView mTextView;
    private TextView noCourses;
    private Switch favoriteSwitch;
    private MaterialToolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ArrayList<Course> courseList;
    private String currentCategory;
    private API api;

    private String currentQuery = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use this to set the correct layout instead of setContentView cfr NavigationActivity/drawer_layout
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_search, contentFrameLayout);
        setTitleToolbar("Cours");
        // ArrayList<Course> test = DatabaseContact.get_courses(); Request to the server

        mTextView = (TextView) findViewById(R.id.text);
        courseList = GlobalVariables.getCourses();

        // Doing all the synchronous queries
        api = API.getInstance();

        // Building the recycler view
        courseRecyclerView = findViewById(R.id.courseRecyclerView);
        courseRecyclerView.setPadding(0,0,0,20);
        searchView = findViewById(R.id.searchView);
        favoriteSwitch = findViewById(R.id.show_fav_switch);
        noCourses = findViewById(R.id.no_courses);

        try{
            currentCategory = (String) getIntent().getSerializableExtra("ClickedCategory");
            if(currentCategory == null) Log.d("NULLWARNING", "Category is null in SearchActivity");

            if (currentCategory.equals("search all")) {
                courseList = GlobalVariables.getCourses();
                setTitleToolbar("Recherche dans tous les cours");
                searchView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
            } else if (currentCategory.equals("favourites courses")) {
                courseList = api.getFavoriteCoursesOfUser(GlobalVariables.getUser());
                if (courseList.isEmpty()) {
                    noCourses.setVisibility(View.VISIBLE);
                    noCourses.setText("Vous n'avez pas encore de cours favoris.\n" +
                            "Cochez l'étoile appartenant à un cours pour l'ajouter à vos favoris,\n" +
                            "vous retrouvez alors ici l'ensemble de ces cours.");
                }
                setTitleToolbar("Recherche dans les cours favoris");
                favoriteSwitch.setVisibility(View.GONE);
            } else {
                courseList = filterFaculties(GlobalVariables.getCourses(), currentCategory);
                setTitleToolbar("Recherche dans les cours de la faculté " + currentCategory);
                searchView.setQueryHint("Code ou nom de cours dans la faculté " + currentCategory);
            }

            // Building the recycler view

            HashSet<Integer> favoritesID = api.getFavoriteCoursesIdsOfUser(GlobalVariables.getUser());
            courseListAdapter = new CourseListAdapter(courseList, favoritesID);
            courseRecyclerView.setHasFixedSize(true);
            courseLayoutManager = new LinearLayoutManager(this);

            courseRecyclerView.setLayoutManager(courseLayoutManager);

            courseRecyclerView.setAdapter(courseListAdapter);


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(true){
                        currentQuery = query;
                        courseListAdapter.getFilter().filter(query);
                    }else{
                        Toast.makeText(SearchActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                    }
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    currentQuery = newText;
                    courseListAdapter.getFilter().filter(newText);
                    return false;
                }
            });

            favoriteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean activated) {
                    if(activated) courseListAdapter.favoriteFilter(currentQuery);
                    else courseListAdapter.resetFavoriteFilter(currentQuery);//courseListAdapter.getFilter().filter(searchView.getQuery());

                    // courseList = courseListAdapter.courseList;
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
                    advertisementsListAct.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(advertisementsListAct);
                }
            });

        } catch (UnknownHostException e){
            finish();
            Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
        }

    }

    private ArrayList<Course> filterFaculties(ArrayList<Course> courses, String faculty) {
        ArrayList<Course> filtered = new ArrayList<>();
        for (Course course : courses) {
            if (course.getFaculty().equals(faculty)) {
                filtered.add(course);
            }
        }
        return filtered;
    }
}