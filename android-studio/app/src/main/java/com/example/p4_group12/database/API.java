package com.example.p4_group12.database;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.GettableObjectFactory;
import com.example.p4_group12.DAO.Social_links;
import com.example.p4_group12.DAO.Tag;
import com.example.p4_group12.DAO.User;
import com.example.p4_group12.Interface.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class API {

    // Class to use to do a request to the API
    // To perform a Sync request use getJSON.execute().get() to get the response from the server
    // To perform a Async request just use getJSON.execute()
    private static class SyncGetJSON extends AsyncTask<Void, Void, String> {

        private String urlWebService;
        private String data;
        private String requestType;
        private UnknownHostException connectionException;

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
        protected String doInBackground(Void... voids){
            try {
                // Sending the request
                URL url = new URL(this.urlWebService);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("Authorization","Bearer "+ key); // Setting the bearer token for the request
                httpURLConnection.setRequestMethod(this.requestType);  //setting the request type

                if(this.requestType.equals("GET") || this.requestType.equals("PUT") ) httpURLConnection.setRequestProperty("Accept", "application/json");
                if(this.requestType.equals("GET") ) httpURLConnection.setRequestProperty("Content-Type", "application/json");
                if(this.requestType.equals("POST") || this.requestType.equals("PUT") || this.requestType.equals("DELETE")) httpURLConnection.setDoOutput(true);

                if(data.length() > 0){
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    bufferedWriter.write(this.data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                }


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
            } catch (UnknownHostException e) {
                connectionException = e;
                return null;
            } catch (IOException e) {
                return null;
            }
        }

        public UnknownHostException getConnectionException(){
            return connectionException;
        }
    }

    private static class SyncSendFile extends AsyncTask<Void, Void, String> {

        private String requestURL;
        private String email;
        private File file;

        public SyncSendFile(String requestURL, String email, File file){
            this.requestURL = requestURL;
            this.email = email;
            this.file = file;
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
                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(requestURL, charset, key);
                multipart.addFormField("email", email);
                multipart.addFilePart("picture", file);
                String response = multipart.finish(); // response from server.
                return response;
            } catch (Exception e) {
                return null;
            }
        }
    }

    private static class SyncSendFileAdvertisement extends AsyncTask<Void, Void, String> {

        private String requestURL;
        private String email;
        private File file;
        private String ad_id;

        public SyncSendFileAdvertisement(String requestURL, String email, File file , String ad_id){
            this.requestURL = requestURL;
            this.email = email;
            this.file = file;
            this.ad_id=ad_id;
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
                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(requestURL, charset, key);
                multipart.addFormField("email", email);
                multipart.addFormField("advertisement_id",ad_id );
                multipart.addFilePart("picture", file);
                String response = multipart.finish(); // response from server.
                return response;
            } catch (Exception e) {
                return null;
            }
        }
    }
    private static class SyncSendFileCourse extends AsyncTask<Void, Void, String> {

        private String requestURL;
        private String email;
        private File file;
        private String course_id;
        private String title;
        private UnknownHostException connectionException;

        public SyncSendFileCourse(String requestURL, String email, File file , String course_id, String title){
            this.requestURL = requestURL;
            this.email = email;
            this.file = file;
            this.course_id = course_id;
            this.title = title;
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
                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(requestURL, charset, key);
                multipart.addFormField("email", email);
                multipart.addFormField("course_id", course_id);
                multipart.addFormField("title", title);
                multipart.addFilePart("file", file);
                String response = multipart.finish(); // response from server.
                return response;
            } catch (UnknownHostException e) {
                connectionException = e;
                return null;
            } catch (Exception e) {
                return null;
            }
        }

        public UnknownHostException getConnectionException(){
            return connectionException;
        }
    }

    private static String key;
    public static API INSTANCE = null;

    public API(String key){
        this.key = key;
    }

    public static API getInstance(){ return INSTANCE;} // The instance is created either on login or signup. getInstance shouldn't be called before doing one of these actions

    public static void saveToken(SharedPreferences sharedPreferences){
        sharedPreferences.edit()
                .putString("API_key", key)
                .apply();
    }

    private static void revokeToken(SharedPreferences sharedPreferences){
        sharedPreferences.edit()
                .putString("API_key", null)
                .apply();
        key = null;
        INSTANCE = null;
    }

    public static API setToken(SharedPreferences sharedPreferences){
        INSTANCE = new API(sharedPreferences.getString("API_key", null));
        return INSTANCE;
    }
    public static String tokenUpdateCourses() throws UnknownHostException{
        try{
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/globalvariables/course_list_update", "", "GET");
            String response = getJSON.execute().get();

            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;

            JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
            return jsonObject.getString("value");

        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject registerUser(User user, String password, String passwordConfirmation) throws UnknownHostException {
        try {
            String data = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(user.getLogin(), "UTF-8") + "&" +
                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                    URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(user.getName(), "UTF-8") + "&" +
                    URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(user.getEmail(), "UTF-8") + "&" +
                    URLEncoder.encode("password_confirmation", "UTF-8") + "=" + URLEncoder.encode(passwordConfirmation, "UTF-8");//Build form answer

            // Creating the request and getting the answer
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/register", data, "POST");
            String response = getJSON.execute().get();

            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;

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

    public static JSONObject loginUser(String login, String password) throws UnknownHostException{
        try{
            String data = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(login, "UTF-8") + "&" +
                          URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/login", data, "POST");
            String response = getJSON.execute().get();

            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;

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

    public void logoutUser(SharedPreferences sharedPreferences) throws UnknownHostException{
        SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/logout", "", "POST");
        try {
            String response = getJSON.execute().get();

            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        revokeToken(sharedPreferences);
    }

    public static void sendToken(String email, String token){
        try{
            String data = URLEncoder.encode("firebase_token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");

            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/firebase_token/" + email, data, "PUT");
            getJSON.execute();

        } catch (UnsupportedEncodingException e) {
            return;
        }

    }

    public User getUserWithEmail(String email) throws UnknownHostException{
        try{
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/user/" + email, "", "GET");
            String response = getJSON.execute().get();

            if(response == null){
                UnknownHostException e = getJSON.getConnectionException();
                if(e != null) throw e;
            }

            JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
            if(jsonObject == null) {
                return null;
            }
            else if(!jsonObject.has("error")){
                return (User) GettableObjectFactory.getObject(jsonObject, User.class);
            }
            return null;

        } catch (ExecutionException | InterruptedException | JSONException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Course> getCourses() throws UnknownHostException{
        ArrayList<Course> allCourses = new ArrayList<>();
        try{
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/course", "", "GET");
            String response = getJSON.execute().get();

            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;

            loadIntoArrayList(response, allCourses, Course.class);
        } catch (InterruptedException | ExecutionException | InstantiationException | JSONException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | ParseException e) {
            e.printStackTrace();
        }
        return allCourses;
    }

    public void addCourseFile(String course_id, File file, String title) throws ExecutionException, InterruptedException, JSONException, UnknownHostException {
        SyncSendFileCourse request = new SyncSendFileCourse(BuildConfig.DB_URL + "/course/file", GlobalVariables.getUser().getEmail(), file, course_id, title);
        String response = request.execute().get();

        UnknownHostException e;
        if(response == null && (e = request.connectionException) != null) throw e;

        JSONObject obj = new JSONObject(response);
        //user.setPicture("users/"+obj.getString("image_name"));
    }
    public ArrayList<com.example.p4_group12.DAO.File> getCourseFiles(int course_id){
        ArrayList<com.example.p4_group12.DAO.File> files = new ArrayList<>();
        try{
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/course/" + course_id + "/files", "", "GET");
            String response = getJSON.execute().get();
            loadIntoArrayList(response, files, com.example.p4_group12.DAO.File.class);
        } catch (InterruptedException | ExecutionException | InstantiationException | JSONException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | ParseException e) {
            e.printStackTrace();
        }
        return files;
    }
    public HashSet<Integer> getFavoriteCoursesIdsOfUser(User user) throws UnknownHostException{
        HashSet<Integer> favoriteIDs = new HashSet<>();
        try{
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/favorite/" + user.getEmail(), "", "GET");
            String response = getJSON.execute().get();

            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;

            // Reading the JSON and putting the IDs inside the hashet
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                favoriteIDs.add(Integer.parseInt(obj.getString("id")));
            }
        } catch (InterruptedException  | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        return favoriteIDs;
    }

    public ArrayList<Course> getFavoriteCoursesOfUser(User user) throws UnknownHostException{
        try{
            ArrayList<Course> favCourse = new ArrayList<>();

            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/favorite/" + user.getEmail(), "", "GET");
            String response = getJSON.execute().get();

            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;

            loadIntoArrayList(response, favCourse, Course.class);

            return favCourse;
        } catch (InterruptedException | ExecutionException | JSONException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addNewFavoriteToUser(User user, Course course){ // No need to handle connection errors
        try{
            String data = URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(user.getEmail(), "UTF-8") + "&" +
                    URLEncoder.encode("course_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(course.getID()), "UTF-8");

            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/favorite", data, "POST");
            getJSON.execute(); // Making the request Async
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void removeFavoriteToUser(User user, Course course){ // No need to handle connection errors
        try{
            String data = URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(user.getEmail(), "UTF-8") + "&" +
                    URLEncoder.encode("course_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(course.getID()), "UTF-8");

            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/favorite", data, "DELETE");
            getJSON.execute(); // Making the request Async
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Advertisement> getCourseAdvertisements(int course_id) throws UnknownHostException{
        ArrayList<Advertisement> allAds = new ArrayList<>();
        try{
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/course/" + course_id + "/advertisement", "", "GET");
            String response = getJSON.execute().get();

            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;

            loadIntoArrayList(response, allAds, Advertisement.class);
        } catch (InterruptedException | ExecutionException | InstantiationException | JSONException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | ParseException e) {
            e.printStackTrace();
        }
        return allAds;
    }

    public Advertisement getAdvertisment(int id) throws  UnknownHostException{
        ArrayList<Advertisement> allAds = new ArrayList<>();
        try{
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/advertisement/" + id, "", "GET");
            String response = getJSON.execute().get();

            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;

            loadIntoArrayList(response, allAds, Advertisement.class);
        } catch (InterruptedException | ExecutionException | InstantiationException | JSONException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | ParseException e) {
            e.printStackTrace();
        }
        return allAds.get(0);
    }

    public Social_links getSocialLinksOfUser(User user) throws UnknownHostException{
        try{
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/user/" + user.getEmail() + "/social_links", "", "GET");
            String response = getJSON.execute().get();

            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;

            JSONObject jsonObject = (JSONObject) new JSONArray(response).get(0);
            if(jsonObject == null) return null;
            return (Social_links) GettableObjectFactory.getObject(jsonObject, Social_links.class);
        } catch (InterruptedException | ExecutionException | JSONException | ParseException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Gwen stop

    public int addNewAdvertisement(int courseId, String title, String description, String email, String type) throws UnknownHostException{
        try{
            String data = URLEncoder.encode("course_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(courseId), "UTF-8") + "&" +
                    URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8") + "&" +
                    URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                    URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&" +
                    URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8");

            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/advertisement", data, "POST");
            String response = getJSON.execute().get(); // Making the request Sync
            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;
            JSONObject jsonObject = new JSONObject(response);
            return Integer.parseInt(jsonObject.getString("id"));
        } catch (UnsupportedEncodingException | InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int addNewTag(Tag tag) throws UnknownHostException {
        try {
            String data = URLEncoder.encode("advertisement_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(tag.getAdvertisementId()), "UTF-8") + "&" +
                    URLEncoder.encode("tag_type", "UTF-8") + "=" + URLEncoder.encode(tag.getTagType(), "UTF-8") + "&" +
                    URLEncoder.encode("tag_value", "UTF-8") + "=" + URLEncoder.encode(tag.getTagValue(), "UTF-8");

            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/advertisement/tags", data, "POST");
            String response = getJSON.execute().get(); // Making the request Async
            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;
            JSONObject jsonObject = new JSONObject(response);
            return Integer.parseInt(jsonObject.getString("id"));
        } catch (UnsupportedEncodingException | ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void removeTag(Tag tag) {
        SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/advertisement/tags/" + tag.getId(), "", "DELETE");
        getJSON.execute(); // Making the request Async
    }

    public List<Tag> getAdvertisementTags(int advertisementId) throws UnknownHostException {
        ArrayList<Tag> tags = new ArrayList<>();
        try{
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/advertisement/tags/" + advertisementId, "", "GET");
            String response = getJSON.execute().get();
            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;
            loadIntoArrayList(response, tags, Tag.class);
        } catch (InterruptedException | ExecutionException | InstantiationException | JSONException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | ParseException e) {
            e.printStackTrace();
        }
        return tags;
    }
    public ArrayList<String> getAdvertisementPictures(int advertisementId) throws UnknownHostException{
        ArrayList<String> pictures = new ArrayList<>();
        try{
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/advertisement/pictures/" + advertisementId, "", "GET");
            String response = getJSON.execute().get();
            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;

            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                pictures.add(obj.getString("picture"));
            }
        } catch (InterruptedException  | ExecutionException | JSONException  e) {
            e.printStackTrace();
        }
        return pictures;
    }

    public void deleteAdvertisment(Advertisement advertisement) throws UnknownHostException{
        SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/advertisement/" + advertisement.getID(), "", "DELETE");
        String response = null; // Making the request Async
        try {
            response = getJSON.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UnknownHostException e;
        if(response == null && (e = getJSON.connectionException) != null) throw e;
    }

    public void updateAdvertisement(Advertisement advertisement) throws UnknownHostException{
        try{
            String data = URLEncoder.encode("course_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(advertisement.getCourseID()), "UTF-8") + "&" +
                    URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(advertisement.getTitle(), "UTF-8") + "&" +
                    URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(advertisement.getEmailAddress(), "UTF-8") + "&" +
                    URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("Types are deprecated", "UTF-8") + "&" +
                    URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(advertisement.getDescription(), "UTF-8");

            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/advertisement/" + advertisement.getID(), data, "PUT");
            String response = getJSON.execute().get(); // Making the request Async
            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void setAdvertisementPicture(int ad_id, File picture) throws IOException, ExecutionException, InterruptedException, JSONException {
        SyncSendFileAdvertisement request = new SyncSendFileAdvertisement(BuildConfig.DB_URL + "/advertisement/pictures", GlobalVariables.getUser().getEmail(), picture,String.valueOf(ad_id));
        String response = request.execute().get();
        JSONObject obj = new JSONObject(response);
    }

    public ArrayList<Advertisement> getAdvertisementsOfUser(User user) throws UnknownHostException{
        ArrayList<Advertisement> allUserAds = new ArrayList<>();
        try{
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/user/" + user.getEmail() + "/advertisement", "", "GET");
            String response = getJSON.execute().get();
            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;
            loadIntoArrayList(response, allUserAds, Advertisement.class);
        } catch (InterruptedException | ExecutionException | InstantiationException | JSONException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | ParseException e) {
            e.printStackTrace();
        }
        return allUserAds;
    }

    public Boolean editNameAndLoginAndDescription(User user, String newName, String newLogin, String newDescription) throws UnknownHostException{
        try{
            String data = "";
            // Probably a better way to do it but can't find it
            if(newLogin != null){
                data =  URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(newLogin, "UTF-8");
            }
            if(newName != null){
                if (data == "") data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(newName, "UTF-8");
                else data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(newName, "UTF-8");
            }
            if(newDescription != null) {
                if (data == "") data = URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(newDescription, "UTF-8");
                else data += "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(newDescription, "UTF-8");
            }
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/user/profile/" + user.getEmail(), data, "PUT");
            String response = getJSON.execute().get();
            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject != null && !jsonObject.has("error");
        } catch (UnsupportedEncodingException | ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void setProfilePicture(User user, File picture) throws IOException, ExecutionException, InterruptedException, JSONException {
        SyncSendFile request = new SyncSendFile(BuildConfig.DB_URL + "/user/picture", user.getEmail(), picture);
        String response = request.execute().get();
        JSONObject obj = new JSONObject(response);
        user.setPicture("users/"+obj.getString("image_name"));
    }
    public void updateSocialLinks(User user) throws UnknownHostException{
        try{
            String data = URLEncoder.encode("discord", "UTF-8") + "=" + URLEncoder.encode(user.getSocial_links().getDiscord(), "UTF-8") + "&" +
                    URLEncoder.encode("teams", "UTF-8") + "=" + URLEncoder.encode(user.getSocial_links().getTeams(), "UTF-8") + "&" +
                    URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(user.getSocial_links().getPhone(), "UTF-8") + "&" +
                    URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(user.getSocial_links().getPublicEmail(), "UTF-8");

            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/social_links/" + user.getEmail(), data, "PUT");
            String response = getJSON.execute().get(); // Making the request Async
            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public Boolean updatePassword(User user, String newPassword, String passwordConfirmation, String oldPasswordGiven)throws UnknownHostException{
        try{
            String data = URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(newPassword, "UTF-8") + "&" +
                    URLEncoder.encode("password_confirmation", "UTF-8") + "=" + URLEncoder.encode(passwordConfirmation, "UTF-8") + "&" +
                    URLEncoder.encode("old_password", "UTF-8") + "=" + URLEncoder.encode(oldPasswordGiven, "UTF-8");

            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/user/password/" + user.getEmail(), data, "PUT");
            String response = getJSON.execute().get();
            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;
            JSONObject jsonObject = new JSONObject(response);

            return !jsonObject.has("errors");
        } catch (UnsupportedEncodingException | ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Advertisement> getBookmarksOfUser(User user)throws UnknownHostException{
        try{
            ArrayList<Advertisement> out = new ArrayList<>();
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/user/" + user.getEmail() + "/bookmarks", "", "GET");
            String response = getJSON.execute().get();
            UnknownHostException e;
            if(response == null && (e = getJSON.connectionException) != null) throw e;
            loadIntoArrayList(response, out, Advertisement.class);
            return  out;
        } catch (JSONException | IllegalAccessException | InstantiationException | ParseException | InvocationTargetException | ExecutionException | NoSuchMethodException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashSet<Integer> getBookmarksIdsOfUser(User user)throws UnknownHostException{
        try{
            HashSet<Integer> out = new HashSet<>();
            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/user/" + user.getEmail() + "/bookmarks", "", "GET");

            String result = getJSON.execute().get();
            UnknownHostException e;
            if(result == null && (e = getJSON.connectionException) != null) throw e;
            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                out.add(Integer.parseInt(obj.getString("id")));
            }
            return out;
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addBookmarkForUser(User user, Advertisement advertisement){
        try{
            String data = URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(user.getEmail(), "UTF-8") + "&" +
                    URLEncoder.encode("advertisement_id", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(advertisement.getID()), "UTF-8");

            SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/bookmarks", data, "POST");
            getJSON.execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void removeBookmarkForUser(User user, Advertisement advertisement){
        SyncGetJSON getJSON = new SyncGetJSON(BuildConfig.DB_URL + "/user/" + user.getEmail() + "/bookmarks/" + Integer.toString(advertisement.getID()), "", "DELETE");
        getJSON.execute();
    }

    /**
     * Here we get the JSON given by the DB and we get the courses from it in order to add them into the course list
     */
    public static void loadIntoArrayList(String json, ArrayList gettableObjectArrayList, Class objectClass) throws JSONException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ParseException {
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            gettableObjectArrayList.add(GettableObjectFactory.getObject(obj, objectClass)); // Careful, check if the class has been added inside the factory method
        }
    }
}
