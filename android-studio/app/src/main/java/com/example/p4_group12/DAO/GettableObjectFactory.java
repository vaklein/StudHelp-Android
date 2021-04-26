package com.example.p4_group12.DAO;

import android.util.Log;

import com.example.p4_group12.database.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * A gettable object is an object that can be get from the DB.
 * Use this in order to standardize the creation after the query
 */
public class GettableObjectFactory {

    public static Object getObject(JSONObject dbObject, Class objectClass) throws JSONException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(objectClass.getCanonicalName().equals(Course.class.getCanonicalName())){
            return new Course(Integer.parseInt(dbObject.getString("id")), dbObject.getString("code"), dbObject.getString("university"), dbObject.getString("name"), dbObject.getString("fac"), dbObject.getString("quadri"));
        }
        else if(objectClass.getCanonicalName().equals(Advertisement.class.getCanonicalName())){
            ArrayList<Tag> tags = new ArrayList<>();
            Log.v("respJSON", "tags are " + dbObject.getJSONArray("tags").toString());
            API.loadIntoArrayList(dbObject.getJSONArray("tags").toString(), tags, Tag.class);
            // Deal with the only picture but the app is implemented in a way that we can show multiple pictures
            List<String> pictures = new ArrayList<>();
            pictures.add(dbObject.getString("picture"));
            if(pictures.get(0).equals("null")) pictures.remove(0); // sinon on pense qu'il y a une image car liste non-vide
            //for (Tag tag : tags) Log.v("Jules", "Tag = " + tag.getTagValue());
            String goodFormatCreationDate = dbObject.getString("created_at").substring(0, 10) + " " + dbObject.getString("created_at").substring(11,19);
            String goodFormatUpdatedDate = dbObject.getString("updated_at").substring(0, 10) + " " + dbObject.getString("updated_at").substring(11,19);
            Log.v("Creation date", goodFormatCreationDate);
            Log.v("Update date", goodFormatUpdatedDate);
            Advertisement ad = new Advertisement(Integer.parseInt(dbObject.getString("id")), dbObject.getString("user_email"), dbObject.getString("name"), dbObject.getString("title"), dbObject.getString("description"), tags, Integer.parseInt(dbObject.getString("course_id")), pictures, goodFormatCreationDate, goodFormatUpdatedDate);
            ad.setCourseName(dbObject.getString("course_name"));
            return ad;
        }
        else if(objectClass.getCanonicalName().equals(User.class.getCanonicalName())) {
            return new User(dbObject.getString("name"), dbObject.getString("login"), dbObject.getString("email"), dbObject.getString("picture"), dbObject.getString("description"));
        }
        else if(objectClass.getCanonicalName().equals(Social_links.class.getCanonicalName())){
            return new Social_links(dbObject.getString("phone"),dbObject.getString("email"),dbObject.getString("teams"),dbObject.getString("discord"));
        }
        else if(objectClass.getCanonicalName().equals(Tag.class.getCanonicalName())) {
            return new Tag(Integer.parseInt(dbObject.getString("id")), Integer.parseInt(dbObject.getString("advertisement_id")), dbObject.getString("tag_type"), dbObject.getString("tag_value"));
        }
        else if(objectClass.getCanonicalName().equals(File.class.getCanonicalName())) {
            String goodFormatCreationDate = dbObject.getString("created_at").substring(0, 10) + " " + dbObject.getString("created_at").substring(11,19);
            return new File(Integer.parseInt(dbObject.getString("id")), Integer.parseInt(dbObject.getString("course_id")), dbObject.getString("file"), dbObject.getString("title"), dbObject.getString("email"), goodFormatCreationDate);
        }
        else throw new NoSuchElementException();
    }
}
