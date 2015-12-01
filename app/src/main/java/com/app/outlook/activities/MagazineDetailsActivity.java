package com.app.outlook.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.app.outlook.R;
import com.app.outlook.fragments.MagazineDetailsFragment;
import com.app.outlook.fragments.SectionDetailsHolderFragment;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.Category;
import com.app.outlook.modal.IntentConstants;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 21/09/15.
 */
public class MagazineDetailsActivity extends AppBaseActivity {

    public static final String EXTRA_NAME = "cheese_name";

    MagazineDetailsFragment magazineDetailsFragment;
    ArrayList<String> mContent = new ArrayList<>();
    private String TAG = "MagazineDetailsActivity";
    String magazineID,magazineTitle;
    @Bind(R.id.toolbar_title)
    ImageView toolbar_title;
    @Bind(R.id.shareImg)
    ImageView shareImg;
    CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.scale_exit);
        setContentView(R.layout.activity_fragment);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        magazineDetailsFragment = new MagazineDetailsFragment();
        magazineID = getIntent().getStringExtra(IntentConstants.MAGAZINE_ID);
        magazineTitle=getIntent().getStringExtra(IntentConstants.MAGAZINE_NAME);
        setLogo();

        String issueID = getIntent().getStringExtra(IntentConstants.ISSUE_ID);
        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.MAGAZINE_ID, magazineID);
        bundle.putString(IntentConstants.MAGAZINE_NAME, magazineTitle);
        bundle.putString(IntentConstants.ISSUE_ID, issueID);
        bundle.putBoolean(IntentConstants.IS_PURCHASED, getIntent().getBooleanExtra(IntentConstants.IS_PURCHASED, false));
        magazineDetailsFragment.setArguments(bundle);
        changeFragment(magazineDetailsFragment, false);
        if (getIntent().getBooleanExtra(IntentConstants.IS_PURCHASED, false)){
            shareImg.setVisibility(View.VISIBLE);
        }
        else{
            shareImg.setVisibility(View.GONE);
        }
    }

    private void setLogo() {
        if(Integer.parseInt(magazineID) == 0){
            toolbar_title.setImageResource(R.drawable.logo_outlook);
        }
    }


    @OnClick(R.id.back)
    public void onMBackClick() {
        onBackPressed();
    }

    public void changeFragment(Fragment fragment, boolean backStack) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_enter, R.anim.slide_exit);
        transaction.add(R.id.contentPanel, fragment);
        if (backStack) {
            transaction.hide(magazineDetailsFragment);
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
@OnClick(R.id.shareImg)
public void onShareIssue(){
    postToFacebook();
}
    public void openSectionDetails(int categoryPosition, int cardPosition) {

        Intent intent = new Intent(MagazineDetailsActivity.this, ArticleDetailsActivity.class);
        intent.putExtra(IntentConstants.CATEGORY_POSITION, categoryPosition);
        intent.putExtra(IntentConstants.CARD_POSITION, cardPosition);
        intent.putExtra(IntentConstants.ISSUE_ID, getIntent().getStringExtra(IntentConstants.ISSUE_ID));
        intent.putExtra(IntentConstants.IS_PURCHASED, getIntent().getBooleanExtra(IntentConstants.IS_PURCHASED, false));

        startActivity(intent);
    }

    public ArrayList<String> getContent() {
        return mContent;
    }

    public void setContent(ArrayList<String> content) {
        this.mContent = content;
    }
    private void postToFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
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
                    .setContentTitle("Hello Facebook")
                    .setContentDescription(
                            "The 'Hello Facebook' sample  showcases simple Facebook integration")
                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                    .build();

            shareDialog.show(linkContent);
        }
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
