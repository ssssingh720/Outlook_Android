package com.app.outlook.fragments;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.outlook.R;
import com.app.outlook.activities.HomeListingActivity;
import com.app.outlook.activities.IssuesListingActivity;
import com.app.outlook.listener.OnPageClickedListener;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.IntentConstants;
import com.app.outlook.modal.Magazine;
import com.app.outlook.modal.MagazineTypeVo;
import com.app.outlook.modal.OutlookConstants;
import com.app.outlook.views.CarouselLinearLayout;
import com.app.outlook.views.CirclePageIndicator;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by srajendrakumar on 09/09/15.
 */
public class HomeListFragment extends BaseFragment implements OnPageClickedListener {

    @Bind(R.id.view_pager)
    public ViewPager mViewPager;
    @Bind(R.id.page_indicator)
    public CirclePageIndicator pageIndicator;
    public ListPagerAdapter adapter;
    @Bind(R.id.magazineName)
    TextView magazineName;
    @Bind(R.id.magazineDescp)
    TextView magazineDescp;
    private CarouselLinearLayout cur = null;
    private CarouselLinearLayout next = null;
    private int currentPosition = 0;
    private ArrayList<MagazineTypeVo> magazineList;
    ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            currentPosition = position;
            if (positionOffset >= 0f && positionOffset <= 1f) {
                cur = adapter.getRootView(position);
                cur.setScaleBoth(HomeListingActivity.BIG_SCALE
                        - HomeListingActivity.DIFF_SCALE * positionOffset);
                if (position < adapter.getCount() - 1) {
                    next = adapter.getRootView(position + 1);
                    next.setScaleBoth(HomeListingActivity.SMALL_SCALE
                            + HomeListingActivity.DIFF_SCALE * positionOffset);
                }

            }
        }

        @Override
        public void onPageSelected(int position) {
            currentPosition = position;
            magazineName.setText(magazineList.get(position).getName().toUpperCase());
            magazineDescp.setText(magazineList.get(position).getDescription());
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.d("MyPagerAdapter", "onPageScrollStateChanged::");
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list, null);
        ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    private void initView() {
        magazineList = (ArrayList<MagazineTypeVo>) getArguments().getSerializable(IntentConstants.MAGAZINE_LIST);
        adapter = new ListPagerAdapter(getActivity(), getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(mPageChangeListener);
        pageIndicator.setOnPageChangeListener(mPageChangeListener);

        pageIndicator.setViewPager(mViewPager);


        // Set current item to the middle page so we can fling to both
        // directions left and right
        mViewPager.setCurrentItem(HomeListingActivity.FIRST_PAGE);

        // Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        mViewPager.setOffscreenPageLimit(5);

        // Set margin for pages as a negative number, so a part of next and
        // previous pages will be showed
        mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.view_pager_padding));
//        mViewPager.setPadding(40, 0, 40, 0);
//        mViewPager.setClipToPadding(false);
//        mViewPager.setClipChildren(false);
//        mViewPager.setPageMargin(-180);

        magazineName.setText(magazineList.get(0).getName().toUpperCase());
        magazineDescp.setText(magazineList.get(0).getDescription());

    }

    @Override
    public void onPageClicked() {
        SharedPrefManager prefManager = SharedPrefManager.getInstance();
                prefManager.init(getActivity());
                if (currentPosition == 0) {
                    prefManager.setSharedData(OutlookConstants.theme, R.style.AppTheme);
                } else if (currentPosition == 1) {
                    prefManager.setSharedData(OutlookConstants.theme, R.style.AppThemeBlue);
                }

        ArrayList<String> subscriptionIDList = new ArrayList<>();
//                    subscriptionIDList.add(magazineList.get(currentPosition).getQtly());
//                    subscriptionIDList.add(magazineList.get(currentPosition).getHalyYearly());
//                    subscriptionIDList.add(magazineList.get(currentPosition).getAnnual());
        subscriptionIDList.add("outlook.test.subscription");
        subscriptionIDList.add("outlook.test.subscription");
        subscriptionIDList.add("outlook.test.subscription");

        Intent intent = new Intent(getActivity(),IssuesListingActivity.class);
        intent.putExtra(IntentConstants.TYPE,magazineList.get(currentPosition).getId());
        intent.putExtra(IntentConstants.SUBSCRIPTION_IDS,subscriptionIDList);
        startActivity(intent);

    }

    public class ListPagerAdapter extends FragmentPagerAdapter {

        private FragmentManager fm;
        private float scale;

        public ListPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            // make the first pager bigger than others
            if (position == HomeListingActivity.FIRST_PAGE)
                scale = HomeListingActivity.BIG_SCALE;
            else
                scale = HomeListingActivity.SMALL_SCALE;

            position = position % HomeListingActivity.PAGES;
            Bundle bundle = new Bundle();
            bundle.putString(IntentConstants.MAGAZINE, new Gson().toJson(magazineList.get(position)));
            HomeListItemFragment listFragment = (HomeListItemFragment) HomeListItemFragment.newInstance(getActivity(), position, scale);
            listFragment.setOnPageClickedListener(HomeListFragment.this);
            listFragment.setArguments(bundle);

            return listFragment;
        }

        @Override
        public int getCount() {
            return HomeListingActivity.PAGES * HomeListingActivity.LOOPS;
        }

        public CarouselLinearLayout getRootView(int position) {

            return (CarouselLinearLayout) fm.findFragmentByTag(this.getFragmentTag(position))
                    .getView().findViewById(R.id.root);
        }

        public String getFragmentTag(int position) {
            return "android:switcher:" + mViewPager.getId() + ":" + position;
        }
    }
}
