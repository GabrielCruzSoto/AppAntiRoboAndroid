package com.gcs.appantiroboandroid.utils;

import android.os.Build;
import android.util.Log;

import com.gcs.appantiroboandroid.ConstantsUtils;
import com.gcs.appantiroboandroid.model.AttributesModel;
import com.gcs.appantiroboandroid.repository.AttributesRepository;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class GmailService{



    private static final String TAG = "APP_ANTIROBO - GmailService";
    private String token;
    private Long expiredToken = 0L;

    private String emailMain;
    private String emailSecondary;

    public GmailService(AttributesRepository attributesRepository){
        AttributesModel attributesModelTokenExpired  =attributesRepository.getValue(ConstantsUtils.TOKEN_EXPIRED);
        if(attributesModelTokenExpired!=null){
            this.expiredToken= Long.valueOf(attributesModelTokenExpired.getValue());
        }

        AttributesModel attributesModelToken  =attributesRepository.getValue(ConstantsUtils.KEY_AUTH_TOKEN);
        if(attributesModelToken!=null){
            this.token= attributesModelToken.getValue();
        }


        AttributesModel attributesModelEmail  =attributesRepository.getValue(ConstantsUtils.ACCOUNT_EMAIL);
        if(attributesModelEmail!=null){
            this.emailMain= attributesModelEmail.getValue();
        }

        AttributesModel attributesModelEmailSecondary  =attributesRepository.getValue(ConstantsUtils.EMAIL_SECONDARY);
        if(attributesModelEmailSecondary!=null){
            this.emailSecondary= attributesModelEmailSecondary.getValue();
        }
    }

    public void sendMessage(String subject, String bodyMessage)  {
        try{


            Log.e(TAG, "sendMessage| token" + token);
            Log.e(TAG, "sendMessage| expiredToken" + expiredToken);
            GoogleCredentials credentials =  GoogleCredentials.create(new AccessToken(token,new Date(expiredToken)));

            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);


            Gmail.Builder gmailBuilder = new Gmail.Builder(new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    requestInitializer).setApplicationName("AppAntiRoboAndroid");

            Gmail gmail = gmailBuilder.build();
            Log.i(TAG, "sendMessage| Preparando mensaje");
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props);
            MimeMessage email = new MimeMessage(session);
            email.setFrom(new InternetAddress(emailSecondary));
            email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(emailMain));
            email.setSubject(subject);
            email.setText(bodyMessage);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            email.writeTo(baos);
            byte[] rawMessageBytes = baos.toByteArray();
            String encodedEmail = Base64.getEncoder().encodeToString(rawMessageBytes);
            Message message = new Message();
            message.setRaw(encodedEmail);
            Log.i(TAG, "sendMessage| Enviando mensaje");
            gmail.users().messages().send("me", message).execute();
        }catch(IOException | AddressException e) {
            Log.e(TAG, "sendMessage| AddressException| IOException -> Error: e => " + e);
            Log.e(TAG, "sendMessage| AddressException| IOException -> Error: getMessage => " + e.getMessage());
            //Log.e(TAG, "sendMessage| AddressException| IOException -> Error: getLocalizedMessage => " + e.getLocalizedMessage());
            Log.e(TAG, "sendMessage| AddressException| IOException -> Error: getMessage => " + e.getMessage());
            Log.e(TAG, "sendMessage| AddressException| IOException -> Error: getStackTrace => " + Arrays.toString(e.getStackTrace()));

        } catch (MessagingException e) {
            Log.e(TAG, "sendMessage| RuntimeException -> Error: " + e.getMessage());
            Log.e(TAG, "sendMessage| RuntimeException -> Error: " + e.getLocalizedMessage());
            Log.e(TAG, "sendMessage| RuntimeException -> Error: " + e.getMessage());
        }

    }
}
