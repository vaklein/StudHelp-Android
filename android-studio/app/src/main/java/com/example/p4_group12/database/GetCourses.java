package com.example.p4_group12.database;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.Interface.adapter.CourseListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetCourses {

    private ArrayList<Course> courseArrayList;

    /**
     *
     * @param courseArrayList : ArrayList of Course object. It must be the same passed as an argument into the recyclerView
     */
    public GetCourses(ArrayList<Course> courseArrayList){
        this.courseArrayList = courseArrayList;
    }

    /**
     * urlWebService : url to the PHP file that computes the query inside the database
     * courseListAdapter : courseListAdapter object. Used to notify the adapter to update the course list when we get a response from the DB
     * **/
    public void getJSON(final String urlWebService, CourseListAdapter courseListAdapter) {

        class GetJSON extends AsyncTask<Void, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    loadIntoArrayList(s);
                    courseListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    /**
     * Here we get the JSON given by the DB and we get the courses from it in order to add them into the course list
     */
    private void loadIntoArrayList(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            Course currCourse = new Course(Integer.parseInt(obj.getString("ID")), obj.getString("CODE"), obj.getString("TEACHER"), obj.getString("NAME"), obj.getString("UNIVERSITY"));
            courseArrayList.add(currCourse);
        }
    }
}
