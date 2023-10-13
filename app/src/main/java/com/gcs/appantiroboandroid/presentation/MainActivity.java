package com.gcs.appantiroboandroid.presentation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.gcs.appantiroboandroid.ConstantsUtils;
import com.gcs.appantiroboandroid.R;
import com.gcs.appantiroboandroid.model.AttributesModel;
import com.gcs.appantiroboandroid.repository.AttributesRepository;
import com.gcs.appantiroboandroid.utils.GoogleLoginUtils;
import com.gcs.appantiroboandroid.utils.OnErrorUtils;
import com.gcs.appantiroboandroid.utils.OnTokenAcquired;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.api.services.gmail.GmailScopes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "APP_ANTIROBO - MainActivity";
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    private SignInButton btnSignIn;
    private AttributesRepository attributesRepository;
    private GoogleLoginUtils googleAPIUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Se agregan valores a los atributos.
        Log.d(TAG, "onCreate| Values are assigned to the attributes of class MainActivity");
        this.attributesRepository = new AttributesRepository(this);
        this.btnSignIn = findViewById(R.id.btnSignIn);
        this.googleAPIUtils = new GoogleLoginUtils();

        Log.d(TAG, "onCreate| invoke method MainActivity.validateIsExistSignInGoogleAPI");
        validateIsExistSignInGoogleAPI();

        Log.d(TAG, "onCreate| invoke method MainActivity.chargeEventsOnClick");
        loadsEventsObjBtnOnClick();

        Log.d(TAG, "onCreate| invoke method MainActivity.loadsMethodsValidateResultActivities");
        loadsMethodsValidateResultActivities();


    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart| invoke method MainActivity.validateIsExistSignInGoogleAPI");
        validateIsExistSignInGoogleAPI();
    }

    private void validateIsExistSignInGoogleAPI() {
        Log.d(TAG, "validateIsExistSignInGoogleAPI| validate credential Google in APP");
        if (this.attributesRepository.getValue(ConstantsUtils.ACCOUNT_TOKEN) != null) {
            Log.d(TAG, "validateIsExistSignInGoogleAPI| exist credential Google in APP");
            Log.d(TAG, "validateIsExistSignInGoogleAPI| invoke method MainActivity.showSettingsActivity");
            this.showSettingsActivity();
        }
    }

    private void loadsEventsObjBtnOnClick() {
        Log.d(TAG, "loadsEventsObjBtnOnClick| Added functionality to the method setOnClickListener the object MainActivity.btnSignIn");
        this.btnSignIn.setOnClickListener(view -> {
            try {
                Log.d(TAG, "loadsEventsObjBtnOnClick| btnSignIn.setOnClickListener");
                btnSignIn.setActivated(false);
                Intent signInIntent = googleAPIUtils.login(this);
                Log.d(TAG, "loadsEventsObjBtnOnClick| btnSignIn.setOnClickListener -> launch signInIntent");
                someActivityResultLauncher.launch(signInIntent);
            } catch (RuntimeException runtimeException) {
                Log.e(TAG, "loadsEventsObjBtnOnClick | btnSignIn.setOnClickListener -> Error deploying Google API signInIntent");
                Toast.makeText(this, "Error al intentar Autheticarse", Toast.LENGTH_LONG).show();
                btnSignIn.setActivated(true);
            }

        });
    }

    private void saveAccount(GoogleSignInAccount account) throws OperationCanceledException, AuthenticatorException, IOException {
        //Log.d(TAG, "saveAccount| Getting stored Google credentials");
        Log.d(TAG, "saveAccount| mapped Google credentials to AttributesModel");
        List<AttributesModel> lstAttributesModel = new ArrayList<>();
        AccountManager am = AccountManager.get(this);
        Bundle bundleInput = new Bundle();

        Account accountOutput = Arrays.stream(am.getAccounts()).filter(account1 -> Objects.equals(account1.name, account.getEmail())).collect(Collectors.toList()).get(0);


        am.getAuthToken(
                accountOutput,                     // Account retrieved using getAccountsByType()
                "oauth2:" +GmailScopes.GMAIL_SEND,            // Auth scope
                bundleInput,                        // Authenticator-specific options
                this,                           // Your activity
                new OnTokenAcquired(this.attributesRepository),          // Callback called when a token is successfully acquired
                new Handler(Looper.getMainLooper(),new OnErrorUtils()));    // Callback called if an error occurs
        lstAttributesModel.add(new AttributesModel(ConstantsUtils.ACCOUNT_ID, account.getId()));
        lstAttributesModel.add(new AttributesModel(ConstantsUtils.ACCOUNT_NAME, account.getDisplayName()));
        lstAttributesModel.add(new AttributesModel(ConstantsUtils.ACCOUNT_EMAIL, account.getEmail()));
        lstAttributesModel.add(new AttributesModel(ConstantsUtils.ACCOUNT_TOKEN, account.getIdToken()));
        lstAttributesModel.add(new AttributesModel(ConstantsUtils.SERVER_AUTH_CODE, account.getServerAuthCode()));
        Log.d(TAG, "saveAccount| Save stored Google credentials");
        this.attributesRepository.saveListAttributesModel(lstAttributesModel);
    }

    private void showSettingsActivity() {
        Log.d(TAG, "showSettingsActivity| Create intent redirect activity to SettingsActivity");
        Intent intentRedirectSettingsActivity = new Intent(this, SettingsActivity.class);

        Log.d(TAG, "showSettingsActivity| redirect activity to SettingsActivity");
        startActivity(intentRedirectSettingsActivity);
    }

    private void loadsMethodsValidateResultActivities() {
        Log.d(TAG, "loadsMethodsValidateResultActivities| Added functionality to the method someActivityResultLauncher");
        this.someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.d(TAG, "loadsMethodsValidateResultActivities| launch");
            Log.d(TAG, "loadsMethodsValidateResultActivities| launch -> result.getResultCode()" + result.getResultCode());
            if (result.getResultCode() == Activity.RESULT_OK) {
                try {
                    Log.d(TAG, "loadsMethodsValidateResultActivities| launch -> get credential Google from API Google");
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    GoogleSignInAccount account = task.getResult(ApiException.class);

                    Log.d(TAG, "loadsMethodsValidateResultActivities| launch -> Invoke method MainActivity.saveAccount");
                    this.saveAccount(account);

                    Log.d(TAG, "loadsMethodsValidateResultActivities| launch -> Invoke method MainActivity.showSettingsActivity");
                    this.showSettingsActivity();

                } catch (ApiException e) {
                    Log.e(TAG, "loadsMethodsValidateResultActivities| launch -> Error: " + e.getMessage());
                    Toast.makeText(this, "Error al obtener credenciales de Google", Toast.LENGTH_LONG).show();
                    throw new RuntimeException("Error al obtener credenciales de Google" + e.getStatusCode());

                } catch (OperationCanceledException e) {
                    throw new RuntimeException(e);
                } catch (AuthenticatorException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Log.e(TAG, "loadsMethodsValidateResultActivities| launch -> Error:  this StatusCode not Authorize ");
                Toast.makeText(this, "Error con la respuesta de Google", Toast.LENGTH_LONG).show();
                throw new RuntimeException("Error con la respuesta de Google" + result.getResultCode());
            }
        });
    }


}