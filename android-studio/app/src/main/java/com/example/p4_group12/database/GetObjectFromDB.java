package com.example.p4_group12.database;

import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.DAO.GettableObjectFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * This class is used to do GET queries inside the database in order to show them inside a recycler view
 */

public class GetObjectFromDB {

    private ArrayList gettableObjectArrayList;
    Class objectClass;

    /**
     *

     */
    public GetObjectFromDB(ArrayList gettableObjectArrayList, Class objectClass){
        this.gettableObjectArrayList = gettableObjectArrayList;
        this.objectClass = objectClass;
    }

    /**
     * query made in a synchronous way
     * @param urlWebService : url to the PHP file that computes the query inside the database
     * @param gettableObjectArrayList : ArrayList of Course object. It must be the same passed as an argument into the recyclerView
     * @param objectClass : The class of the object that we are trying to get with the query (this object needs to be defined in GettableObjectFactory)
     * **/
    public static void getJSON(final String urlWebService, ArrayList gettableObjectArrayList, Class objectClass) {

        class GetJSON extends AsyncTask<Void, Void, String> {

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
                    URL url = new URL(urlWebService);
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
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        try {
            loadIntoArrayList(getJSON.execute().get(), gettableObjectArrayList, objectClass);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Gwen", "Json exception");
        } catch (InstantiationException e) {
            Log.d("Gwen","instantiation Exception");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Log.d("Gwen","Invocation Exception");
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            Log.d("Gwen","no such elem Exception");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.d("Gwen","illegal access Exception");
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
