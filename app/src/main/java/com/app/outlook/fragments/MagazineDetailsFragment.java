package com.app.outlook.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.outlook.R;
import com.app.outlook.activities.MagazineDetailsActivity;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 22/09/15.
 */
public class MagazineDetailsFragment extends BaseFragment implements View.OnClickListener{

    @Bind(R.id.sectionListLyt)
    LinearLayout sectionListLyt;
    @Bind(R.id.sectionBreifListLyt)
    LinearLayout sectionBreifListLyt;
    @Bind(R.id.parallaxView)
    ParallaxScrollView parallaxView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_magazine_details,null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        loadSectionListLyt();
        loadSectionBreifListLyt();
    }

    private void loadSectionBreifListLyt() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.template_one, null);
        view.setOnClickListener(this);
        View view1 = inflater.inflate(R.layout.template_one, null);
        view1.setOnClickListener(this);
        View view2 = inflater.inflate(R.layout.template_one,null);
        view2.setOnClickListener(this);
        View view3 = inflater.inflate(R.layout.template_one,null);
        view3.setOnClickListener(this);

        sectionBreifListLyt.addView(view);
        sectionBreifListLyt.addView(view1);
        sectionBreifListLyt.addView(view2);
        sectionBreifListLyt.addView(view3);
    }

    private void loadSectionListLyt() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.details_section_row_item, null);
        ((TextView)view.findViewById(R.id.txtLeft)).setText("REGULAR");
        ((TextView)view.findViewById(R.id.txtLeft)).setTypeface(Typeface.DEFAULT_BOLD);
        ((TextView)view.findViewById(R.id.txtRight)).setText("BUSINESS");
        View view1 = inflater.inflate(R.layout.details_section_row_item, null);
        ((TextView)view1.findViewById(R.id.txtLeft)).setText("COVER STORY");
        ((TextView)view1.findViewById(R.id.txtRight)).setText("FEATURES");
        View view2 = inflater.inflate(R.layout.details_section_row_item,null);
        ((TextView)view2.findViewById(R.id.txtLeft)).setText("CURRENT AFFAIRS");
        ((TextView)view2.findViewById(R.id.txtRight)).setText("EDITORS PICK");
        View view3 = inflater.inflate(R.layout.details_section_row_item,null);
        ((TextView)view3.findViewById(R.id.txtLeft)).setText("CARS");
        ((TextView)view3.findViewById(R.id.txtRight)).setText("");

        sectionListLyt.addView(view);
        sectionListLyt.addView(view1);
        sectionListLyt.addView(view2);
        sectionListLyt.addView(view3);
    }

    @OnClick(R.id.goUp)
    public void goUp(){
        parallaxView.smoothScrollTo(0,0);
    }
    @Override
    public void onClick(View v) {
        ((MagazineDetailsActivity)getActivity()).openSectionDetails(0);
    }
}
