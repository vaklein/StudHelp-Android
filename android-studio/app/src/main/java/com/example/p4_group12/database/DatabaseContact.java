package com.example.p4_group12.database;

import android.os.AsyncTask;
import android.util.Log;

import com.example.p4_group12.DAO.Course;

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
import java.util.ArrayList;

public class DatabaseContact {

    public static void insert_user(String login,String password,String name,String email){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> { // Il faut lancer un autre thread car une requete sur le main thread peut faire crasher l'app
            @Override                                                       // a modifier en executor si on veut update l'app, asynctask deprecated
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL("https://db.valentinklein.eu:8182/insert_user.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");  //POST request
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                            URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                            URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(login, "UTF-8") + "&" +
                            URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");//Build form answer
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

        sendPostReqAsyncTask.execute(name, email);
    }

    public static void insert_course(String code,String teacher,String name,String university){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> { // Il faut lancer un autre thread car une requete sur le main thread peut faire crasher l'app
            @Override                                                       // a modifier en executor si on veut update l'app, asynctask deprecated
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL("https://db.valentinklein.eu:8182/insert_course.php");
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
    public static void insert_advertisement(int course_id,String title,String description,String user_email) {}
/* PAS FINI, le file manager ne repond plus
    public static void insert_advertisement(int course_id,String title,String description,String user_email){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> { // Il faut lancer un autre thread car une requete sur le main thread peut faire crasher l'app
            @Override                                                       // a modifier en executor si on veut update l'app, asynctask deprecated
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL("https://db.valentinklein.eu:8182/insert_advertisement.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");  //POST request
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8") + "&" +
                            URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8") + "&" +
                            URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(user_email, "UTF-8") + "&" +
                            URLEncoder.encode("course_id", "UTF-8") + "=" + URLEncoder.encode(course_id, "UTF-8");//Build form answer
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
    }*/

    public static ArrayList<Course> get_courses(){
        // from https://medium.com/@JasonCromer/android-asynctask-http-request-tutorial-6b429d833e28

        class SendGetReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params){
                String inputLine;
                String result = "test";
                try {
                    //Create a URL object holding our url
                    URL url = new URL("https://db.valentinklein.eu:8182/get_courses.php");
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

    public static boolean connect(String login,String password){
    return false;
}

}

