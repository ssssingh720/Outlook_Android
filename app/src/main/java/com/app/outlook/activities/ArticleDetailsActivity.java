package com.app.outlook.activities;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.app.outlook.OutLookApplication;
import com.app.outlook.R;
import com.app.outlook.fragments.SectionDetailsHolderFragment;
import com.app.outlook.listener.OnArticleModeChangeListener;
import com.app.outlook.modal.IntentConstants;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.gms.analytics.HitBuilders;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiInterstitial;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 04/11/15.
 */
public class ArticleDetailsActivity extends AppBaseActivity {

    @Bind(R.id.toolbar_title)
    TextView titleTxt;
    private SectionDetailsHolderFragment sectionDetailsHolderFragment;
    private boolean isPurchased;
    @Bind(R.id.nightMode1)
    ToggleButton nightModeImg;
    @Bind(R.id.textSizeImg)
    ImageView textSizeImg;
    OnArticleModeChangeListener articleModeChangeListener;
    private static final long YOUR_PLACEMENT_ID = 1446840680498L;

    private InMobiInterstitial mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.scale_exit);
        setContentView(R.layout.activity_article_details);
        ButterKnife.bind(this);

        //showAdd();

        sectionDetailsHolderFragment = new SectionDetailsHolderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentConstants.CATEGORY_POSITION, getIntent().getIntExtra(IntentConstants.CATEGORY_POSITION, 0));
        bundle.putInt(IntentConstants.CARD_POSITION, getIntent().getIntExtra(IntentConstants.CARD_POSITION, 0));
        bundle.putString(IntentConstants.ISSUE_ID, getIntent().getStringExtra(IntentConstants.ISSUE_ID));
        bundle.putString(IntentConstants.MAGAZINE_ID, getIntent().getStringExtra(IntentConstants.MAGAZINE_ID));
        bundle.putString(IntentConstants.MAGAZINE_NAME, getIntent().getStringExtra(IntentConstants.MAGAZINE_NAME));
        bundle.putInt(IntentConstants.SUB_CATEGORY_POSITION, getIntent().getIntExtra(IntentConstants.SUB_CATEGORY_POSITION, 0));
        bundle.putString(IntentConstants.CATEGORY_TYPE, getIntent().getStringExtra(IntentConstants.CATEGORY_TYPE));
        bundle.putBoolean(IntentConstants.IS_PURCHASED, getIntent().getBooleanExtra(IntentConstants.IS_PURCHASED, false));
        if (getIntent().getStringExtra(IntentConstants.ADMIN_MAGAZINE)!=null){
            bundle.putString(IntentConstants.ADMIN_MAGAZINE, getIntent().getStringExtra(IntentConstants.ADMIN_MAGAZINE));
        }
        sectionDetailsHolderFragment.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_enter, R.anim.slide_exit);
        transaction.add(R.id.contentPanel, sectionDetailsHolderFragment);

        transaction.commit();
    }

    private void showAdd() {
        mInterstitialAd = new InMobiInterstitial(ArticleDetailsActivity.this, YOUR_PLACEMENT_ID,
                new InMobiInterstitial.InterstitialAdListener() {
                    @Override
                    public void onAdRewardActionCompleted(InMobiInterstitial inMobiInterstitial, Map<Object, Object> map) {

                    }

                    @Override
                    public void onAdDisplayed(InMobiInterstitial inMobiInterstitial) {

                    }

                    @Override
                    public void onAdDismissed(InMobiInterstitial inMobiInterstitial) {

                    }

                    @Override
                    public void onAdInteraction(InMobiInterstitial inMobiInterstitial, Map<Object, Object> map) {

                    }

                    @Override
                    public void onAdLoadSucceeded(InMobiInterstitial inMobiInterstitial) {
                        if (inMobiInterstitial.isReady()) {
                            inMobiInterstitial.show();
                        }
                    }

                    @Override
                    public void onAdLoadFailed(InMobiInterstitial inMobiInterstitial, InMobiAdRequestStatus inMobiAdRequestStatus) {
                        Log.w("INMOBI", "Unable to load interstitial ad (error message: " +
                                inMobiAdRequestStatus.getMessage() + ")");
                    }

                    @Override
                    public void onUserLeftApplication(InMobiInterstitial inMobiInterstitial) {

                    }
                });
        mInterstitialAd.load();

    }
    @Override
    public void onResume() {
        super.onResume();
    }

    public void setTitle(String title) {
        titleTxt.setText(title.toUpperCase());
    }

    @OnClick(R.id.toolbar_back)
    public void onMBackClick() {
        onBackPressed();
    }

    @OnClick(R.id.toolbar_title)
    public void onSelectionClick() {
        sectionDetailsHolderFragment.onSelectionClick();
    }

    public OnArticleModeChangeListener getArticleModeChangeListener() {
        return articleModeChangeListener;
    }

    public void setArticleModeChangeListener(OnArticleModeChangeListener articleModeChangeListener) {
        this.articleModeChangeListener = articleModeChangeListener;
    }

    @OnClick(R.id.nightMode1)
    public void onToggleNightMode(){
        if (nightModeImg.isChecked()){
            sectionDetailsHolderFragment.onNightMode(true);
        }
        else{
            sectionDetailsHolderFragment.onNightMode(false);
        }
    }
    @OnClick(R.id.textSizeImg)
    public void onTextSizeChange(){
        sectionDetailsHolderFragment.onResizeText();
    }

    public void hideShowToolBar(ScrollState scrollState){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        android.support.v7.app.ActionBar ab= getSupportActionBar();

        if (ab == null) {
            return;
        }
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
                toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
               // toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();


                //ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
                toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                //toolbar.animate().translationY(toolbar.getTop()).setInterpolator(new AccelerateInterpolator()).start();

            }
        }

    }

}
