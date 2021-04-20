package com.example.p4_group12.DAO;

import android.util.Log;

import com.example.p4_group12.database.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * A gettable object is an object that can be get from the DB.
 * Use this in order to standardize the creation after the query
 */
public class GettableObjectFactory {

    public static Object getObject(JSONObject dbObject, Class objectClass) throws JSONException, ParseException {
        if(objectClass.getCanonicalName().equals(Course.class.getCanonicalName())){
            return new Course(Integer.parseInt(dbObject.getString("id")), dbObject.getString("code"), dbObject.getString("university"), dbObject.getString("name"), dbObject.getString("fac"), dbObject.getString("quadri"));
        }
        else if(objectClass.getCanonicalName().equals(Advertisement.class.getCanonicalName())){
            List<Tag> tags = API.getInstance().getAdvertisementTags(Integer.parseInt(dbObject.getString("id")));
            List<String> pictures = API.getInstance().getAdvertisementPictures(Integer.parseInt(dbObject.getString("id")));
            //for (Tag tag : tags) Log.v("Jules", "Tag = " + tag.getTagValue());
            String goodFormatCreationDate = dbObject.getString("created_at").substring(0, 10) + " " + dbObject.getString("created_at").substring(11,19);
            String goodFormatUpdatedDate = dbObject.getString("updated_at").substring(0, 10) + " " + dbObject.getString("updated_at").substring(11,19);
            Log.v("Creation date", goodFormatCreationDate);
            Log.v("Update date", goodFormatUpdatedDate);
            return new Advertisement(Integer.parseInt(dbObject.getString("id")), dbObject.getString("user_email"), dbObject.getString("name"), dbObject.getString("title"), dbObject.getString("description"), tags, Integer.parseInt(dbObject.getString("course_id")), pictures, goodFormatCreationDate, goodFormatUpdatedDate);
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
