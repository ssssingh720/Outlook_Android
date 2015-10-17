package com.app.outlook.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import com.android.volley.VolleyError;
import com.app.outlook.R;
import com.app.outlook.Utils.APIMethods;
import com.app.outlook.activities.MagazineDetailsActivity;
import com.app.outlook.adapters.GridListAdapter;
import com.app.outlook.modal.DetailsObject;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 22/09/15.
 */
public class MagazineDetailsFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG = "MagazineDetailsFragment";
    @Bind(R.id.sectionListLyt)
    LinearLayout sectionListLyt;
    @Bind(R.id.sectionBreifListLyt)
    LinearLayout sectionBreifListLyt;
    @Bind(R.id.parallaxView)
    ParallaxScrollView parallaxView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_magazine_details,null);
        ButterKnife.bind(this, mView);
        initView();
        fetchMagazineDetails();
        return mView;
    }

    private void fetchMagazineDetails() {
        HashMap<String, String> params = new HashMap<>();
        params.put("post_id","864");
        placeRequest(APIMethods.MAGAZINE_DETAILS, DetailsObject.class, params);
    }

    private void initView() {
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

    private void loadSectionListLyt(ArrayList<String> items) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

//        ArrayList<String> items = new ArrayList<>();
//        items.add("REGULAR");
//        items.add("BUSINESS");
//        items.add("COVER STORY");
//        items.add("FEATURES");
//        items.add("CURRENT AFFAIRS");
//        items.add("EDITORS PICK");
//        items.add("CARS");

        ArrayList<Integer> itemsIds = new ArrayList<Integer>();
        itemsIds.add(R.id.category1);
        itemsIds.add(R.id.category2);
        itemsIds.add(R.id.category3);
        itemsIds.add(R.id.category4);
        itemsIds.add(R.id.category5);
        itemsIds.add(R.id.category6);
        itemsIds.add(R.id.category7);
        itemsIds.add(R.id.category8);
        itemsIds.add(R.id.category9);
        itemsIds.add(R.id.category10);
        itemsIds.add(R.id.category11);
        itemsIds.add(R.id.category12);


        int size = items.size() % 2 == 0 ? (items.size()/2 ) : ((items.size()/2) +1) ;
        size = size+3;
        Log.v("List","size : "+size);
        for(int i =0;i<= size;i++){
            if(i< items.size()) {
                View view = inflater.inflate(R.layout.details_section_row_item, null);
                ((TextView) view.findViewById(R.id.txtLeft)).setText(items.get(i));
                ((TextView) view.findViewById(R.id.txtLeft)).setId(itemsIds.get(i));
                ((TextView) view.findViewById(itemsIds.get(i))).setOnClickListener(this);

                if (i + 1 < items.size()) {
                    ((TextView) view.findViewById(R.id.txtRight)).setText(items.get(i + 1));
                    ((TextView) view.findViewById(R.id.txtRight)).setId(itemsIds.get(i + 1));
                    ((TextView) view.findViewById(itemsIds.get(i + 1))).setOnClickListener(this);
                } else {
                    ((TextView) view.findViewById(R.id.txtRight)).setText("");
                }

                sectionListLyt.addView(view);

                i++;
            }
            Log.v("List", "i++ : " + i);
        }

    }

    @OnClick(R.id.goUp)
    public void goUp(){
        parallaxView.smoothScrollTo(0,0);
    }
    @Override
    public void onClick(View v) {
        ((MagazineDetailsActivity)getActivity()).openSectionDetails(0);
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        Log.d(TAG, "onAPIResponse APIMethod::" + apiMethod);
        Log.d(TAG,"onAPIResponse Response::" + response);
        if(apiMethod.equals(APIMethods.MAGAZINE_DETAILS)) {
            DetailsObject detailsObject = (DetailsObject) response;
            ArrayList<String> sections = new ArrayList<>();
            for(int i=0; i< detailsObject.getCategories().size(); i++){
                sections.add(detailsObject.getCategories().get(i).getCategoryName().replace("_", " ").toUpperCase());
            }

            loadSectionListLyt(sections);
        }

    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        Log.d(TAG, "onAPIResponse APIMethod::" + apiMethod);
        Log.d(TAG,"onAPIResponse Error::" + error);
    }
}
