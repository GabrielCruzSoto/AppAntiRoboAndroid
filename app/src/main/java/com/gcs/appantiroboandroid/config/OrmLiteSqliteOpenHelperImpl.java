package com.gcs.appantiroboandroid.config;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gcs.appantiroboandroid.model.ConfigModel;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class OrmLiteSqliteOpenHelperImpl  extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "APP_ANTIROBO | OrmLiteSqliteOpenHelperImpl | ";
    private static final String DATABASE_NAME = "db_antirobo.db";
    private static final int DATABASE_VERSION = 1;

    public OrmLiteSqliteOpenHelperImpl(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ConfigModel.class);
        } catch (SQLException e) {
            //e.printStackTrace();
            Log.e(TAG + "onCreate", "runtimeException="+e.getMessage());

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            if(checkTableExist(database,"item_user"))
                TableUtils.dropTable(connectionSource,ConfigModel.class,false);

            onCreate(database,connectionSource);
        }catch (SQLException e){
            Log.e(TAG + "onCreate", "runtimeException="+e.getMessage());
        }
    }

    private boolean checkTableExist(SQLiteDatabase database, String tableName){
        Cursor c = null;
        boolean tableExist = false;
        try {
            c = database.query(tableName, null,null,null,null,null,null);
            tableExist = true;
        }catch (Exception e){
            Log.e(TAG + "onCreate", "runtimeException="+e.getMessage());
        }
        return tableExist;
    }
    public void clearTable() throws SQLException{
        TableUtils.clearTable(getConnectionSource(),ConfigModel.class);
    }
}