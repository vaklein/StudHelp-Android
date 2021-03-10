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

/**
 * This class is used to do GET queries inside the database in order to show them inside a recycler view
 */

public class GetObjectFromDB {

    private ArrayList gettableObjectArrayList;
    Class objectClass;

    /**
     *
     * @param gettableObjectArrayList : ArrayList of Course object. It must be the same passed as an argument into the recyclerView
     * @param objectClass : The class of the object that we are trying to get with the query
     */
    public GetObjectFromDB(ArrayList gettableObjectArrayList, Class objectClass){
        this.gettableObjectArrayList = gettableObjectArrayList;
        this.objectClass = objectClass;
    }

    /**
     * urlWebService : url to the PHP file that computes the query inside the database
     * adapter : adapter object. Used to notify the adapter to update the course list when we get a response from the DB
     * **/
    public void getJSON(final String urlWebService, RecyclerView.Adapter adapter) {

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
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    Log.v("Gwen","instantiation Exception");
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    Log.v("Gwen","Invocation Exception");
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    Log.v("Gwen","no such elem Exception");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    Log.v("Gwen","illegal access Exception");
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
                    bufferedReader.close();
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
    private void loadIntoArrayList(String json) throws JSONException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            // Log.v("Gwen", obj.toString());
            gettableObjectArrayList.add(GettableObjectFactory.getObject(obj, this.objectClass)); // Careful, check if the class has been added inside the factory method
        }
    }
}
