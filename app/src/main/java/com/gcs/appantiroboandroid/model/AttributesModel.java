package com.gcs.appantiroboandroid.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tbl_attributes")
public class AttributesModel {
    public AttributesModel(){

    }
    public AttributesModel(String attribute, String value){
        this.attribute =attribute;
        this.value=value;

    }
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(unique = true)
    private String attribute;

    @DatabaseField
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
