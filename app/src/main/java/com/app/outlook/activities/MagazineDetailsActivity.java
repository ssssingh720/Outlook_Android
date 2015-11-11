package com.app.outlook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;

import com.app.outlook.R;
import com.app.outlook.fragments.MagazineDetailsFragment;
import com.app.outlook.fragments.SectionDetailsHolderFragment;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.Category;
import com.app.outlook.modal.IntentConstants;

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
    String magazineID;
    @Bind(R.id.toolbar_title)
    ImageView toolbar_title;

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
        setLogo();

        String issueID = getIntent().getStringExtra(IntentConstants.ISSUE_ID);
        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.MAGAZINE_ID, magazineID);
        bundle.putString(IntentConstants.ISSUE_ID, issueID);
        magazineDetailsFragment.setArguments(bundle);
        changeFragment(magazineDetailsFragment, false);
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

    public void openSectionDetails(int categoryPosition, int cardPosition) {

        Intent intent = new Intent(MagazineDetailsActivity.this, ArticleDetailsActivity.class);
        intent.putExtra(IntentConstants.CATEGORY_POSITION, categoryPosition);
        intent.putExtra(IntentConstants.CARD_POSITION, cardPosition);
        intent.putExtra(IntentConstants.ISSUE_ID, getIntent().getStringExtra(IntentConstants.ISSUE_ID));
        startActivity(intent);
    }

    public ArrayList<String> getContent() {
        return mContent;
    }

    public void setContent(ArrayList<String> content) {
        this.mContent = content;
    }

}
