package com.outlookgroup.outlookmags.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.outlookgroup.outlookmags.R;
import com.outlookgroup.outlookmags.Utils.APIMethods;
import com.outlookgroup.outlookmags.Utils.Util;
import com.outlookgroup.outlookmags.manager.SharedPrefManager;
import com.outlookgroup.outlookmags.modal.BaseVO;
import com.outlookgroup.outlookmags.modal.FeedParams;
import com.outlookgroup.outlookmags.modal.OutlookConstants;
import com.outlookgroup.outlookmags.modal.ResponseError;
import com.outlookgroup.outlookmags.modal.UserProfileVo;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Madhumita on 05-11-2015.
 */
public class LogInActivity extends AppBaseActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = "LogInActivity";
    //Google Declarations
    private static final int RC_SIGN_IN = 9001;
    private final int SIGNUP_REQUEST = 105;

    public ProfileTracker mProfileTracker;
    @Bind(R.id.facebook_button)
    RelativeLayout mFacebookLogInBtn;
    @Bind(R.id.google_button)
    RelativeLayout mGoogleLogInBtn;
    @Bind(R.id.email_button)
    Button mEmailLogInBtn;
    @Bind(R.id.enter_email)
    EditText mEmailEditField;
    @Bind(R.id.enter_password)
    EditText mPasswordEditField;
    @Bind(R.id.password_forgot)
    TextView mForgotPassword;
    @Bind(R.id.signup_text)
    TextView mSignUp;
    //Facebook Callbacks
    CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private LoadToast loadToast;
    private Dialog forgotPasswordPopUp;
    /*Facebok login Module starts here & Callback methods*/
    public FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            loadToast.success();
            Log.d(TAG, "Facebook Login Success::" + loginResult);
            Set<String> permissionsDenied = loginResult.getRecentlyDeniedPermissions();
            Log.d("LogIn", "Facebook Login Success Permission Denied::" + permissionsDenied);
            final AccessToken accessToken = loginResult.getAccessToken();
            final String facebook_token = accessToken.getToken();
            Profile profile = null;
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                 //   Log.v("facebook - profile", profile2.getFirstName());
                    profile = profile2;
                    mProfileTracker.stopTracking();
                }
            };
            mProfileTracker.startTracking();
            if (facebook_token != null) {
                requestFacebookData(facebook_token);
            }
        }

        @Override
        public void onCancel() {
            loadToast.error();
            Log.d("LogIn", "Facebook Login Cancel::");
        }

        @Override
        public void onError(FacebookException exception) {
            loadToast.error();
            Log.d("LogIn", "Facebook Login Error::" + exception);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.scale_exit);
       /* // [START restore_saved_instance_state]
        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }*/
        // initialize g+ & FB sdks
        initializeSDK();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    private void initView() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        loadToast = new LoadToast(this);
        loadToast.setText("Signing In ...");
        int height = size.y;
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.app_red));

    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @OnClick(R.id.facebook_button)
    void facebookLogin() {
        if (Util.isNetworkOnline(LogInActivity.this)) {
            loadToast.show();
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        } else {
            showToast(getResources().getString(R.string.no_internet));
        }
    }

    @OnClick(R.id.google_button)
    void googleLogin() {
        if (Util.isNetworkOnline(LogInActivity.this)) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    else{
        showToast(getResources().getString(R.string.no_internet));
    }
    }

    @OnClick(R.id.email_button)
    void emailLogin() {
        String email = mEmailEditField.getText().toString().trim();
        String password = mPasswordEditField.getText().toString().trim();
        if (email.length() > 0 && password.length() > 0) {
            if (Util.isNetworkOnline(LogInActivity.this)) {
                doEmailLogIn(email, password);
            }
            else{
                showToast(getResources().getString(R.string.no_internet));
            }
        } else{
            showToast(getResources().getString(R.string.empty_email_password));
        }
    }



    @OnClick(R.id.password_forgot)
    void forgotPassword() {
        displayForgotPasswordPopUp();
    }

    @OnClick(R.id.signup_text)
    void signUp() {
        startActivityForResult(new Intent(LogInActivity.this, SignUpActivity.class), SIGNUP_REQUEST);
    }

    private void displayForgotPasswordPopUp() {
        forgotPasswordPopUp = new Dialog(LogInActivity.this, R.style.DialogSlideAnim);
        forgotPasswordPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgotPasswordPopUp.setContentView(R.layout.popup_forgot_password);
        forgotPasswordPopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        forgotPasswordPopUp.getWindow().setGravity(Gravity.CENTER);
        forgotPasswordPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       final EditText emailField=(EditText)forgotPasswordPopUp.findViewById(R.id.forgot_password_email);
        Button resetPasswordBtn=(Button)forgotPasswordPopUp.findViewById(R.id.reset_password_button);
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                if (email.length() > 0 && SharedPrefManager.isValidEmail(email)) {
                    if (Util.isNetworkOnline(LogInActivity.this)) {
                        recoverPassword(email);
                        hidePopupDialog();
                    } else {
                        showToast(getResources().getString(R.string.no_internet));
                    }
                } else {
                    showToast(getResources().getString(R.string.email_to_get_password));
                }
            }
        });
        forgotPasswordPopUp.show();
    }

    /* recover password api call*/
    private void recoverPassword(String email) {
        loadToast.show();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(FeedParams.EMAIL, email);
        placeRequest(APIMethods.RESET_PASSWORD, BaseVO.class, params, true, null);
    }

    /*login api call after social login*/
    private void afterSocialLogIn(String uName, String email) {
        loadToast.show();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(FeedParams.USERNAME, uName);
        params.put(FeedParams.EMAIL, email);
        placeRequest(APIMethods.REGISTER, UserProfileVo.class, params, true, null);
    }
    /*login api call [Email]*/
    private void doEmailLogIn(String email, String password) {
        loadToast.show();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(FeedParams.USERNAME, email);
        params.put(FeedParams.PASSWORD, password);
        placeRequest(APIMethods.LOGIN, UserProfileVo.class, params, true, null);
        mEmailLogInBtn.setEnabled(false);
    }

    /*initializing the g+ & fb SDK*/
    private void initializeSDK() {
        registerGCM();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* Activity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //FaceBookSDK Initialize
        //FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, callback);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        //outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("LogIn", "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further errors.
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
        if (requestCode == SIGNUP_REQUEST) {
            if (resultCode == 103) {
                registerNotification();
               /* startActivity(new Intent(LogInActivity.this, HomeListingActivity.class));
                finish();*/
            }
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.i(TAG + "signin", acct.getDisplayName() + acct.getEmail());
            String personName = acct.getDisplayName();
            Log.d(TAG, "requestGoogleData Person Name::" + personName);
            String email = acct.getEmail();
            Log.d(TAG, "email::" + email);
            Log.d(TAG,"image::"+acct.getPhotoUrl());
            Uri uri=acct.getPhotoUrl();
            UserProfileVo profile = new UserProfileVo();
            profile.setName(personName);
            profile.setEmail(email);
            profile.setgId(acct.getId());
            if (acct.getPhotoUrl()!=null) {
             profile.setGmailPhoto(acct.getPhotoUrl().toString());
            }
            /*if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                if (person.hasImage()) {
                    Person.Image image = person.getImage();
                    Log.i("g+image",person.getImage().getUrl());
                }
            }*/
            saveSocialLogInData(profile);
            afterSocialLogIn(personName, email);
        } else {
            Log.d("GLogIn", result.getStatus()+"");
            showToast("Unable to Login through g+");
        }
    }
    /* g+ connected */
    @Override
    public void onConnected(Bundle bundle) {
        Log.d("LogIn", "onConnected:" + bundle);
        // Show the signed-in UI
        loadToast.success();
        //requestGoogleData(true);
    }

    /* g+ connection suspended */
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    /* g+ connection failed */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("LogIn", "onConnectionFailed:" + connectionResult);

            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                showErrorDialog(connectionResult);
            }

    }

    /*g+ Error dialog*/
    private void showErrorDialog(ConnectionResult connectionResult) {
        int errorCode = connectionResult.getErrorCode();

        if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
            // Show the default Google Play services error dialog which may still start an intent
            // on our behalf if the user can resolve the issue.
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, RC_SIGN_IN,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    }).show();
        } else {
            // No default Google Play Services error, display a message to the user.
            String errorString = getString(R.string.play_services_error_fmt, errorCode);
            Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();

        }
    }



    /*facebook data*/
    public void requestFacebookData(final String facebook_token) {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {    /*String text = "<b>Name :</b> " + json.getString("name") + "<br><br><b>Email :</b> " + json.getString("email") + "<br><br><b>Profile link :</b> " + json.getString("link");
                        showToast((Html.fromHtml(text)) + "");*/
                        UserProfileVo profile = new UserProfileVo();
                        profile.setName(json.getString("name"));
                        profile.setEmail(json.getString("email"));
                        profile.setFbId(json.getString("id"));
                        saveSocialLogInData(profile);
                        afterSocialLogIn(profile.getName(), profile.getEmail());
                    }
                } catch (JSONException e) {
                    //cancelAllLogins();
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();


    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        Log.i(TAG, "success" + response);

        if (apiMethod.equalsIgnoreCase(APIMethods.REGISTER)){
        UserProfileVo userInfo = (UserProfileVo) response;
            SharedPrefManager.getInstance().setSharedData(OutlookConstants.IS_ADMIN,userInfo.isAdmin());
        //userInfo.setEmail(mEmailEditField.getText().toString().trim());
            saveTokenAfterSocialLogIn(userInfo);
            registerNotification();

        }
        if (apiMethod.equalsIgnoreCase(APIMethods.LOGIN)){
            UserProfileVo userInfo = (UserProfileVo) response;
            userInfo.setEmail(mEmailEditField.getText().toString().trim());
            SharedPrefManager.getInstance().setSharedData(OutlookConstants.IS_ADMIN, userInfo.isAdmin());
            saveLogInToken(userInfo);
            registerNotification();
        }
        if (apiMethod.equalsIgnoreCase(APIMethods.RESET_PASSWORD)){
            loadToast.success();
            showToast("Password has been sent to your registered email");
        }
        if (apiMethod.equalsIgnoreCase(APIMethods.REGISTER_NOTIFICATION)){
            loadToast.success();
            SharedPrefManager.getInstance().setSharedData(OutlookConstants.IS_LOGGEDIN, true);
            SharedPrefManager.getInstance().setSharedData(OutlookConstants.IS_NOTIFICATION, true);
            startActivity(new Intent(LogInActivity.this, HomeListingActivity.class));
            finish();
        }
    }

    private void registerNotification() {
        //loadToast.show();
        String gcmToken= SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);
        if (gcmToken!=null && !gcmToken.isEmpty()) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(FeedParams.USER_ID, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID));
            params.put(FeedParams.TOKEN, gcmToken);
            params.put(FeedParams.DEVICE_ID, SharedPrefManager.getInstance().getSharedDataString(OutlookConstants.GCM_TOKEN));
            params.put(FeedParams.DEVICE_TYPE, "android");
            placeRequest(APIMethods.REGISTER_NOTIFICATION, UserProfileVo.class, params, false, null);
        }
        else{
            registerGCM();
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(FeedParams.USER_ID, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID));
            params.put(FeedParams.TOKEN, SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN));
            params.put(FeedParams.DEVICE_ID, SharedPrefManager.getInstance().getSharedDataString(OutlookConstants.GCM_TOKEN));
            params.put(FeedParams.DEVICE_TYPE, "android");
            placeRequest(APIMethods.REGISTER_NOTIFICATION, UserProfileVo.class, params, false, null);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        ResponseError rError = null;
        loadToast.error();
        if (apiMethod.equalsIgnoreCase(APIMethods.LOGIN)){
            mEmailLogInBtn.setEnabled(true);
            if (error.getClass().equals(ResponseError.class)) {
                rError = (ResponseError) error;
            }
            if (rError.getErrorMessage().equals("true")) {
                showToast("Username and password does not match");
            }
            else{
                showToast("Couldn't able to LogIn.Please try later.");
            }
        }
        if (apiMethod.equalsIgnoreCase(APIMethods.REGISTER)){
        showToast("Couldn't LogIn. Please try later.");}
        if (apiMethod.equalsIgnoreCase(APIMethods.RESET_PASSWORD)){
            showToast("Couldn't recover your password. Please try later.");}
        if (apiMethod.equalsIgnoreCase(APIMethods.REGISTER_NOTIFICATION)){
            SharedPrefManager.getInstance().setSharedData(OutlookConstants.IS_LOGGEDIN, true);
            startActivity(new Intent(LogInActivity.this, HomeListingActivity.class));
            showToast("Could not Register for Notification. Please check in your App-Settings.");
            finish();
        }
    }

    /*saving Login data & move to next screen*/
    private void saveLogInToken(UserProfileVo profile) {
        SharedPrefManager.getInstance().setSharedData(FeedParams.TOKEN, profile.getToken());
        SharedPrefManager.getInstance().setSharedData(FeedParams.USER_ID, profile.getUserId());
        if (profile.getEmail()!=null) {
            SharedPrefManager.getInstance().setSharedData(FeedParams.PROFILE_EMAIL, profile.getEmail());
        }
        Log.i(TAG, profile.getToken() + "email" + SharedPrefManager.getInstance().getSharedDataString(FeedParams.PROFILE_EMAIL) + "name" + profile.getName());
        String token=SharedPrefManager.getInstance().getSharedDataString(OutlookConstants.GCM_TOKEN);
        Log.d(TAG + "gcmToken", token);

    }

    private void saveSocialLogInData(UserProfileVo profileVo) {
        SharedPrefManager.getInstance().setSharedData(FeedParams.PROFILE_EMAIL, profileVo.getEmail());
        SharedPrefManager.getInstance().setSharedData(FeedParams.PROFILE_NAME, profileVo.getName());
        SharedPrefManager.getInstance().setSharedData(FeedParams.FB_ID, profileVo.getFbId());
        SharedPrefManager.getInstance().setSharedData(FeedParams.GMAIL_ID, profileVo.getgId());
        SharedPrefManager.getInstance().setSharedData(FeedParams.GMAIL_IMAGE, profileVo.getGmailPhoto());
    }
    private void saveTokenAfterSocialLogIn(UserProfileVo profile){
        SharedPrefManager.getInstance().setSharedData(FeedParams.TOKEN, profile.getToken());
        SharedPrefManager.getInstance().setSharedData(FeedParams.USER_ID, profile.getUserId());
        //SharedPrefManager.getInstance().setSharedData(OutlookConstants.IS_LOGGEDIN, true);
        //startActivity(new Intent(LogInActivity.this, HomeListingActivity.class));
        Log.i(TAG + "Social", profile.getToken() + "email" + SharedPrefManager.getInstance().getSharedDataString(FeedParams.PROFILE_EMAIL) + "name" + SharedPrefManager.getInstance().getSharedDataString(FeedParams.PROFILE_NAME));
       // finish();
    }

    private void hidePopupDialog() {
        if (forgotPasswordPopUp.isShowing()) {
            forgotPasswordPopUp.dismiss();
            forgotPasswordPopUp.cancel();
        }
    }

}
