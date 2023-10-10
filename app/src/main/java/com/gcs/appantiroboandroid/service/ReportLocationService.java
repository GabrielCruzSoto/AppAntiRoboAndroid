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
import com.gcs.appantiroboandroid.config.GoogleAPIUtils;
import com.gcs.appantiroboandroid.model.AttributesModel;
import com.gcs.appantiroboandroid.repository.AttributesRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Driver;

import javax.mail.MessagingException;

public class ReportLocationService extends Service {
    private static final String TAG = "APP_ANTIROBO - ReportLocationService";

    private AttributesRepository attributesRepository;

    private GoogleAPIUtils googleAPIUtils;

    private String accountId;
    private String accountName;
    private String accountEmail;
    private String accountToken;
    private String emailSecondary;
    private int minutesIterator;

    private Handler handler;

    private InputStreamReader inputStreamReader;
    @Override
    public void onCreate() {
        this.googleAPIUtils = new GoogleAPIUtils();
        Log.d(TAG, "onCreate| ");
        super.onCreate();
        this.attributesRepository = new AttributesRepository(this);
        Log.d(TAG, "SettingsFragment| get credential Google storage app");
        this.accountId = this.attributesRepository.getValue(ConstantsUtils.ACCOUNT_ID).getValue();
        this.accountName = this.attributesRepository.getValue(ConstantsUtils.ACCOUNT_NAME).getValue();
        this.accountEmail = this.attributesRepository.getValue(ConstantsUtils.ACCOUNT_EMAIL).getValue();
        this.accountToken = this.attributesRepository.getValue(ConstantsUtils.ACCOUNT_TOKEN).getValue();
        this.emailSecondary = this.attributesRepository.getValue(ConstantsUtils.EMAIL_SECONDARY).getValue();
        String strMinutes = this.attributesRepository.getValue(ConstantsUtils.MINUTES_ITERATOR).getValue();
        this.minutesIterator = Integer.parseInt(strMinutes);

        Resources resources = this.getResources();
        InputStream inputStream = resources.openRawResource(R.raw.client_secret);
        inputStreamReader = new InputStreamReader(inputStream);
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
                    googleAPIUtils.sendMessage(inputStreamReader,accountEmail,emailSecondary,"TEST","TEST APP gps");

                } catch (IOException e) {
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
