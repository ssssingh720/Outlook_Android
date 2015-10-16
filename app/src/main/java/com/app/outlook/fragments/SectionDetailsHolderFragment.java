package com.app.outlook.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.app.outlook.R;
import com.app.outlook.modal.IntentConstants;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by srajendrakumar on 22/09/15.
 */
public class SectionDetailsHolderFragment extends BaseFragment{

    private int mSelectedItem;
    @Bind(R.id.container)
    ViewPager mPager;
    private PageAdapter mAdapter;
    private int pageCount = 10;
    private int currentPageCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_section_details_holder,null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        mAdapter = new PageAdapter(getChildFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.setPageTransformer(true,  new DepthPageTransformer());
        mPager.setCurrentItem(currentPageCount);

    }



    private class PageAdapter extends FragmentStatePagerAdapter {

        public PageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            final Bundle bundle = new Bundle();
            bundle.putInt(IntentConstants.POSITION, position);

            final SectionDetailsFragment fragment = new SectionDetailsFragment();
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return pageCount;
        }

    }

}
