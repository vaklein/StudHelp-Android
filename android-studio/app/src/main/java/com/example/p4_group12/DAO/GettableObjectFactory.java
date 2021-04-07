package com.example.p4_group12.DAO;

import org.json.JSONException;
import org.json.JSONObject;

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
            return new Advertisement(Integer.parseInt(dbObject.getString("id")), dbObject.getString("user_email"), dbObject.getString("title"), dbObject.getString("description"), dbObject.getString("type"));
        }
        else if(objectClass.getCanonicalName().equals(User.class.getCanonicalName())) {
            return new User(dbObject.getString("name"), dbObject.getString("login"), dbObject.getString("email"));
        }
        else if(objectClass.getCanonicalName().equals(Social_links.class.getCanonicalName())){
            return new Social_links(dbObject.getString("discord"),dbObject.getString("teams"),dbObject.getString("facebook"));
        }
        else throw new NoSuchElementException();
    }
}
