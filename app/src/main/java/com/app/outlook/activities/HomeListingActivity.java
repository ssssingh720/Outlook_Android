package com.app.outlook.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;

import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.model.IntentConstants;
import com.app.outlook.model.Magazine;
import com.app.outlook.R;
import com.app.outlook.fragments.HomeListFragment;
import com.app.outlook.fragments.MagazineGridView;
import com.app.outlook.model.OutlookConstants;

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
        loadDummyData();
        loadFragments();


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
    public void onGridviewClick(){
        changeFragment(magazineGridFragment);

        carouselView.setSelected(false);
        gridView.setSelected(true);
    }

    @OnClick(R.id.carouselView)
    public void onCarouselViewClick(){
        changeFragment(magazineListFragment);

        carouselView.setSelected(true);
        gridView.setSelected(false);
    }

    private void changeFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.contentPanel,fragment)
                .commit();
    }

    private void loadDummyData() {

        magazineList = new ArrayList<>();
        Magazine m1 = new Magazine();
        m1.setName("OUTLOOK");
        m1.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m1.setId(1);
        m1.setImage(R.drawable.outlook_coverpage);
        magazineList.add(m1);

        Magazine m2 = new Magazine();
        m2.setName("TRAVELLER");
        m2.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m2.setId(2);
        m2.setImage(R.drawable.outlook_traveller_dummy2);
        magazineList.add(m2);

        Magazine m3 = new Magazine();
        m3.setName("MONEY");
        m3.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m3.setId(3);
        m3.setImage(R.drawable.outlook_traveller_dummy3);
        magazineList.add(m3);

        Magazine m4 = new Magazine();
        m4.setName("HINDI");
        m4.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m4.setId(4);
        m4.setImage(R.drawable.outlook_traveller_dummy4);
        magazineList.add(m4);

        Magazine m5 = new Magazine();
        m5.setName("BUSINESS");
        m5.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m5.setId(5);
        m5.setImage(R.drawable.outlook_traveller_dummy5);
        magazineList.add(m5);

    }

}
