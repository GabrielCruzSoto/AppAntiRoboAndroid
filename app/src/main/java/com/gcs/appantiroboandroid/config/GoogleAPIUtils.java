package com.gcs.appantiroboandroid.config;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.google.auth.Credentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GoogleAPIUtils {
    private static final String TAG = "APP_ANTIROBO - GoogleAPIUtils";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final Scope gmailScope = new Scope(GmailScopes.GMAIL_SEND);
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;

    public GoogleAPIUtils() {

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

    public void sendMessage(InputStreamReader inputStreamFile, String fromEmail, String emailTo, String subject, String bodyMessage) throws IOException {


        try {
            //GoogleClientSecrets clientSecrets = GoogleClientSecrets.load( GsonFactory.getDefaultInstance(), inputStreamFile);
            //GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
            //        .createScoped(GmailScopes.GMAIL_SEND);
            GoogleCredentials credentials = new GoogleCredentials(new AccessToken());

            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
            // Create the gmail API client
            Gmail service = new Gmail.Builder(new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    requestInitializer)
                    .setApplicationName("App Anti Robo ")
                    .build();




            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage email = new MimeMessage(session);
            email.setFrom(new InternetAddress(fromEmail));
            email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(emailTo));
            email.setSubject(subject);
            email.setText(bodyMessage);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            email.writeTo(baos);
            byte[] rawMessageBytes = baos.toByteArray();
            String encodedEmail = Base64.getEncoder().encodeToString(rawMessageBytes);
            Message message = new Message();
            message.setRaw(encodedEmail);

            service.users().messages().send("me", message).execute();
        } catch (Exception e) {
            Log.e(TAG, "sendMessage| run -> Error: " + e.getMessage());
        }
        // Encode as MIME message

    }


}
