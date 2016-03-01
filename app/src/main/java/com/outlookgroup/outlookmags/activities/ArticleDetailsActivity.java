package com.outlookgroup.outlookmags.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.internal.Logger;
import com.facebook.share.model.AppInviteContent;
import com.outlookgroup.outlookmags.R;
import com.outlookgroup.outlookmags.Utils.Util;
import com.outlookgroup.outlookmags.fragments.SectionDetailsHolderFragment;
import com.outlookgroup.outlookmags.listener.OnArticleModeChangeListener;
import com.outlookgroup.outlookmags.manager.SharedPrefManager;
import com.outlookgroup.outlookmags.modal.IntentConstants;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.outlookgroup.outlookmags.modal.OutlookConstants;
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
    OnArticleModeChangeListener articleModeChangeListener;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.scale_exit);
        setContentView(R.layout.activity_article_details);
        ButterKnife.bind(this);
        initToolBar();

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        android.support.v7.app.ActionBar ab= getSupportActionBar();
*/
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
       /* if (getIntent().getStringExtra(IntentConstants.ADMIN_MAGAZINE)!=null){
            bundle.putString(IntentConstants.ADMIN_MAGAZINE, getIntent().getStringExtra(IntentConstants.ADMIN_MAGAZINE));
        }*/
        sectionDetailsHolderFragment.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_enter, R.anim.slide_exit);
        transaction.add(R.id.contentPanel, sectionDetailsHolderFragment);

        transaction.commit();
    }


@OnClick(R.id.shareArticle)
public void shareArticle(){
    if (Util.isNetworkOnline(ArticleDetailsActivity.this)) {
        //postFB();
        postToFacebook();
        //tryAnotherWay();
    }
    else{
        showToast(getResources().getString(R.string.no_internet));
    }

    /*Intent sharingIntent = new Intent(Intent.ACTION_SEND);
    sharingIntent.setType("text*//*");
    sharingIntent.putExtra(Intent.EXTRA_TEXT, sectionDetailsHolderFragment.getShareData());
    startActivity(Intent.createChooser(sharingIntent, "Share Article"));*/
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

    public void initToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        android.support.v7.app.ActionBar ab= getSupportActionBar();
        if (!ab.isShowing()) {
            ab.show();
            toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article_menu, menu);
        MenuItem nightMode = menu.findItem(R.id.menu_nightMode);
        if (SharedPrefManager.getInstance().getSharedDataBoolean(IntentConstants.IS_NIGHT_MODE)){
            nightMode.setTitle("Day mode");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_nightMode:
                if (item.getTitle().equals("Night mode")) {
                    SharedPrefManager.getInstance().setSharedData(IntentConstants.IS_NIGHT_MODE, true);
                    sectionDetailsHolderFragment.onNightMode(true);
                    item.setTitle("Day mode");
                }
                else if(item.getTitle().equals("Day mode")){
                    SharedPrefManager.getInstance().setSharedData(IntentConstants.IS_NIGHT_MODE,false);
                    sectionDetailsHolderFragment.onNightMode(false);
                    item.setTitle("Night mode");
                }

                return true;
            case R.id.menu_fontSize:
                sectionDetailsHolderFragment.onResizeText();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    private void postToFacebook() {
        callbackManager = CallbackManager.Factory.create();
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(sectionDetailsHolderFragment.getShareData())
                    .setContentDescription(
                            sectionDetailsHolderFragment.getShareBuyLine())
                    .setContentUrl(Uri.parse(OutlookConstants.APP_LINK))
                    .build();

            shareDialog.show(ArticleDetailsActivity.this,linkContent);
        }
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager!=null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

}
