package com.example.p4_group12.Interface;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.R;
import com.example.p4_group12.Interface.adapter.CourseListAdapter;
import com.example.p4_group12.database.GetObjectFromDB;

import java.util.ArrayList;

public class CourseListActivity extends AppCompatActivity {
    private RecyclerView courseRecyclerView;
    private RecyclerView.LayoutManager courseLayoutManager;
    private CourseListAdapter courseListAdapter;
    private TextView mTextView;

    /**
     * Hardcoded implementation to get a list of courses
     * */
    public static ArrayList<Course> get_courses() {
        ArrayList<Course> courses = new ArrayList<>();

        courses.add(new Course(2,"LINGI2261","Yves Deville","Artificial intelligence","UCLouvain"));
        courses.add(new Course(3,"LSINF2990","Promotor","TFE","UCLouvain"));
        courses.add(new Course(4,"LTECO2100","Hans Ausloos","Lectures bibliques","UCLouvain"));
        courses.add(new Course(5,"LTECO2300","Marcela Lobo Bustamante","Questions éthiques","UCLouvain"));
        courses.add(new Course(6,"LTECO2200","Régis Burnet, Dominique Martens","Questions humaines fondamentales","UCLouvain"));
        courses.add(new Course(7,"LFSA2200","John Cultiaux, Eline Jammaers","Organisation et ressources humaines","UCLouvain"));
        courses.add(new Course(8,"LINGI2349","Etienne Riviere, Ramin Sadre","Netwroking and security seminar","UCLouvain"));
        courses.add(new Course(9,"LINGI2359","Axel Legay","Software engineering and programming systems seminar","UCLouvain"));
        courses.add(new Course(10,"LINGI2369","Pierre Dupont, Siegfried Nijssen","Artificial intelligence and machine learning seminar","UCLouvain"));
        courses.add(new Course(11,"LINGI2132","Nicolas Laurent","Languages and translators","UCLouvain"));
        courses.add(new Course(12,"LINGI2172","Siegfried Nijssen","Databases","UCLouvain"));
        courses.add(new Course(13,"LINGI2241","Ramin Sadre","Architecture and performance of computer systems","UCLouvain"));
        courses.add(new Course(14,"LINGI2262","Pierre Dupont","Machine Learning : classification and evaluation","UCLouvain"));
        courses.add(new Course(15,"LINGI2255","Axel Legay","Software engineering project","UCLouvain"));

        return courses;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        // ArrayList<Course> test = DatabaseContact.get_courses(); Request to the server
        ArrayList<Course> courseList = new ArrayList<>();
        GetObjectFromDB query = new GetObjectFromDB(courseList, Course.class);


        mTextView = (TextView) findViewById(R.id.text);



        // Building the recycler view
        courseRecyclerView = findViewById(R.id.courseRecyclerView);
        courseRecyclerView.setHasFixedSize(true);
        courseLayoutManager = new LinearLayoutManager(this);
        courseListAdapter = new CourseListAdapter(courseList);

        courseRecyclerView.setLayoutManager(courseLayoutManager);
        courseRecyclerView.setAdapter(courseListAdapter);

        query.getJSON("https://db.valentinklein.eu:8182/get_courses.php", courseListAdapter);

        // Creating the onClickListener for the courses
        courseListAdapter.setCourseClickListener(new CourseListAdapter.OnCourseClickListener() {
            @Override
            public void OnCourseClick(int position) {
                Course clickedCourse = courseList.get(position);
                // Toast.makeText(getApplication().getBaseContext(), clickedCourse.getName(), Toast.LENGTH_LONG).show();
                Intent advertismentsListAct = new Intent(getApplicationContext(), AdvertisementsListActivity.class);
                advertismentsListAct.putExtra("ClickedCourse", clickedCourse);
                startActivity(advertismentsListAct);
            }
        });
    }
}