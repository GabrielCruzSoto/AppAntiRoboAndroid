package com.gcs.appantiroboandroid.utils;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.gcs.appantiroboandroid.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String TAG = "APP_ANTIROBO | SettingsFragment | ";
    private AppCompatActivity appCompatActivity;
    private String accountEmail;
    private String accountName;
    private String accountToken;
    private String accountId;

    public SettingsFragment(String accountId, String accountName, String accountEmail,String accountToken ) {
        this.accountEmail = accountEmail;
        this.accountName = accountName;
        this.accountToken= accountToken;
        this.accountId= accountId;
    }
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {

        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Preference preference = new Preference(appCompatActivity);
        preference.setKey(getString(R.string.SETTINGS_ACTIVITY_TXT_FIELD_EMAIL_ACCOUNT));
        preference.setDefaultValue(accountEmail);
    }
}
