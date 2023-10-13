package com.gcs.appantiroboandroid.utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.GmailScopes;

public class GoogleLoginUtils {
    private static final String TAG = "APP_ANTIROBO - GoogleAPIUtils";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final Scope gmailScope = new Scope(GmailScopes.GMAIL_SEND);
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;




    public GoogleLoginUtils() {

        Log.d(TAG, "GoogleAPIUtils| builder");

    }

    public Intent login(Activity activity) {
        Log.d(TAG, "login| Declare scope Google Gmail");


        Log.d(TAG, "login| request Sign In Google");
        this.googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(gmailScope)
                .requestEmail()
                .build();

        Log.d(TAG, "login| client Google API");
        this.googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions);
        return googleSignInClient.getSignInIntent();
    }






}
