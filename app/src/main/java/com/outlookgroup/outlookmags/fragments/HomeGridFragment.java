package com.outlookgroup.outlookmags.fragments;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.outlookgroup.outlookmags.OutLookApplication;
import com.outlookgroup.outlookmags.R;
import com.outlookgroup.outlookmags.activities.IssuesListingActivity;
import com.outlookgroup.outlookmags.adapters.HomeGridViewAdapter;
import com.outlookgroup.outlookmags.modal.IntentConstants;
import com.outlookgroup.outlookmags.modal.MagazineTypeVo;
import com.google.android.gms.analytics.HitBuilders;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by srajendrakumar on 09/09/15.
 */
public class HomeGridFragment extends BaseFragment {

    @Bind(R.id.gridView)
    GridView gridView;
    private ArrayList<MagazineTypeVo> magazineList;
    private HomeGridViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_magazine_gridview, null);
        ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    private void initView() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        /*DisplayMetrics matrics = getResources().getDisplayMetrics();
        Log.i("widthMatrixyz",matrics.xdpi+":"+matrics.ydpi);


        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.i("widthMatrix",metrics.densityDpi+"" );

        switch(metrics.densityDpi){
            case DisplayMetrics.DENSITY_LOW:
                Log.i("widthMatrixLow",metrics.xdpi+"::"+metrics.ydpi);
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Log.i("widthMatrixMEDIUM",metrics.xdpi+"::"+metrics.ydpi);
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Log.i("widthMatrixHIgh",metrics.xdpi+"::"+metrics.ydpi);
                break;
        }*/
        magazineList = (ArrayList<MagazineTypeVo>) getArguments().getSerializable(IntentConstants.MAGAZINE_LIST);
        adapter = new HomeGridViewAdapter(getActivity(), R.layout.grid_item_layout, magazineList, width);
        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(adapter);
        AlphaInAnimationAdapter animationAlphaAdapter = new AlphaInAnimationAdapter(animationAdapter);
        animationAlphaAdapter.setAbsListView(gridView);
        gridView.setAdapter(animationAlphaAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* SharedPrefManager prefManager = SharedPrefManager.getInstance();
                prefManager.init(getActivity());
                switch (position){
                    case 0:
                        prefManager.setSharedData(OutlookConstants.theme, R.style.AppTheme);
                        break;
                    case 1:
                        prefManager.setSharedData(OutlookConstants.theme, R.style.AppThemeBlue);
                        break;
                    case 2:
                        prefManager.setSharedData(OutlookConstants.theme, R.style.AppThemeOrange);
                        break;
                    case 3:
                        prefManager.setSharedData(OutlookConstants.theme, R.style.AppThemeGreen);
                        break;
                    case 4:
                        prefManager.setSharedData(OutlookConstants.theme, R.style.AppThemeYellow);
                        break;
                    case 5:
                        prefManager.setSharedData(OutlookConstants.theme, R.style.AppThemePurple);
                        break;
                    default:
                        prefManager.setSharedData(OutlookConstants.theme, R.style.AppTheme);
                        break;
                }*/
               /* if (position == 0) {
                    prefManager.setSharedData(OutlookConstants.theme, R.style.AppTheme);
                } else if (position == 1) {
                    prefManager.setSharedData(OutlookConstants.theme, R.style.AppThemeBlue);
                }*/

                ArrayList<String> subscriptionIDList = new ArrayList<>();
                    subscriptionIDList.add(magazineList.get(position).getQtly());
//                    subscriptionIDList.add(magazineList.get(currentPosition).getHalyYearly());
//                    subscriptionIDList.add(magazineList.get(currentPosition).getAnnual());
            //    subscriptionIDList.add("outlook.two");
                subscriptionIDList.add("outlook.five");
                subscriptionIDList.add("outlook.five");

                Intent intent = new Intent(getActivity(),IssuesListingActivity.class);
                intent.putExtra(IntentConstants.TYPE, magazineList.get(position).getId());
                intent.putExtra(IntentConstants.MAGAZINE_NAME, magazineList.get(position).getName());
                intent.putExtra(IntentConstants.SUBSCRIPTION_IDS, subscriptionIDList);
                startActivity(intent);
                OutLookApplication.tracker().send(new HitBuilders.EventBuilder("Issues", "")
                        .setLabel(magazineList.get(position).getName()+magazineList.get(position).getId())
                        .build());
            }
        });
    }
}
