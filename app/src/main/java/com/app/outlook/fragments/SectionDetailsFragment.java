package com.app.outlook.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.app.outlook.R;
import com.app.outlook.activities.MagazineDetailsActivity;
import com.app.outlook.model.IntentConstants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 22/09/15.
 */
public class SectionDetailsFragment extends BaseFragment {


    @Bind(R.id.containerLyt)
    LinearLayout containerLyt;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_section_details,null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        int position = getArguments().getInt(IntentConstants.POSITION);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = null;
        if(position%2 == 0){
            v = inflater.inflate(R.layout.template_four,null);
        }else{
            v = inflater.inflate(R.layout.template_five,null);
        }

        containerLyt.addView(v, 0);
    }

    @OnClick(R.id.goUp)
    public void goUp(){
        scrollView.smoothScrollTo(0, 0);
    }
}

