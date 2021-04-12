package com.example.p4_group12.DAO;

import android.os.Parcelable;

import java.io.Serializable;

public class Tag implements Serializable {
    private final int advertisementId;
    private final String tagType;
    private final String tagValue;

    public Tag(int id, String type, String value) {
        advertisementId = id;
        tagType = type;
        tagValue = value;
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
