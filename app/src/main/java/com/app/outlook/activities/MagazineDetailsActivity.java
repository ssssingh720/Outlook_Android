package com.app.outlook.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.app.outlook.R;
import com.app.outlook.fragments.MagazineDetailsFragment;
import com.app.outlook.fragments.SectionDetailsHolderFragment;
import com.app.outlook.modal.IntentConstants;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 21/09/15.
 */
public class MagazineDetailsActivity extends AppBaseActivity {

    public static final String EXTRA_NAME = "cheese_name";

    SectionDetailsHolderFragment sectionDetailsHolderFragment;
    MagazineDetailsFragment magazineDetailsFragment;
    private String TAG = "MagazineDetailsActivity";
    ArrayList<String> mContent = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        if (sectionDetailsHolderFragment.isRemoving()) {
                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.show(magazineDetailsFragment);
                        }
                    }
                });

        magazineDetailsFragment = new MagazineDetailsFragment();
        changeFragment(magazineDetailsFragment, false);
    }

    @OnClick(R.id.back)
    public void onMBackClick() {
        onBackPressed();
    }

    private void changeFragment(Fragment fragment, boolean backStack) {
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

    public void openSectionDetails(int cardPosition, int itemPosition) {
        sectionDetailsHolderFragment = new SectionDetailsHolderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentConstants.CARD_HOLDER_POSITION, cardPosition);
        bundle.putInt(IntentConstants.ITEM_POSITION, itemPosition);
        bundle.putStringArrayList(IntentConstants.CONTENTS, mContent);
        sectionDetailsHolderFragment.setArguments(bundle);
        changeFragment(sectionDetailsHolderFragment, true);

    }

    public ArrayList<String> getContent() {
        return mContent;
    }

    public void setContent(ArrayList<String> content) {
        this.mContent = content;
    }

}
