package com.gcs.appantiroboandroid.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DatabaseTable(tableName = "tbl_config")
public class ConfigModel {
    public ConfigModel(){

    }
    public ConfigModel(String attribute, String value){
        this.attribute =attribute;
        this.value=value;

    }
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String attribute;

    @DatabaseField
    private String value;
}
