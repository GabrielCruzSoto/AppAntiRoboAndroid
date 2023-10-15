package com.gcs.appantiroboandroid.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.gcs.appantiroboandroid.ConstantsUtils;
import com.gcs.appantiroboandroid.R;
import com.gcs.appantiroboandroid.repository.AttributesRepository;
import com.gcs.appantiroboandroid.utils.GmailService;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class ReportLocationService extends Service {

    private static final String TAG = "APP_ANTIROBO - ReportLocationService";

    private GmailService gmailUtils;

    private int minutesIterator;

    private Handler handler;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private static String SUBJECT_MESSAGE = "APP ANTIROBO - STATUS ";


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

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(10000);  // Intervalo en milisegundos para actualizar la ubicación
                locationRequest.setFastestInterval(5000);  // Intervalo más rápido de actualización de la ubicación

                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult.getLastLocation() != null) {
                            Location location = locationResult.getLastLocation();
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            String url = "https://www.google.com/maps?q=" + latitude + "," + longitude;
                            String messageBody = "Tu Ubicacion Actual es " + url;

                            gmailUtils.sendMessage(SUBJECT_MESSAGE, messageBody);
                            // Utiliza la ubicación (latitude y longitude) como desees
                        }
                    }
                };

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    return;
                }
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

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
