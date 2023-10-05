package com.gcs.appantiroboandroid.repository;

import android.content.Context;
import android.util.Log;

import com.gcs.appantiroboandroid.config.OrmLiteSqliteOpenHelperImpl;
import com.gcs.appantiroboandroid.model.ConfigModel;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class ConfigRepository {

    private static final String TAG = "APP_ANTIROBO | ConfigRepository | ";

    private Dao<ConfigModel, Integer> configDAO;
    private OrmLiteSqliteOpenHelperImpl ormLiteSqliteOpenHelper;

    public ConfigRepository(Context context) {
        this.ormLiteSqliteOpenHelper = new OrmLiteSqliteOpenHelperImpl(context);
        if(configDAO == null){
            try {
                configDAO = this.ormLiteSqliteOpenHelper.getDao(ConfigModel.class);
            } catch (SQLException sqlException) {
                Log.e(TAG + "ConfigRepository" , "error ormLiteSqliteOpenHelper.getDao sqlException.getMessage()="+ sqlException.getMessage());
                throw new RuntimeException(sqlException);
            }
        }
    }

    public ConfigModel getValue(String attribute) {

        List<ConfigModel> lstConfigModel = null;
        try {
            lstConfigModel = this.configDAO.queryForEq("attribute",attribute);
        } catch (SQLException sqlException) {
            Log.e(TAG + "getValue" , "error configDAO.queryForEq sqlException.getMessage()="+ sqlException.getMessage());
            throw new RuntimeException(sqlException);
        }
        return lstConfigModel.get(0);
    }

    public void saveListConfigModel(List<ConfigModel> listConfigModel){

        try {
            this.configDAO.create(listConfigModel);
        } catch (SQLException sqlException) {
            Log.e(TAG + "saveListConfigModel" , "error configDAO.create sqlException.getMessage()="+ sqlException.getMessage());
            throw new RuntimeException(sqlException);
        }
    }
}
