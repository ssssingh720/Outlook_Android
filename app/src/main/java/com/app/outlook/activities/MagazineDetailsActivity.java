package com.app.outlook.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.LinearLayout;

import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.app.outlook.R;
import com.app.outlook.fragments.MagazineDetailsFragment;
import com.app.outlook.fragments.SectionDetailsHolderFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 21/09/15.
 */
public class MagazineDetailsActivity extends AppBaseActivity{

    public static final String EXTRA_NAME = "cheese_name";

    SectionDetailsHolderFragment sectionDetailsHolderFragment;
    MagazineDetailsFragment magazineDetailsFragment;

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
                        if(sectionDetailsHolderFragment.isRemoving()){
                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.show(magazineDetailsFragment);
                        }
                    }
                });

        magazineDetailsFragment = new MagazineDetailsFragment();
        changeFragment(magazineDetailsFragment,false);
    }

    @OnClick(R.id.back)
    public void onMBackClick(){
        onBackPressed();
    }

    private void changeFragment(Fragment fragment, boolean backStack){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_enter, R.anim.slide_exit);
        transaction.add(R.id.contentPanel, fragment);
        if(backStack) {
            transaction.hide(magazineDetailsFragment);
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void openSectionDetails(int currnetPage){
        sectionDetailsHolderFragment = new SectionDetailsHolderFragment();
        changeFragment(sectionDetailsHolderFragment,true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_listing, menu);
        return true;
    }
}
