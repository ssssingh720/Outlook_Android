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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by srajendrakumar on 22/09/15.
 */
public class SectionDetailsHolderFragment extends BaseFragment {

    private int mSelectedItem;
    @Bind(R.id.container)
    ViewPager mPager;
    private PageAdapter mAdapter;
    private int cardHolderPosition = 0;
    private int currentPageCount = 0;
    ArrayList<String> mContents = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_section_details_holder, null);
        cardHolderPosition = getArguments().getInt(IntentConstants.CARD_HOLDER_POSITION, 0);
        currentPageCount = getArguments().getInt(IntentConstants.ITEM_POSITION, 0);
        mContents = getArguments().getStringArrayList(IntentConstants.CONTENTS);
        ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    private void initView() {
        mAdapter = new PageAdapter(getChildFragmentManager(), mContents);
        mPager.setAdapter(mAdapter);
        mPager.setPageTransformer(true, new DepthPageTransformer());
        mPager.setCurrentItem(currentPageCount);
    }


    private class PageAdapter extends FragmentStatePagerAdapter {

        ArrayList<String> mContents;

        public PageAdapter(FragmentManager fragmentManager, ArrayList<String> contents) {
            super(fragmentManager);
            mContents = contents;
        }

        @Override
        public Fragment getItem(int position) {
            final Bundle bundle = new Bundle();
            bundle.putInt(IntentConstants.POSITION, position);
            bundle.putString(IntentConstants.WEB_CONTENT, mContents.get(position));
            final SectionDetailsFragment fragment = new SectionDetailsFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return mContents.size();
        }
    }
}
