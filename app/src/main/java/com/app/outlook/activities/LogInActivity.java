package com.app.outlook.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.app.outlook.R;
import com.app.outlook.Utils.APIMethods;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.FeedParams;
import com.app.outlook.modal.OutlookConstants;
import com.app.outlook.modal.UserProfileVo;
import com.app.outlook.modal.YearListVo;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

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
    //Google Declarations
    private static final int RC_SIGN_IN = 9001;
    //Facebook Callbacks
    CallbackManager callbackManager;
    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = "LogInActivity";
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;
    public ProfileTracker mProfileTracker;
    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;
    /* LogIn types */
    private static final int LOGIN_FACEBOOK = 1;
    private static final int LOGIN_GOOGLE = 2;
    private static final int LOGIN_EMAIL = 3;
    private LoadToast loadToast;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // [START restore_saved_instance_state]
        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }
        // initialize g+ & FB sdks
        initializeSDK();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private void initView() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        loadToast = new LoadToast(this);
        loadToast.setText("Loading...");
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
    @OnClick (R.id.facebook_button) void facebookLogin(){
        loadToast.show();
        Toast.makeText(getApplicationContext(),"facebook",Toast.LENGTH_SHORT).show();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));


    }
    @OnClick (R.id.google_button) void googleLogin(){
        //loadToast.show();
        mShouldResolve = true;
        mGoogleApiClient.connect();
        Toast.makeText(getApplicationContext(), "google", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.email_button) void emailLogin(){
       String email= mEmailEditField.getText().toString().trim();
        String password=mPasswordEditField.getText().toString().trim();
        if (SharedPrefManager.isValidEmail(email) && password.length()>0){
            doEmailLogIn(email, password);
        }
    }
/*login api call*/
    private void doEmailLogIn(String email, String password) {
        loadToast.show();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(FeedParams.EMAIL, email);
        params.put(FeedParams.USERNAME, password);
        placeRequest(APIMethods.REGISTER, UserProfileVo.class, params, true);

    }

    /*initializing the g+ & fb SDK*/
    private void initializeSDK() {
        //GoogleAPIClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();

        //FaceBookSDK Initialize
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, callback);
    }
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
                    Log.v("facebook - profile", profile2.getFirstName());
                    profile = profile2;
                    if (accessToken != null) {
                        requestFacebookData(facebook_token);
                    }

                    mProfileTracker.stopTracking();
                }
            };
            mProfileTracker.startTracking();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("LogIn", "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further errors.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
/* g+ connected */
    @Override
    public void onConnected(Bundle bundle) {
        Log.d("LogIn", "onConnected:" + bundle);
        // Show the signed-in UI
        loadToast.success();
        requestGoogleData(true);
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

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                showErrorDialog(connectionResult);
            }
        } else {
            // Show the signed-out UI
            requestGoogleData(false);
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
                            mShouldResolve = false;
                            requestGoogleData(false);
                        }
                    }).show();
        } else {
            // No default Google Play Services error, display a message to the user.
            String errorString = getString(R.string.play_services_error_fmt, errorCode);
            Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
            mShouldResolve = false;
            requestGoogleData(false);
        }
    }
    /*google data*/
    private void requestGoogleData(boolean isSignedIn) {
        Log.d(TAG, "requestGoogleData isSignedIn::" + isSignedIn);
        if (isSignedIn) {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                Log.d(TAG, "requestGoogleData Person Name::" + personName);
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                Log.d(TAG, "email::" + email);
                UserProfileVo profile= new UserProfileVo();
                profile.setName(personName);
                profile.setEmail(email);
                saveSocialLogInData(profile);
                doEmailLogIn(profile.getEmail(),profile.getEmail());
            }
        } else {
            // Show signed-out message
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
                        UserProfileVo profile= new UserProfileVo();
                        profile.setName(json.getString("name"));
                        profile.setEmail(json.getString("email"));
                        saveSocialLogInData(profile);
                        doEmailLogIn(profile.getEmail(),profile.getEmail());
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
        Log.i(TAG,"success"+response);
        loadToast.success();
        UserProfileVo userInfo = (UserProfileVo) response;
        userInfo.setEmail(mEmailEditField.getText().toString().trim());
        saveLogInToken(userInfo);
    }
    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        Log.i(TAG, "Error" + error);
        loadToast.error();
        Toast.makeText(getApplicationContext(),"Couldn't LogIn. Please try later.",Toast.LENGTH_SHORT).show();
    }
    /*saving Login data & move to next screen*/
    private void saveLogInToken(UserProfileVo profile) {
            SharedPrefManager.getInstance().setSharedData(OutlookConstants.TOKEN, profile.getToken());
            SharedPrefManager.getInstance().setSharedData(OutlookConstants.IS_LOGGEDIN, true);
            startActivity(new Intent(LogInActivity.this, HomeListingActivity.class));
            Log.i(TAG, profile.getToken() + "email" + profile.getEmail() + "name" + profile.getName());
            finish();
    }
private void saveSocialLogInData(UserProfileVo profileVo){
    SharedPrefManager.getInstance().setSharedData(OutlookConstants.PROFILE_EMAIL, profileVo.getEmail());
    SharedPrefManager.getInstance().setSharedData(OutlookConstants.PROFILE_NAME, profileVo.getName());
}
}
