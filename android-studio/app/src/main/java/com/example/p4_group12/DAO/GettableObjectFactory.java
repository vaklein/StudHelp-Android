package com.example.p4_group12.DAO;

import android.util.Log;

import com.example.p4_group12.database.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.NoSuchElementException;


/**
 * A gettable object is an object that can be get from the DB.
 * Use this in order to standardize the creation after the query
 */
public class GettableObjectFactory {

    public static Object getObject(JSONObject dbObject, Class objectClass) throws JSONException {
        if(objectClass.getCanonicalName().equals(Course.class.getCanonicalName())){
            return new Course(Integer.parseInt(dbObject.getString("id")), dbObject.getString("code"), dbObject.getString("university"), dbObject.getString("name"), dbObject.getString("fac"), dbObject.getString("quadri"));
        }
        else if(objectClass.getCanonicalName().equals(Advertisement.class.getCanonicalName())){
            List<Tag> tags = API.getInstance().getAdvertisementTags(Integer.parseInt(dbObject.getString("id")));
            for (Tag tag : tags) Log.v("Jules", "Tag = " + tag.getTagValue());
            return new Advertisement(Integer.parseInt(dbObject.getString("id")), dbObject.getString("user_email"), dbObject.getString("title"), dbObject.getString("description"), tags, Integer.parseInt(dbObject.getString("course_id")));
        }
        else if(objectClass.getCanonicalName().equals(User.class.getCanonicalName())) {
            return new User(dbObject.getString("name"), dbObject.getString("login"), dbObject.getString("email"));
        }
        else if(objectClass.getCanonicalName().equals(Social_links.class.getCanonicalName())){
            return new Social_links(dbObject.getString("discord"),dbObject.getString("teams"),dbObject.getString("facebook"));
        }
        else if(objectClass.getCanonicalName().equals(Tag.class.getCanonicalName())) {
            return new Tag(Integer.parseInt(dbObject.getString("advertisement_id")), dbObject.getString("tag_type"), dbObject.getString("tag_value"));
        }
        else throw new NoSuchElementException();
    }
}
