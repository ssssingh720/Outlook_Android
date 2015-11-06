package com.app.outlook.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.app.outlook.R;
import com.app.outlook.fragments.SectionDetailsHolderFragment;
import com.app.outlook.modal.IntentConstants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 04/11/15.
 */
public class ArticleDetailsActivity extends AppBaseActivity {

    private SectionDetailsHolderFragment sectionDetailsHolderFragment;
    @Bind(R.id.toolbar_title)
    TextView titleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_details);
        ButterKnife.bind(this);

        sectionDetailsHolderFragment = new SectionDetailsHolderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentConstants.CATEGORY_POSITION, getIntent().getIntExtra(IntentConstants.CATEGORY_POSITION,0));
        bundle.putInt(IntentConstants.CARD_POSITION, getIntent().getIntExtra(IntentConstants.CARD_POSITION,0));
        bundle.putString(IntentConstants.ISSUE_ID, getIntent().getStringExtra(IntentConstants.ISSUE_ID));
        bundle.putString(IntentConstants.MAGAZINE_ID, getIntent().getStringExtra(IntentConstants.MAGAZINE_ID));
        sectionDetailsHolderFragment.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_enter, R.anim.slide_exit);
        transaction.add(R.id.contentPanel, sectionDetailsHolderFragment);

        transaction.commit();
    }

    public void setTitle(String title){
        titleTxt.setText(title);
    }

    @OnClick(R.id.toolbar_back)
    public void onMBackClick() {
        onBackPressed();
    }

    @OnClick(R.id.toolbar_title)
    public void onSelectionClick() {
        sectionDetailsHolderFragment.onSelectionClick();
    }
}