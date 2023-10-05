package com.gcs.appantiroboandroid.presentation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gcs.appantiroboandroid.AccountGoogleConstants;
import com.gcs.appantiroboandroid.R;
import com.gcs.appantiroboandroid.config.OrmLiteSqliteOpenHelperImpl;
import com.gcs.appantiroboandroid.exception.GoogleAuthFailException;
import com.gcs.appantiroboandroid.model.ConfigModel;
import com.gcs.appantiroboandroid.repository.ConfigRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.api.services.gmail.GmailScopes;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "APP_ANTIROBO | MainActivity | ";
    private static final int RC_SIGN_IN = 9001;
    private SignInButton btnSignIn;
    private GoogleSignInClient googleSignInClient;
    private String accountEmail;
    private String accountToken;
    private ConfigRepository configRepository;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configRepository = new ConfigRepository(this);

        ConfigModel configModel = this.configRepository.getValue(AccountGoogleConstants.EMAIL);




        if (this.accountToken != null) {
            Intent intentResult =  new Intent(this, SettingsActivity.class);
            startActivity(intentResult);
        }





        this.btnSignIn = findViewById(R.id.btnSignIn);
        Scope gmailScope = new Scope(GmailScopes.GMAIL_SEND);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(gmailScope)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, googleSignInOptions);
        this.btnSignIn.setOnClickListener(view -> {
            try{
                Log.i(TAG, "setOnClickListener| onclick btnSignIn");
                btnSignIn.setActivated(false);
                Intent signInIntent = googleSignInClient.getSignInIntent();
                someActivityResultLauncher.launch(signInIntent);
            }catch(RuntimeException runtimeException){
                Log.e(TAG, "setOnClickListener| runtimeException="+runtimeException.getMessage());
                Toast.makeText(this, "Error al intentar Autheticarse", Toast.LENGTH_LONG);
                btnSignIn.setActivated(false);
            }

        });
    }
    private final ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.i(TAG + "someActivityResultLauncher", "result activity login Google API");
                Log.i(TAG + "someActivityResultLauncher", "result ="+result.getResultCode() );

                //Validate Result Code Activity Sign In Google
                if (result.getResultCode() == Activity.RESULT_OK) {

                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);

                        List<ConfigModel> lstConfigModel = new ArrayList<>();
                        ConfigModel accountIdConfig = new ConfigModel("account_id", account.getId());
                        ConfigModel accountNameConfig = new ConfigModel("account_name", account.getDisplayName());
                        ConfigModel accountEmailConfig = new ConfigModel("account_email", account.getEmail());
                        ConfigModel accountTokenConfig = new ConfigModel("account_token",account.getIdToken());
                        lstConfigModel.add(new ConfigModel());

                        this.configRepository.saveListConfigModel(lstConfigModel);

                        Log.i(TAG + "someActivityResultLauncher", "Get Info Account");
                        Intent intentRedirectSettingsActivity = new Intent(this, SettingsActivity.class);
                        Log.i(TAG + "someActivityResultLauncher", "Redirect to SettingsActivity");
                        startActivity(intentRedirectSettingsActivity);
                        return;
                    } catch (ApiException e) {
                        Log.e(TAG + "someActivityResultLauncher", "signInResult:failed code=" + e.getStatusCode());
                        throw new GoogleAuthFailException("Fail in Login Google code"+ e.getStatusCode());
                    }
                }else{
                    Log.e(TAG + "someActivityResultLauncher", "result code fail =" + result.getResultCode());
                    Toast.makeText(this, "Error al intentar Autheticarse", Toast.LENGTH_LONG);
                    return;
                }
            });

}