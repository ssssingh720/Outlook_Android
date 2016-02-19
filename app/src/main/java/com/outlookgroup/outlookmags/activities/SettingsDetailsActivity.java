package com.outlookgroup.outlookmags.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.outlookgroup.outlookmags.R;
import com.outlookgroup.outlookmags.modal.IntentConstants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.outlookgroup.outlookmags.modal.IntentConstants.ABOUT_US;
import static com.outlookgroup.outlookmags.modal.IntentConstants.HELP;
import static com.outlookgroup.outlookmags.modal.IntentConstants.PRIVACY;

/**
 * Created by Madhumita on 14-01-2016.
 */
public class SettingsDetailsActivity extends AppBaseActivity {
    @Bind(R.id.webview_settings)
    WebView mWebview;
    @Bind(R.id.toolbar_title)
    TextView mTitle;
    int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.scale_exit);
        setContentView(R.layout.activity_settings_webview);
        ButterKnife.bind(this);
       type= getIntent().getIntExtra(IntentConstants.SCREEN_NAME,0);
        initView(type);
    }
    @OnClick(R.id.toolbar_back)
    public void onMBackClick() {
        onBackPressed();
    }

    private void initView(int type) {
        String webUrl=null;
        switch (type){
            case ABOUT_US:
                mTitle.setText(getResources().getString(R.string.about));
                webUrl = "file:///android_asset/about_us.html";
                break;
            case PRIVACY:
                mTitle.setText(getResources().getString(R.string.privacy_policy));
                webUrl = "file:///android_asset/privacy_policy.html";
                break;
            case HELP:
                mTitle.setText(getResources().getString(R.string.how_to_use));
                webUrl="http://www.outlooktraveller.in/app/help.html";
                mWebview.getSettings().setLoadWithOverviewMode(true);
                mWebview.getSettings().setUseWideViewPort(true);
                mWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                break;

        }

        mWebview.getSettings().setJavaScriptEnabled(true);

        mWebview.loadUrl(webUrl);
    }

}
