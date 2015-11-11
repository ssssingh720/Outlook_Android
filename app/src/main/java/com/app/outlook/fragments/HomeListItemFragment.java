package com.app.outlook.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.outlook.R;
import com.app.outlook.Utils.Util;
import com.app.outlook.activities.IssuesListingActivity;
import com.app.outlook.listener.OnPageClickedListener;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.IntentConstants;
import com.app.outlook.modal.Magazine;
import com.app.outlook.modal.MagazineTypeVo;
import com.app.outlook.modal.OutlookConstants;
import com.app.outlook.views.CarouselLinearLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeListItemFragment extends BaseFragment {

    @Bind(R.id.magazineImg)
    ImageView magazineImg;
    MagazineTypeVo magazine;
    @Bind(R.id.cardView)
    CardView cardView;
    private int currentPosition = 0;
    private OnPageClickedListener onPageClickedListener;

    public static Fragment newInstance(Context context, int pos,
                                       float scale) {
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        b.putFloat("scale", scale);
        return Fragment.instantiate(context, HomeListItemFragment.class.getName(), b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list_item, container, false);
        ButterKnife.bind(this, mView);

        CarouselLinearLayout root = (CarouselLinearLayout) mView.findViewById(R.id.root);
        float scale = this.getArguments().getFloat("scale");
        currentPosition = this.getArguments().getInt("pos");
        root.setScaleBoth(scale);

        init();
        return mView;
    }

    private void init() {
        float restingElevation = cardView.getCardElevation(); // keep the default
        Log.d("elevation", "resting: " + restingElevation);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        CardView.LayoutParams lp = (CardView.LayoutParams) magazineImg.getLayoutParams();
        lp.width = (width - Util.dipToPixels(getActivity(), 150));
        lp.height = (int) ((width - Util.dipToPixels(getActivity(), 150)) * 1.4);
        magazineImg.setLayoutParams(lp);

        magazine = new Gson().fromJson(getArguments().getString(IntentConstants.MAGAZINE), MagazineTypeVo.class);
        if (!magazine.getCoverImage().isEmpty())
            Picasso.with(getActivity()).load(magazine.getCoverImage()).into(magazineImg);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onPageClickedListener.onPageClicked();
            }
        });

    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public OnPageClickedListener getOnPageClickedListener() {
        return onPageClickedListener;
    }

    public void setOnPageClickedListener(OnPageClickedListener onPageClickedListener) {
        this.onPageClickedListener = onPageClickedListener;
    }
}
