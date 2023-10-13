package com.gcs.appantiroboandroid.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gcs.appantiroboandroid.ConstantsUtils;
import com.gcs.appantiroboandroid.R;
import com.gcs.appantiroboandroid.repository.AttributesRepository;
import com.gcs.appantiroboandroid.utils.GmailService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReportLocationService extends Service {
    private static final String TAG = "APP_ANTIROBO - ReportLocationService";

    private GmailService gmailUtils;

    private int minutesIterator;

    private Handler handler;

    @Override
    public void onCreate() {

        Log.d(TAG, "onCreate| ");
        super.onCreate();
        AttributesRepository attributesRepository = new AttributesRepository(this);
        Log.d(TAG, "SettingsFragment| get credential Google storage app");

        String strMinutes = attributesRepository.getValue(ConstantsUtils.MINUTES_ITERATOR).getValue();
        this.minutesIterator = Integer.parseInt(strMinutes);

        this.gmailUtils = new GmailService(attributesRepository);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand| create instance handler");
        handler = new Handler(Looper.getMainLooper());

        Log.d(TAG, "onStartCommand| execute service ");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    gmailUtils.sendMessage("TEST","TEST APP gps");

                } catch (Exception e) {
                    Log.e(TAG, "onStartCommand| run-> Error: MessagingException | IOException e.message ="+ e.getMessage());

                }

                Log.d(TAG, "onStartCommand| program execute in min " + minutesIterator);
                handler.postDelayed(this, (long) minutesIterator * 60 * 1000);
            }
        }, 0);

        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
