package com.example.p4_group12.database;

import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.BuildConfig;
import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.Interface.GlobalVariables;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;

public class DatabaseContact {
    public static void insert_course(String code,String teacher,String name,String university){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> { // Il faut lancer un autre thread car une requete sur le main thread peut faire crasher l'app
            @Override                                                       // a modifier en executor si on veut update l'app, asynctask deprecated
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(BuildConfig.DB_URL + "insert_course.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");  //POST request
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                            URLEncoder.encode("code", "UTF-8") + "=" + URLEncoder.encode(code, "UTF-8") + "&" +
                            URLEncoder.encode("teacher", "UTF-8") + "=" + URLEncoder.encode(teacher, "UTF-8") + "&" +
                            URLEncoder.encode("university", "UTF-8") + "=" + URLEncoder.encode(university, "UTF-8");//Build form answer
                    bufferedWriter.write(data); //Send data
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection.getInputStream(); //DB answer
                    IS.close();
                    httpURLConnection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);
                //Print txt when POST request done
                //Toast.makeText(LoginActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute();
    }

    /**
     * Temporary version of the method
     */
    public static void insert_advertisement(int course_id,String title,String description,String user_email, String type){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> { // Il faut lancer un autre thread car une requete sur le main thread peut faire crasher l'app
            @Override                                                       // a modifier en executor si on veut update l'app, asynctask deprecated
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(BuildConfig.DB_URL + "insert_advertisement.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");  //POST request
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8") + "&" +
                            URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8") + "&" +
                            URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(user_email, "UTF-8") + "&" +
                            URLEncoder.encode("course_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(course_id), "UTF-8") + "&" +
                            URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");//Build form answer
                    Log.d("title",title);
                    Log.d("descr",description);
                    Log.d("email",user_email);
                    Log.d("id",String.valueOf(course_id));
                    bufferedWriter.write(data); //Send data
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection.getInputStream(); //DB answer

                    // Used to print what the php script is printing
                    /* ByteArrayOutputStream result = new ByteArrayOutputStream();
                    byte[] buffer = new byte[500];
                    int length;
                    while ((length = IS.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                    }

                    Log.d("Gwen", result.toString()); */

                    IS.close();
                    httpURLConnection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);
                Log.i("result",result);
                //Print txt when POST request done
                //Toast.makeText(LoginActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute();
    }

    public static ArrayList<Course> get_courses(){
        // from https://medium.com/@JasonCromer/android-asynctask-http-request-tutorial-6b429d833e28

        class SendGetReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params){
                String inputLine;
                String result = "test";
                try {
                    //Create a URL object holding our url
                    URL url = new URL(BuildConfig.DB_URL + "get_courses.php");
                    //Create a connection
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    //Set methods and timeouts
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(15000);
                    httpURLConnection.setConnectTimeout(15000);

                    //Connect to our url
                    httpURLConnection.connect();

                    //Create a new InputStreamReader
                    InputStreamReader streamReader = new
                            InputStreamReader(httpURLConnection.getInputStream());
                    //Create a new buffered reader and String Builder
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    //Check if the line we are reading is not null
                    while((inputLine = reader.readLine()) != null){
                        stringBuilder.append(inputLine);
                    }
                    //Close our InputStream and Buffered reader
                    reader.close();
                    streamReader.close();
                    //Set our result equal to our stringBuilder
                    result = stringBuilder.toString();
                    Log.v("Gwen", result);

                } catch (MalformedURLException e) {
                    Log.v("Gwen", "catch1");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.v("Gwen", e.toString());
                    e.printStackTrace();
                }

                return result; // https://stackoverflow.com/questions/46738864/building-a-json-object-from-sql-query-results-in-php to encode the result of the query
            }
        }

        SendGetReqAsyncTask sendPostReqAsyncTask = new SendGetReqAsyncTask();

        sendPostReqAsyncTask.execute();
        return new ArrayList<Course>();
    }

    /**
     * returns a hashset with all the id of the favorite courses of the user described by the email given in the args
     **/
    public static void getUserFavoriteCourseIds(String userEmail,  HashSet<Integer> favoriteIDs, RecyclerView.Adapter adapter) {
        // Doing the query in a sync way. The recycler viewer needs the favorites to load the course when he gets it

        class GetJSON extends AsyncTask<Void, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    readJSON(s, favoriteIDs);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(BuildConfig.DB_URL + "get_user_favorite.php?UserEmail=" + userEmail);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    bufferedReader.close();
                    return sb.toString().trim();
                } catch (Exception e) {
                    Log.e("QUERY", e.toString());
                    e.printStackTrace();
                }
                return null;
            }
        }

        GetJSON getJSON = new GetJSON();
        getJSON.execute();

    }

    private static void readJSON(String json, HashSet<Integer> favoriteIDs) throws  JSONException{
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            // Log.v("Gwen", obj.toString());
            favoriteIDs.add(Integer.parseInt(obj.getString("COURSE_ID")));
        }
    }

    public static void insert_favorite(String userEmail,Integer courseID){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> { // Il faut lancer un autre thread car une requete sur le main thread peut faire crasher l'app
            @Override                                                       // a modifier en executor si on veut update l'app, asynctask deprecated
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(BuildConfig.DB_URL + "insert_new_favorite.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");  //POST request
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("userEmail", "UTF-8") + "=" + URLEncoder.encode(userEmail, "UTF-8") + "&" +
                            URLEncoder.encode("courseID", "UTF-8") + "=" + URLEncoder.encode(courseID.toString(), "UTF-8");//Build form answer
                    bufferedWriter.write(data); //Send data
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection.getInputStream(); //DB answer
                    IS.close();
                    httpURLConnection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);
                //Print txt when POST request done
                //Toast.makeText(LoginActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute();
    }

    public static void delete_favorite(String userEmail,Integer courseID){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> { // Il faut lancer un autre thread car une requete sur le main thread peut faire crasher l'app
            @Override                                                       // a modifier en executor si on veut update l'app, asynctask deprecated
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(BuildConfig.DB_URL + "delete_favorite.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");  //POST request
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("userEmail", "UTF-8") + "=" + URLEncoder.encode(userEmail, "UTF-8") + "&" +
                            URLEncoder.encode("courseID", "UTF-8") + "=" + URLEncoder.encode(courseID.toString(), "UTF-8");//Build form answer
                    bufferedWriter.write(data); //Send data
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection.getInputStream(); //DB answer
                    IS.close();
                    httpURLConnection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);
                //Print txt when POST request done
                //Toast.makeText(LoginActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute();
    }

    public static void delete_advertisement(Integer advertisementID){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> { // Il faut lancer un autre thread car une requete sur le main thread peut faire crasher l'app
            @Override                                                       // a modifier en executor si on veut update l'app, asynctask deprecated
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(BuildConfig.DB_URL + "delete_advertisement.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");  //POST request
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data =
                            URLEncoder.encode("advertisementID", "UTF-8") + "=" + URLEncoder.encode(advertisementID.toString(), "UTF-8");//Build form answer
                    bufferedWriter.write(data); //Send data
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection.getInputStream(); //DB answer
                    IS.close();
                    httpURLConnection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);
                //Print txt when POST request done
                //Toast.makeText(LoginActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute();
    }

    public static void insert_social_links(String user_email,String discord,String teams,String facebook){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> { // Il faut lancer un autre thread car une requete sur le main thread peut faire crasher l'app
            @Override                                                       // a modifier en executor si on veut update l'app, asynctask deprecated
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(BuildConfig.DB_URL + "insert_social_links.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");  //POST request
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(user_email, "UTF-8") + "&" +
                            URLEncoder.encode("discord", "UTF-8") + "=" + URLEncoder.encode(discord, "UTF-8") + "&" +
                            URLEncoder.encode("teams", "UTF-8") + "=" + URLEncoder.encode(teams, "UTF-8") + "&" +
                            URLEncoder.encode("facebook", "UTF-8") + "=" + URLEncoder.encode(facebook, "UTF-8");//Build form answer
                    bufferedWriter.write(data); //Send data
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection.getInputStream(); //DB answer

                         InputStreamReader isr = new InputStreamReader(IS,
                                 StandardCharsets.UTF_8);
                         BufferedReader br = new BufferedReader(isr);

                        br.lines().forEach(line -> Log.i("lucas",line));
                    IS.close();
                    httpURLConnection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);
                Log.i("lucas",result);
                //Print txt when POST request done
                //Toast.makeText(LoginActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute();
    }

    public static void update_social_links(String discord,String teams,String facebook) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
        /*    @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog.getDialog().show();
            }*/

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(BuildConfig.DB_URL + "update_social_links.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");  //POST request
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(GlobalVariables.getEmail(), "UTF-8") + "&" +
                            URLEncoder.encode("discord", "UTF-8") + "=" + URLEncoder.encode(discord, "UTF-8") + "&" +
                            URLEncoder.encode("teams", "UTF-8") + "=" + URLEncoder.encode(teams, "UTF-8") + "&" +
                            URLEncoder.encode("facebook", "UTF-8") + "=" + URLEncoder.encode(facebook, "UTF-8");//Build form answer
                    bufferedWriter.write(data); //Send data
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection.getInputStream(); //DB answer
                    InputStreamReader isr = new InputStreamReader(IS,
                            StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(isr);

                    br.lines().forEach(line -> Log.i("lucas",line));
                    IS.close();
                    httpURLConnection.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return "data ok";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

            }

        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    public static ArrayList<String> get_social_links(String email) {
        ArrayList<String> array = new ArrayList<>();
        class AsyncLogin extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    Log.i("lucas","enter");
                    URL url = new URL(BuildConfig.DB_URL + "get_social_links.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");  //POST request
                    Log.i("lucas","enter");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    Log.i("lucas","enter");
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    Log.i("lucas","enterbis");
                    String data = URLEncoder.encode("UserEmail", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") ;//Build form answer
                    bufferedWriter.write(data); //Send data
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection.getInputStream(); //DB answer
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

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                try {
                    JSONObject response = new JSONObject(result);
                    JSONObject object = response.getJSONObject("response");
                    array.add(object.getString("DISCORD"));
                    array.add(object.getString("TEAMS"));
                    array.add(object.getString("FACEBOOK"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        return array;
    }


}