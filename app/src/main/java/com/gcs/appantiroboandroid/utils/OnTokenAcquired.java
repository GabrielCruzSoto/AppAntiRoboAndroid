package com.gcs.appantiroboandroid.utils;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;
import android.util.Log;

import com.gcs.appantiroboandroid.ConstantsUtils;
import com.gcs.appantiroboandroid.repository.AttributesRepository;

import java.io.IOException;

public class OnTokenAcquired implements AccountManagerCallback<Bundle> {
    private static final String TAG = "APP_ANTIROBO - OnTokenAcquired";

    private final AttributesRepository  attributesRepository;

    public OnTokenAcquired(AttributesRepository attributesRepository){
        super();
        this.attributesRepository= attributesRepository;
    }

    @Override
    public void run(AccountManagerFuture<Bundle> result) {
        // Get the result of the operation from the AccountManagerFuture.
        try {
            Bundle bundle = result.getResult();
            String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
            Long tokenExpired = bundle.getLong("android.accounts.expiry");
            //bundle.get(AccountManager.KEY_ACCOUNT_SESSION_BUNDLE);
            if(token!= null){
                attributesRepository.saveAttribute(ConstantsUtils.KEY_AUTH_TOKEN,token);
            }
            if(tokenExpired!=0){
                attributesRepository.saveAttribute(ConstantsUtils.TOKEN_EXPIRED, String.valueOf(tokenExpired));
            }


        } catch (AuthenticatorException | IOException | OperationCanceledException e) {
            Log.e("onError","ERROR "+  e.getMessage());
            throw new RuntimeException(e);
        }

    }
}