package com.gcs.appantiroboandroid.repository;

import android.content.Context;
import android.util.Log;

import com.gcs.appantiroboandroid.config.OrmLiteSqliteOpenHelperImpl;
import com.gcs.appantiroboandroid.model.AttributesModel;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class AttributesRepository {

    private static final String TAG = "APP_ANTIROBO - AttributesRepository";

    private Dao<AttributesModel, Integer> configDAO;

    public AttributesRepository(Context context) {
        Log.d(TAG, "AttributesRepository| builder");
        try {
            Log.d(TAG, "AttributesRepository| builder -> create instance class OrmLiteSqliteOpenHelperImpl");
            OrmLiteSqliteOpenHelperImpl ormLiteSqliteOpenHelper = new OrmLiteSqliteOpenHelperImpl(context);
            if (configDAO == null) {
                Log.d(TAG, "AttributesRepository| builder -> create instance class Dao");
                configDAO = ormLiteSqliteOpenHelper.getDao(AttributesModel.class);
            }
        } catch (SQLException sqlException) {
            Log.e(TAG, "AttributesRepository| builder -> Error: SQLException message ="+ sqlException.getMessage());
            throw new RuntimeException("AttributesRepository| builder -> Error: SQLException message ="+ sqlException.getMessage());
        }
    }

    public AttributesModel getValue(String attribute) {
        Log.d(TAG, "getValue| get value the of attribute " + attribute );

        AttributesModel attributesModel = null;
        List<AttributesModel> lstAttributesModel = null;
        try {
            Log.d(TAG, "getValue| invoke method configDAO.queryForEq");
            lstAttributesModel = this.configDAO.queryForEq("attribute", attribute);

            Log.d(TAG, "getValue| validate exist the value of attribute");
            if (lstAttributesModel != null && !lstAttributesModel.isEmpty()) {


                attributesModel = lstAttributesModel.get(0);
            }
        } catch (SQLException sqlException) {
            Log.e(TAG, "getValue| Error: SQLException message ="+ sqlException.getMessage());
            throw new RuntimeException("getValue| Error: SQLException message ="+ sqlException.getMessage());
        }
        Log.d(TAG, "getValue| get value is " + attribute);
        return attributesModel;
    }

    public void saveListAttributesModel(List<AttributesModel> listAttributesModel) {
        try {
             listAttributesModel
                    .forEach((AttributesModel attribute)-> this.saveAttribute(attribute.getAttribute(), attribute.getValue()));

            Log.d(TAG, "saveListAttributesModel| invoke method configDAO.create");

        } catch (Exception e) {
            Log.e(TAG, "saveListAttributesModel| Error: Exception message ="+ e.getMessage());
            throw new RuntimeException("saveListConfigModel| Error: Exception message ="+ e.getMessage());

        }
    }

    public void saveAttribute(String attribute, String value) {
        try {
            Log.d(TAG, "saveAttribute| mapper string and string to AttributesModel");
            AttributesModel attributesModel = this.getValue(attribute);

            int result = 0;

            if (attributesModel == null) {
                attributesModel = new AttributesModel(attribute, value);
                result= this.configDAO.create(attributesModel);
            }else{
                result= this.configDAO.update(attributesModel);
            }
            Log.d(TAG, "saveAttribute| invoke method configDAO.create");
            if ( result == 0) {
                Log.e(TAG, "saveAttribute| Error: Attribute not save");
                throw new RuntimeException("saveAttribute| Error: Attribute not save");
            }

        } catch (SQLException sqlException) {
            Log.e(TAG, "saveAttribute| Error: SQLException message ="+ sqlException.getMessage());
            throw new RuntimeException("saveAttribute| Error: SQLException message ="+ sqlException.getMessage());
        }
    }


}
