package com.gcs.appantiroboandroid.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.gcs.appantiroboandroid.ConstantsUtils;
import com.gcs.appantiroboandroid.R;
import com.gcs.appantiroboandroid.presentation.fragment.SettingsFragment;
import com.gcs.appantiroboandroid.repository.AttributesRepository;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "APP_ANTIROBO - SettingsActivity";

    private AttributesRepository attributesRepository;

    private void showActivityMain(){
        Log.d(TAG, "showActivityMain| Create intent redirect activity to MainActivity");
        Intent intentRedirectSettingsActivity = new Intent(this, MainActivity.class);

        Log.d(TAG, "showActivityMain| redirect activity to MainActivity");
        startActivity(intentRedirectSettingsActivity);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Log.d(TAG, "onCreate| Values are assigned to the attributes of class SettingsActivity");
        this.attributesRepository = new AttributesRepository(this);

        Log.d(TAG, "onCreate| validate exist credential Google in storage APP");
        if(this.attributesRepository.getValue(ConstantsUtils.ACCOUNT_TOKEN)==null){

            Log.d(TAG, "onCreate| not exist credential Google in storage APP");
            Log.d(TAG, "onCreate| invoke method SettingsActivity.showSettingsActivity");
            showActivityMain();
        }else{
            if (savedInstanceState == null) {
                Log.d(TAG, "onCreate| create instance class SettingsFragment show");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings, new SettingsFragment(attributesRepository))
                        .commit();
            }
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }


    }



}