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
import com.app.outlook.activities.CategoryListingActivity;
import com.app.outlook.model.IntentConstants;
import com.app.outlook.model.Magazine;
import com.app.outlook.views.CarouselLinearLayout;
import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeListItemFragment extends BaseFragment {

    @Bind(R.id.magazineImg)
    ImageView magazineImg;
    Magazine magazine;

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
        view  = inflater.inflate(R.layout.fragment_list_item, container, false);
        ButterKnife.bind(this,view);

        CarouselLinearLayout root = (CarouselLinearLayout) view.findViewById(R.id.root);
        float scale = this.getArguments().getFloat("scale");
        root.setScaleBoth(scale);

        init();
        return view;
    }

    private void init() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        CardView.LayoutParams lp = (CardView.LayoutParams) magazineImg.getLayoutParams();
        lp.width = (width- Util.dipToPixels(getActivity(),150));
        lp.height = (int) ((width- Util.dipToPixels(getActivity(), 150))*1.4);
        magazineImg.setLayoutParams(lp);

        magazine =new Gson().fromJson(getArguments().getString(IntentConstants.MAGAZINE), Magazine.class);

        Log.v("MyLog","id -- "+magazine.getId());
        magazineImg.setImageResource(magazine.getImage());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(magazine.getId() == 1)
                startActivity(new Intent(getActivity(), CategoryListingActivity.class));
            }
        });

    }
}
