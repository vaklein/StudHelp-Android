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
            return new Course(Integer.parseInt(dbObject.getString("ID")), dbObject.getString("CODE"), dbObject.getString("TEACHER"), dbObject.getString("NAME"), dbObject.getString("NAME"));
        }
        else if(objectClass.getCanonicalName().equals(Advertisement.class.getCanonicalName())){
            return new Advertisement(Integer.parseInt(dbObject.getString("ID")), dbObject.getString("USER_EMAIL"), dbObject.getString("TITLE"), dbObject.getString("DESCRIPTION"));
        }
        else throw new NoSuchElementException();
    }
}
