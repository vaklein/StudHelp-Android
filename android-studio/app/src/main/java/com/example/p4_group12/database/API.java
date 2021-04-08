package com.example.p4_group12.database;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.GettableObjectFactory;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.Interface.LoginActivity;
import com.example.p4_group12.Interface.SignupActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

public class API {

    // Class to use to do a Sync request to the API
    private static class SyncGetJSON extends AsyncTask<Void, Void, String> {

        private String urlWebService;
        private String data;
        private String requestType;

        public SyncGetJSON(String urlWebService, String data, String requestType){
            this.urlWebService = urlWebService;
            this.data = data;
            this.requestType = requestType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Sending the request
                URL url = new URL(urlWebService);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("Authorization","Bearer "+ key); // Setting the bearer token for the request
                httpURLConnection.setRequestMethod(this.requestType);  //setting the request type

                if(this.requestType == "GET") httpURLConnection.setRequestProperty("Accept", "application/json");
                if(this.requestType == "GET") httpURLConnection.setRequestProperty("Content-Type", "application/json");
                if(this.requestType == "POST" || this.requestType == "PUT" || this.requestType == "DELETE") httpURLConnection.setDoOutput(true);

                if(data.length() > 0){
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    bufferedWriter.write(this.data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                }

                // Log.d("Gwen", Integer.toString(httpURLConnection.getResponseCode())); printing the response code of the http request

                // Getting the answer from th DB
                InputStream IS = httpURLConnection.getResponseCode() / 100 == 2 ? httpURLConnection.getInputStream() : httpURLConnection.getErrorStream(); //DB answer
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS));
                String json;
                StringBuilder result = new StringBuilder();
                while ((json = bufferedReader.readLine()) != null) {
                    result.append(json + "\n");
                }
                IS.close();
                httpURLConnection.disconnect();
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private static String key;
    private static API INSTANCE = null;

    private API(String key){
        this.key = key;
    }

    public static API getInstance(){ return INSTANCE;} // The instance is created either on login or signup. getInstance shouldn't be called before doing one of these actions


    public static JSONObject registerUser(User user, String passwordConfirmation){
        try {
            String data = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(user.getLogin(), "UTF-8") + "&" +
                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(user.getPassword(), "UTF-8") + "&" +
                    URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(user.getName(), "UTF-8") + "&" +
                    URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(user.getEmail(), "UTF-8") + "&" +
                    URLEncoder.encode("password_confirmation", "UTF-8") + "=" + URLEncoder.encode(passwordConfirmation, "UTF-8");//Build form answer

            // Creating the request and getting the answer
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/register", data, "POST");
            String response = getJSON.execute().get();

            // Handling the answer
            JSONObject jsonObject = new JSONObject(response);
            if(!jsonObject.has("error")){ // If no error while creating the new user, create an insance of the API object with the key of the user
                INSTANCE = new API(jsonObject.getString("token"));
                return (JSONObject) jsonObject.get("user");
            }
            return jsonObject;
        } catch (UnsupportedEncodingException | ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject loginUser(String login, String password){
        try{
            String data = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(login, "UTF-8") + "&" +
                          URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/login", data, "POST");
            String response = getJSON.execute().get();

            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject == null) return null;
            else if(!jsonObject.has("message")){ // If no error while creating the new user, create an insance of the API object with the key of the user
                INSTANCE = new API(jsonObject.getString("token"));
                return (JSONObject) jsonObject.get("user");
            }
            return jsonObject;

        } catch (UnsupportedEncodingException | ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Course> getCourses(){
        ArrayList<Course> allCourses = new ArrayList<>();
        try{
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/course", "", "GET");
            String response = getJSON.execute().get();
            loadIntoArrayList(response, allCourses, Course.class);
        } catch (InterruptedException  | ExecutionException | InstantiationException | JSONException | NoSuchMethodException | IllegalAccessException | InvocationTargetException  e) {
            e.printStackTrace();
        }
        return allCourses;
    }

    public HashSet<Integer> getFavoriteCoursesIdsOfUser(User user){
        HashSet<Integer> favoriteIDs = new HashSet<>();
        try{
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/favorite/" + user.getEmail(), "", "GET");
            String response = getJSON.execute().get();

            // Reading the JSON and putting the IDs inside the hashet
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                favoriteIDs.add(Integer.parseInt(obj.getString("course_id")));
            }
        } catch (InterruptedException  | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        return favoriteIDs;
    }

    public void addNewFavoriteToUser(User user, Course course){
        try{
            String data = URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(user.getEmail(), "UTF-8") + "&" +
                    URLEncoder.encode("course_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(course.getID()), "UTF-8");

            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/favorite", data, "POST");
            getJSON.execute(); // Making the request Async
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void removeFavoriteToUser(User user, Course course){
        try{
            String data = URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(user.getEmail(), "UTF-8") + "&" +
                    URLEncoder.encode("course_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(course.getID()), "UTF-8");

            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/favorite", data, "DELETE");
            getJSON.execute(); // Making the request Async
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Here we get the JSON given by the DB and we get the courses from it in order to add them into the course list
     */
    private static void loadIntoArrayList(String json, ArrayList gettableObjectArrayList, Class objectClass) throws JSONException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            // Log.d("Gwen", obj.toString());
            gettableObjectArrayList.add(GettableObjectFactory.getObject(obj, objectClass)); // Careful, check if the class has been added inside the factory method
        }
    }
}
