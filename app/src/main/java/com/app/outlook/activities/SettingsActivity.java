package com.app.outlook.activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.app.outlook.R;
import com.app.outlook.Utils.APIMethods;
import com.app.outlook.Utils.Util;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.FeedParams;
import com.app.outlook.modal.IntentConstants;
import com.app.outlook.modal.OutlookConstants;
import com.app.outlook.modal.UserProfileVo;

import org.w3c.dom.Text;

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
    @Bind(R.id.username_settings)
    TextView userName;
    @Bind(R.id.email_settings)
    TextView emailId;
    Dialog logoutPopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager prefManager = SharedPrefManager.getInstance();
        int theme = prefManager.getSharedDataInt(IntentConstants.THEME) == 0 ? R.style.AppTheme : prefManager.getSharedDataInt(IntentConstants.THEME);
        setTheme(theme);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_NOTIFICATION)){
            switchNotification.setChecked(true);
        }
        else{
            switchNotification.setChecked(false);
        }
        userName.setText(SharedPrefManager.getInstance().getSharedDataString(FeedParams.PROFILE_NAME));
        emailId.setText(SharedPrefManager.getInstance().getSharedDataString(FeedParams.PROFILE_EMAIL));
        switchNotification.setOnClickListener(this);
    }


    @OnClick(R.id.imgClose)
    public void onCloseClick() {
        finish();
    }
@OnClick(R.id.settings_logout)
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
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.scale_exit);
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        if (apiMethod.equalsIgnoreCase(APIMethods.REGISTER_NOTIFICATION)){
            SharedPrefManager.getInstance().setSharedData(OutlookConstants.IS_NOTIFICATION, true);
            //switchNotification.setChecked(true);
        }
        if (apiMethod.equalsIgnoreCase(APIMethods.UN_REGISTER_NOTIFICATION)){
            SharedPrefManager.getInstance().setSharedData(OutlookConstants.IS_NOTIFICATION, false);
            //switchNotification.setChecked(false);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        if (apiMethod.equalsIgnoreCase(APIMethods.REGISTER_NOTIFICATION)){
            switchNotification.setChecked(false);
        }
        if (apiMethod.equalsIgnoreCase(APIMethods.UN_REGISTER_NOTIFICATION)){
            switchNotification.setChecked(true);
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
