package com.app.outlook.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;

import com.app.outlook.R;
import com.app.outlook.fragments.HomeListFragment;
import com.app.outlook.fragments.MagazineGridView;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.IntentConstants;
import com.app.outlook.modal.Magazine;
import com.app.outlook.modal.OutlookConstants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeListingActivity extends AppBaseActivity {

    private ArrayList<Magazine> magazineList;
    private MagazineGridView magazineGridFragment;
    private HomeListFragment magazineListFragment;


    public final static int PAGES = 5;
    public final static int LOOPS = 1;
    public final static int FIRST_PAGE = 0;//PAGES * LOOPS / 2;
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    @Bind(R.id.carouselView)
    ImageButton carouselView;
    @Bind(R.id.gridView)
    ImageButton gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        setContentView(R.layout.activity_home_listing);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        carouselView.setSelected(true);
//        loadDummyData();
//        loadFragments();


    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPrefManager prefManager = SharedPrefManager.getInstance();
        prefManager.init(this);
        prefManager.setSharedData(OutlookConstants.theme, R.style.AppTheme);
        setTheme(R.style.AppTheme);
    }

    private void loadFragments() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstants.MAGAZINE_LIST, magazineList);

        magazineGridFragment = new MagazineGridView();
        magazineGridFragment.setArguments(bundle);

        magazineListFragment = new HomeListFragment();
        magazineListFragment.setArguments(bundle);

        changeFragment(magazineListFragment);
    }

    @OnClick(R.id.gridView)
    public void onGridviewClick() {
        changeFragment(magazineGridFragment);

        carouselView.setSelected(false);
        gridView.setSelected(true);
    }

    @OnClick(R.id.carouselView)
    public void onCarouselViewClick() {
        changeFragment(magazineListFragment);

        carouselView.setSelected(true);
        gridView.setSelected(false);
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.contentPanel, fragment)
                .commit();
    }



}
