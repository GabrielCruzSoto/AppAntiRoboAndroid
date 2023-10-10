package com.gcs.appantiroboandroid.utils;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;
import android.util.Log;

import com.gcs.appantiroboandroid.repository.AttributesRepository;

import java.io.IOException;

public class OnTokenAcquired implements AccountManagerCallback<Bundle> {
    private static final String TAG = "APP_ANTIROBO - OnTokenAcquired";

    public OnTokenAcquired(AttributesRepository attributesRepository){
        super();
    }

    @Override
    public void run(AccountManagerFuture<Bundle> result) {
        // Get the result of the operation from the AccountManagerFuture.
        try {
            Bundle bundle = result.getResult();
            String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
            String accountType = bundle.getString(AccountManager.KEY_ACCOUNT_TYPE);
            String statusToken = bundle.getString(AccountManager.KEY_ACCOUNT_STATUS_TOKEN);
            String userData = bundle.getString(AccountManager.KEY_USERDATA);
            String accountSessionBundle = bundle.getString(AccountManager.KEY_ACCOUNT_SESSION_BUNDLE);
            //bundle.get(AccountManager.KEY_ACCOUNT_SESSION_BUNDLE);
            Log.d(TAG, "run| KEY_AUTHTOKEN="+ token);
            Log.d(TAG, "run| KEY_ACCOUNT_TYPE="+ accountType);
            Log.d(TAG, "run| KEY_ACCOUNT_STATUS_TOKEN="+ statusToken);
            Log.d(TAG, "run| KEY_USERDATA="+ userData);
            Log.d(TAG, "run| KEY_ACCOUNT_SESSION_BUNDLE="+ accountSessionBundle);

        } catch (AuthenticatorException | IOException | OperationCanceledException e) {
            Log.e("onError","ERROR "+  e.getMessage());
            throw new RuntimeException(e);
        }

    }
}