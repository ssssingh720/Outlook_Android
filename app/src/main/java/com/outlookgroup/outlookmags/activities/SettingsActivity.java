package com.outlookgroup.outlookmags.activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.outlookgroup.outlookmags.R;
import com.outlookgroup.outlookmags.Utils.APIMethods;
import com.outlookgroup.outlookmags.Utils.CircleTransform;
import com.outlookgroup.outlookmags.Utils.Util;
import com.outlookgroup.outlookmags.manager.SharedPrefManager;
import com.outlookgroup.outlookmags.modal.FeedParams;
import com.outlookgroup.outlookmags.modal.IntentConstants;
import com.outlookgroup.outlookmags.modal.OutlookConstants;
import com.outlookgroup.outlookmags.modal.UserProfileVo;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 02/11/15.
 */
public class SettingsActivity extends AppBaseActivity implements View.OnClickListener{
    @Bind(R.id.switch_notification)
    Switch switchNotification;
    /*@Bind(R.id.username_settings)
    TextView userName;*/
    @Bind(R.id.email_settings)
    TextView emailId;
    @Bind(R.id.imageView_user)
    ImageView userImage;
    @Bind(R.id.text_notify_status)
            TextView notifyStatus;
    Dialog logoutPopUp;
    String fbId,gId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.scale_exit);
        SharedPrefManager prefManager = SharedPrefManager.getInstance();
        int theme = prefManager.getSharedDataInt(IntentConstants.THEME) == 0 ? R.style.AppTheme : prefManager.getSharedDataInt(IntentConstants.THEME);
        setTheme(theme);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            notifyStatus.setVisibility(View.VISIBLE);
        } else{
            notifyStatus.setVisibility(View.GONE);        }
        fbId=SharedPrefManager.getInstance().getSharedDataString(FeedParams.FB_ID);
        gId=SharedPrefManager.getInstance().getSharedDataString(FeedParams.GMAIL_ID);
        if (fbId!=null && !fbId.isEmpty()){
            String url=APIMethods.FB_URL+fbId+APIMethods.FB_IMAGE;
            Picasso.with(this).load(url).transform(new CircleTransform()).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(userImage);
        }
        else if(gId!=null && !gId.isEmpty()){
            String uri= SharedPrefManager.getInstance().getSharedDataString(FeedParams.GMAIL_IMAGE);
            if (uri!=null && !uri.isEmpty()) {
                Picasso.with(this).load(uri).transform(new CircleTransform()).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(userImage);
            }
            else{
                userImage.setImageResource(R.drawable.icon_user);
            }
            }

        else{
            userImage.setImageResource(R.drawable.icon_user);
        }
        if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_NOTIFICATION)){
            switchNotification.setChecked(true);
            notifyStatus.setText("ON");
        }
        else{
            switchNotification.setChecked(false);
            notifyStatus.setText("OFF");
        }
        //userName.setText(SharedPrefManager.getInstance().getSharedDataString(FeedParams.PROFILE_NAME));
        emailId.setText(SharedPrefManager.getInstance().getSharedDataString(FeedParams.PROFILE_EMAIL));
        switchNotification.setOnClickListener(this);
    }


    @OnClick({R.id.imgClose,R.id.settings_logout,R.id.settings_about,R.id.settings_terms_condition,R.id.settings_privacy_policy,R.id.settings_how_to_use})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.imgClose:
                onCloseClick();
                break;
            case R.id.settings_logout:
                onLogOutClick();
                break;
            case R.id.settings_about:
                openWebView(IntentConstants.ABOUT_US);
                break;
            case R.id.settings_privacy_policy:
                openWebView(IntentConstants.PRIVACY);
                break;
            case R.id.settings_how_to_use:
                openWebView(IntentConstants.HELP);
                break;
        }
    }

    private void openWebView(int type) {
        Intent webIntent = new Intent(getApplicationContext(),SettingsDetailsActivity.class);
        webIntent.putExtra(IntentConstants.SCREEN_NAME,type);
        startActivity(webIntent);
    }

    public void onCloseClick() {
        finish();
    }
public void onLogOutClick(){
    if (Util.isNetworkOnline(this)) {
        showLogoutPopUp();
    }
    else{
        showToast(getResources().getString(R.string.no_internet));
    }
}

    private void showLogoutPopUp() {
        logoutPopUp = new Dialog(SettingsActivity.this, R.style.DialogSlideAnim);
        logoutPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logoutPopUp.setContentView(R.layout.popup_logout);
        logoutPopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        logoutPopUp.getWindow().setGravity(Gravity.CENTER);
        logoutPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button logoutNo=(Button)logoutPopUp.findViewById(R.id.logoutNoBtn);
        Button logoutYes=(Button)logoutPopUp.findViewById(R.id.logoutYesBtn);

        logoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           hidePopupDialog();
            }
        });
        logoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.isNetworkOnline(SettingsActivity.this)) {
                    String root =getCacheDir().getAbsolutePath();
                    String filePath = root + File.separator + "Outlook/";
                    unRegisterNotification();
                    SharedPrefManager.getInstance().clearPreference();
                    Util.clearNotification(SettingsActivity.this);
                    File file = new File(filePath);
                    if (file.exists()) {
                        Log.i("filelogoutpath",filePath);
                        Util.deleteDirectory(file);
                    }
                    if (fbId!=null && !fbId.isEmpty()) {
                        LoginManager.getInstance().logOut();
                    }
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();
                    Intent intent = new Intent(getApplicationContext(),
                            LogInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    hidePopupDialog();
                    startActivity(intent);
                    finish();
                }
                else{
                    showToast(getResources().getString(R.string.no_internet));
                }
            }
        });
        logoutPopUp.show();
    }

    private void unRegisterNotification() {
        if (Util.isNetworkOnline(this)) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(FeedParams.USER_ID, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID));
            params.put(FeedParams.TOKEN, SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN));
            params.put(FeedParams.DEVICE_ID, SharedPrefManager.getInstance().getSharedDataString(OutlookConstants.GCM_TOKEN));
            params.put(FeedParams.DEVICE_TYPE, "android");
            placeRequest(APIMethods.UN_REGISTER_NOTIFICATION, UserProfileVo.class, params, false, null);
        }
        else{
            showToast(getResources().getString(R.string.no_internet));
            switchNotification.setChecked(SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_NOTIFICATION));
            if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_NOTIFICATION))
                notifyStatus.setText("ON");
            else
                notifyStatus.setText("OFF");
        }
    }
    private void registerNotification() {
        if (Util.isNetworkOnline(this)) {
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
        else{
            showToast(getResources().getString(R.string.no_internet));
            switchNotification.setChecked(SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_NOTIFICATION));
            if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_NOTIFICATION))
                notifyStatus.setText("ON");
            else
                notifyStatus.setText("OFF");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        if (apiMethod.equalsIgnoreCase(APIMethods.REGISTER_NOTIFICATION)){
            SharedPrefManager.getInstance().setSharedData(OutlookConstants.IS_NOTIFICATION, true);
            //switchNotification.setChecked(true);
            notifyStatus.setText("ON");
        }
        if (apiMethod.equalsIgnoreCase(APIMethods.UN_REGISTER_NOTIFICATION)){
            SharedPrefManager.getInstance().setSharedData(OutlookConstants.IS_NOTIFICATION, false);
            //switchNotification.setChecked(false);
            notifyStatus.setText("OFF");
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        if (apiMethod.equalsIgnoreCase(APIMethods.REGISTER_NOTIFICATION)){
            switchNotification.setChecked(false);
            notifyStatus.setText("OFF");
        }
        if (apiMethod.equalsIgnoreCase(APIMethods.UN_REGISTER_NOTIFICATION)) {
            switchNotification.setChecked(true);
            notifyStatus.setText("ON");
        }
    }
    private void hidePopupDialog() {
        if (logoutPopUp.isShowing()) {
            logoutPopUp.dismiss();
            logoutPopUp.cancel();
        }
    }

    @Override
    public void onClick(View view) {
       boolean isChecked= ((Switch) view).isChecked();
        if(isChecked){
            registerNotification();
        }else{
            unRegisterNotification();
        }
    }

}
