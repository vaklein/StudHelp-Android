package com.example.p4_group12.DAO;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tag implements Serializable {
    private final int id;
    private final int advertisementId;
    private final String tagType;
    private final String tagValue;

    /* Note that the id is set to -1 if the Tag object is created to be put in the database
    *  and the real id in the database when it is get from the api
    */
    public Tag(int id, int advertisementId, String type, String value) {
        this.id = id;
        this.advertisementId = advertisementId;
        tagType = type;
        tagValue = value;
    }

    public static List<String> getAllTagsName() {
        List<String> tagNames = new ArrayList<>();
        tagNames.add("Offre");
        tagNames.add("Demande");
        tagNames.add("Bachelier");
        tagNames.add("Master");
        tagNames.add("Livre/Syllabus");
        tagNames.add("Synthèse");
        tagNames.add("Aide");
        tagNames.add("Matériel");
        tagNames.add("Autres");
        return tagNames;
    }

    public int getAdvertisementId() {
        return advertisementId;
    }

    public String getTagType() {
        return tagType;
    }

    public String getTagValue() {
        return tagValue;
    }

    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return getTagValue();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj.getClass().getCanonicalName().equals(Tag.class.getCanonicalName())) {
            return this.getTagValue().equals(((Tag)obj).getTagValue()) &&
                    this.getAdvertisementId() == ((Tag)obj).getAdvertisementId();
        } return false;
    }
}
