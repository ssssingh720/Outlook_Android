package com.app.outlook.fragments;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.app.outlook.activities.CategoryListingActivity;
import com.app.outlook.adapters.MagazineGridViewAdapter;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.IntentConstants;
import com.app.outlook.modal.Magazine;
import com.app.outlook.R;
import com.app.outlook.modal.OutlookConstants;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by srajendrakumar on 09/09/15.
 */
public class MagazineGridView extends BaseFragment{

    private ArrayList<Magazine> magazineList;
    private MagazineGridViewAdapter adapter;
    @Bind(R.id.gridView)
    GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_magazine_gridview,null);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        magazineList = (ArrayList<Magazine>) getArguments().getSerializable(IntentConstants.MAGAZINE_LIST);
        adapter = new MagazineGridViewAdapter(getActivity(),R.layout.grid_item_layout,magazineList,width);
        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(adapter);
        AlphaInAnimationAdapter animationAlphaAdapter = new AlphaInAnimationAdapter(animationAdapter);
        animationAlphaAdapter.setAbsListView(gridView);
        gridView.setAdapter(animationAlphaAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    SharedPrefManager prefManager = SharedPrefManager.getInstance();
                    prefManager.init(getActivity());
                    prefManager.setSharedData(OutlookConstants.theme, R.style.AppTheme);
                    startActivity(new Intent(getActivity(), CategoryListingActivity.class));
                }
//                if(position == 1) {
//                    SharedPrefManager prefManager = SharedPrefManager.getInstance();
//                    prefManager.init(getActivity());
//                    prefManager.setSharedData(OutlookConstants.theme, R.style.AppThemeBlue);
//                    startActivity(new Intent(getActivity(), CategoryListingActivity.class));
//                }
            }
        });
    }
}
