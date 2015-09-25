package com.app.outlook.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.app.outlook.R;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.model.OutlookConstants;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by srajendrakumar on 11/09/15.
 */
public class AppBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPrefManager prefManager = SharedPrefManager.getInstance();
        prefManager.init(this);
        int theme = prefManager.getSharedDataInt(OutlookConstants.theme);
        if(theme != 0)
            setTheme(theme);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
