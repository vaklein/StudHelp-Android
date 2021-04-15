package com.example.p4_group12.DAO;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tag implements Serializable {
    private final int advertisementId;
    private final String tagType;
    private final String tagValue;

    public Tag(int id, String type, String value) {
        advertisementId = id;
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
}
