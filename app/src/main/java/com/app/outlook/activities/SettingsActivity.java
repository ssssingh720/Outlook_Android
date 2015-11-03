package com.app.outlook.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.outlook.R;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.IntentConstants;

/**
 * Created by srajendrakumar on 02/11/15.
 */
public class SettingsActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager prefManager = SharedPrefManager.getInstance();
        int theme = prefManager.getSharedDataInt(IntentConstants.THEME) == 0 ? R.style.AppTheme : prefManager.getSharedDataInt(IntentConstants.THEME) ;
        setTheme(theme);

        setContentView(R.layout.activity_settings);
    }

    @Override
    public void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}
