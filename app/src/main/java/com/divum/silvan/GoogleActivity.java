package com.divum.silvan;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.IOException;


public class GoogleActivity extends Activity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "MainActivity";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    // Google client to interact with Google API
    public GoogleApiClient mGoogleApiClient;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;

    private boolean mSignInClicked;

    private ConnectionResult mConnectionResult;

    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private LinearLayout llProfileLayout;
    //private DefaultpinActivity Defaultpin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_google);
            btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        /*btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
*/
            // Button click listeners
            btnSignIn.setOnClickListener(this);
            // btnSignOut.setOnClickListener(this);
            //btnRevokeAccess.setOnClickListener(this);

            // Initializing google plus api client
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN).build();

    }
    ///usr/lib/jvm/java-8-oracle/bin/keytool -list -v -keystore /home/divum/Desktop/Silvan_images/sign_key/silvan_keystore  -alias silvan -storepass silvan_keystore -keypass silvan_password
    //keytool -exportcert -alias silvan -keystore /home/divum/Desktop/Silvan_images/sign_key/silvan_keystore -list -v
    protected void onStart() {

        super.onStart();

            mGoogleApiClient.connect();
            // Intent in=new Intent(GoogleActivity.this,MainActivity.class);startActivity(in);
        }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Button on click listener
     * */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_sign_in:
                System.out.println("4");
                // Signin button clicked
               /* String Result = getIntent().getExtras().getString("Request");
                if (Result.equals("logout")) {
                    signOutFromGplus();
                } else {
               */
                signInWithGplus();
                break;
            //case R.id.btn_sign_out:
                // Signout button clicked

                //signOutFromGplus();
                //break;
            /*case R.id.btn_revoke_access:
                // Revoke access button clicked
                System.out.println("6");
               //revokeGplusAccess();
                break;
        */}
    }
    public void onConnectionFailed(ConnectionResult result) {

        if (!result.hasResolution()) {

            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }

        if (!mIntentInProgress) {

            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {

                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
            if (requestCode == RC_SIGN_IN) {

            if (responseCode != RESULT_OK) {

                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {

                mGoogleApiClient.connect();

            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {

        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        //final String SCOPES = "https://www.googleapis.com/auth/userinfo.profile";
        Log.i(TAG, "onConnected");

// Update the user interface to reflect that the user is signed in.
       /* btnSignIn.setEnabled(true);
        btnSignOut.setEnabled(true);
        btnRevokeAccess.setEnabled(true);*/

// Retrieve some profile information to personalize our app for the user.
        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);


        AsyncTask<Void, Void, String > task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                String token = null;
                final String SCOPES = "https://www.googleapis.com/auth/plus.login ";

                try {

                    token = GoogleAuthUtil.getToken(
                            getApplicationContext(),
                            Plus.AccountApi.getAccountName(mGoogleApiClient),
                            "oauth2:" + SCOPES);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                }


                return token;

            }

            @Override
            protected void onPostExecute(String token) {

                Log.i(TAG, "Access token retrieved:" + token);
                Intent in = new Intent(GoogleActivity.this,DefaultpinActivity.class);

                in.putExtra("isunlock",false);
                startActivity(in);
                finish();
               // sa.ShowCofigPopUp(SplashActivity.class);
                ;

            }

        };

        task.execute();


        /*System.out.print("email" + email);
        mStatus.setText(String.format(
                getResources().getString(R.string.signed_in_as),
                currentUser.getDisplayName()));

        Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
                .setResultCallback(this);

// Indicate that the sign in process is complete.
        mSignInProgress = STATE_DEFAULT; }*/





        // Get user's information
        // getProfileInformation();

        // Update the UI after signin
        //  Intent in = new Intent(GoogleActivity.this, DefaultpinActivity.class);
        //startActivity(in);

        updateUI(true);

    }

    @Override
    public void onConnectionSuspended(int arg0) {

        mGoogleApiClient.connect();

        updateUI(false);

    }

    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {

            btnSignIn.setVisibility(View.GONE);
            // btnSignOut.setVisibility(View.VISIBLE);
            // btnRevokeAccess.setVisibility(View.VISIBLE);
            //llProfileLayout.setVisibility(View.VISIBLE);
        } else {

            btnSignIn.setVisibility(View.VISIBLE);
           /* btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);*/
        }
    }
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
            //Intent in=new Intent(GoogleActivity.this,MainActivity.class);startActivity(in);
        }
    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();

            }
        }
    }
    /*private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                txtName.setText(personName);
                txtEmail.setText(email);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    /*/*//**
     * Background Async task to load user profile picture from url
     /*//*
    *//*private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }*/

    public void signOutFromGplus() {
        System.out.println("method called for unlock");
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            updateUI(false);
        }else {
            System.out.println("not connected");
        }
    }
   /* private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                            updateUI(false);
                        }

                    });
        }
    }*/
}

 /*class Signout extends GoogleActivity{
    public void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            //updateUI(false);
        }
    }
}
*/