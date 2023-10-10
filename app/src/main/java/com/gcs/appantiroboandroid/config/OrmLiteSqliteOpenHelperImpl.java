package com.gcs.appantiroboandroid.config;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gcs.appantiroboandroid.model.AttributesModel;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class OrmLiteSqliteOpenHelperImpl  extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "APP_ANTIROBO - OrmLiteSqliteOpenHelperImpl";
    private static final String DATABASE_NAME = "db_antirobo.db";
    private static final int DATABASE_VERSION = 1;

    public OrmLiteSqliteOpenHelperImpl(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "OrmLiteSqliteOpenHelperImpl| builder");
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.d(TAG, "onCreate| Create Table tbl_attributes");
            TableUtils.createTable(connectionSource, AttributesModel.class);
        } catch (SQLException e) {
            Log.e(TAG, "onCreate| Error: SQLException message ="+ e.getMessage());
            throw new RuntimeException("onCreate| Error: SQLException message ="+ e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.d(TAG, "onUpgrade| validate exist table tbl_attributes");
            if(checkTableExist(database,"item_user")){
                Log.d(TAG, "onUpgrade| drop table tbl_attributes");
                TableUtils.dropTable(connectionSource, AttributesModel.class,false);
            }
            Log.d(TAG, "onUpgrade| Invoke table onCreate");
            onCreate(database,connectionSource);
        }catch (SQLException e){
            Log.e(TAG, "onUpgrade| Error: SQLException message ="+ e.getMessage());
            throw new RuntimeException("onUpgrade| Error: SQLException message ="+ e.getMessage());
        }
    }

    private boolean checkTableExist(SQLiteDatabase database, String tableName){
        Cursor c = null;
        try {
            Log.d(TAG, "checkTableExist| execute query validate table ="+tableName);
            c = database.query(tableName, null,null,null,null,null,null);
            c.close();
            Log.d(TAG, "checkTableExist| table exist "+ tableName);
        }catch (Exception e){
            Log.e(TAG, "checkTableExist| Error: Exception message ="+ e.getMessage());
            return false;
        }
        return true;
    }
}