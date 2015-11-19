package com.app.outlook.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.widget.Button;
import android.widget.EditText;

import com.app.outlook.R;
import com.app.outlook.Utils.APIMethods;
import com.app.outlook.Utils.Util;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.FeedParams;
import com.app.outlook.modal.OutlookConstants;
import com.app.outlook.modal.UserProfileVo;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Madhumita on 12-11-2015.
 */

public class SignUpActivity extends AppBaseActivity {
    @Bind(R.id.signup_email)
    EditText mSignUpEmail;
    @Bind(R.id.signup_password)
    EditText mSignUpPassword;
    @Bind(R.id.signup_confirm_password)
    EditText mSignUpConfirmPassword;
    @Bind(R.id.signup_user_name)
    EditText mSignUpUserName;
    @Bind(R.id.signup_button)
    Button mButtonSignUp;
    private LoadToast loadToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initView();
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

    @OnClick(R.id.signup_button)
    void doRegistration() {
        String userName = mSignUpUserName.getText().toString();
        String password = mSignUpPassword.getText().toString();
        String confirmPassword = mSignUpConfirmPassword.getText().toString();
        String email = mSignUpEmail.getText().toString();

        boolean is_userName_empty = Util.isEmptyString(userName);
        boolean is_password_empty = Util.isEmptyString(password);
        boolean is_confirmPassword_empty = Util.isEmptyString(confirmPassword);
        boolean is_email_empty = Util.isEmptyString(email);
        boolean is_validEmail = SharedPrefManager.isValidEmail(email);
        if (is_userName_empty) {
            mSignUpUserName.setError("Enter User Name");
            return;
        }
        if (is_email_empty || !SharedPrefManager.isValidEmail(email)) {
            mSignUpEmail.setError("Enter a valid Email Id");
            return;
        }

        if (is_password_empty ) {
            mSignUpPassword.setError("Enter Password");
            return;
        }
        if (is_confirmPassword_empty) {
            mSignUpConfirmPassword.setError("Enter Confirm Password");
            return;
        }
        if (!password.equals(confirmPassword)) {
            mSignUpConfirmPassword.setError("Confirmation password mismatch");
            return;
        }
        /*if (is_validEmail){
            mSignUpEmail.setError("Enter a valid email");
            return;
        }*/
        if (Util.isNetworkOnline(SignUpActivity.this)) {
        // API CALL
            loadToast.show();
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(FeedParams.EMAIL, email);
            params.put(FeedParams.PASSWORD, password);
            params.put(FeedParams.USERNAME, userName);
            placeRequest(APIMethods.REGISTER, UserProfileVo.class, params, true);
        } else {
            showToast(getResources().getString(R.string.no_internet));
        }

    }
    @OnClick(R.id.toolbar_back)
    public void onMBackClick() {
        finish();
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        loadToast.success();
        if (apiMethod.equalsIgnoreCase(APIMethods.REGISTER)){
            UserProfileVo userInfo = (UserProfileVo) response;
            //userInfo.setEmail(mEmailEditField.getText().toString().trim());
            saveToken(userInfo);
        }
    }

    private void saveToken(UserProfileVo userInfo) {
        SharedPrefManager.getInstance().setSharedData(FeedParams.TOKEN, userInfo.getToken());
        SharedPrefManager.getInstance().setSharedData(FeedParams.USER_ID, userInfo.getUserId());
        SharedPrefManager.getInstance().setSharedData(FeedParams.PROFILE_NAME, userInfo.getName());
        SharedPrefManager.getInstance().setSharedData(FeedParams.PROFILE_EMAIL, userInfo.getEmail());
        SharedPrefManager.getInstance().setSharedData(OutlookConstants.IS_LOGGEDIN, true);
        setResult(103);
       // startActivity(new Intent(SignUpActivity.this, HomeListingActivity.class));
        Log.i("Register", userInfo.getToken() + "email" + SharedPrefManager.getInstance().getSharedDataString(FeedParams.PROFILE_EMAIL) + "name" + userInfo.getName());
        finish();
    }
}
