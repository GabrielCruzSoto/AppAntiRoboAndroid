package com.gcs.appantiroboandroid.presentation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;

import com.gcs.appantiroboandroid.R;
import com.gcs.appantiroboandroid.utils.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "APP_ANTIROBO | SettingsActivity | ";
    private String accountEmail;
    private String accountName;
    private String accountToken;
    private String accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        SharedPreferences sharedPreferences = getSharedPreferences("google_signin_account", Context.MODE_PRIVATE);
        accountId = sharedPreferences.getString("account_id", null);
        accountEmail = sharedPreferences.getString("account_email", null);
        accountName = sharedPreferences.getString("account_name", null);
        accountToken = sharedPreferences.getString("account_token", null);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment(accountId, accountName, accountEmail, accountToken ))
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


}