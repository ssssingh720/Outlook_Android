package com.app.outlook.fragments;

import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import com.app.outlook.R;
import com.app.outlook.Utils.CircleTransform;
import com.app.outlook.Utils.RoundedCornerTransformation;
import com.app.outlook.Utils.Util;
import com.app.outlook.activities.MagazineDetailsActivity;
import com.app.outlook.adapters.GridListAdapter;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
    TypedArray categoryIds ;
    TypedArray cardIds ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_magazine_details,null);
        ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    private void initView() {
        categoryIds = getResources().obtainTypedArray(R.array.categoryIds);
        cardIds = getResources().obtainTypedArray(R.array.cardIds);


        loadSectionListLyt();
        loadSectionBreifListLyt();

        categoryIds.recycle();
        cardIds.recycle();
    }

    private void loadSectionBreifListLyt() {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        ArrayList<String> items = new ArrayList<>();
        items.add("REGULAR");
        items.add("BUSINESS");
        items.add("COVER STORY");
        items.add("FEATURES");
        items.add("CURRENT AFFAIRS");
        items.add("EDITORS PICK");
        items.add("CARS");

        for(int i= 0;i<items.size();i++){
            View view = inflater.inflate(R.layout.template_one, null);
            loadCardView(view,i);
        }

    }

    private void loadCardView(View cardView,int position){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout itemsLyt = (LinearLayout) cardView.findViewById(R.id.itemsLyt);
        LinearLayout subtitleLyt = (LinearLayout) cardView.findViewById(R.id.subtitleLyt);

        TextView txtTitle = (TextView) cardView.findViewById(R.id.txtTitle);
        if(true){
            txtTitle.setText("");
        }else{
            subtitleLyt.setVisibility(View.GONE);
        }

        ArrayList<String> items = new ArrayList<>();
        items.add("REGULAR");
        items.add("BUSINESS");
        items.add("COVER STORY");

        for(int i= 0;i<items.size();i++){
            View subView = inflater.inflate(R.layout.template_three, null);
            subView = loadCardItem(subView,i);
            subView.setTag("card," + position+","+i);
            subView.setOnClickListener(this);
            itemsLyt.addView(subView);
        }

        sectionBreifListLyt.addView(cardView);
    }

    private View loadCardItem(View view, Object obj){

        TextView subtitle = (TextView) view.findViewById(R.id.subtitle);
        TextView sub_category_name = (TextView) view.findViewById(R.id.sub_category_name);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView author = (TextView) view.findViewById(R.id.author);
        ImageView userImg = (ImageView) view.findViewById(R.id.userImg);


        if(true){
//            subtitle.setText("");
        }else{
            subtitle.setVisibility(View.GONE);
        }
        if(true){
//            sub_category_name.setText("");
        }else{
            sub_category_name.setVisibility(View.GONE);
        }
        if(true){
//            description.setText("");
        }else{
            description.setVisibility(View.GONE);
        }
        if(true){
//            author.setText("");
        }else{
            author.setVisibility(View.GONE);
        }
        if(false){
//            Picasso.with(getActivity()).load("imageUrl")
//                    .placeholder(R.drawable.network_null_image)
//                    .error(R.drawable.network_null_image)
//                    .transform(new CircleTransform()).fit().centerCrop().into(userImg);

        }else{
            userImg.setVisibility(View.GONE);
        }

        return view;
    }

    private void loadSectionListLyt() {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        ArrayList<String> items = new ArrayList<>();
        items.add("REGULAR");
        items.add("BUSINESS");
        items.add("COVER STORY");
        items.add("FEATURES");
        items.add("CURRENT AFFAIRS");
        items.add("EDITORS PICK");
        items.add("CARS");

        int size = items.size() % 2 == 0 ? (items.size()/2 ) : ((items.size()/2) +1) ;
        size = size+3;
        for(int i =0;i<= size;i++){
            if(i< items.size()) {
                View view = inflater.inflate(R.layout.details_section_row_item, null);
                ((TextView) view.findViewById(R.id.txtLeft)).setText(items.get(i));
//                ((TextView) view.findViewById(R.id.txtLeft)).setId(categoryIds.getResourceId(i, -1));
                ((TextView) view.findViewById(R.id.txtLeft)).setTag("category,"+i);
                ((TextView) view.findViewById(R.id.txtLeft)).setOnClickListener(this);

                if (i + 1 < items.size()) {
                    ((TextView) view.findViewById(R.id.txtRight)).setText(items.get(i + 1));
//                    ((TextView) view.findViewById(R.id.txtRight)).setId(categoryIds.getResourceId(i + 1, -1));
                    ((TextView) view.findViewById(R.id.txtRight)).setTag("category,"+(i + 1));
                    ((TextView) view.findViewById(R.id.txtRight)).setOnClickListener(this);
                } else {
                    ((TextView) view.findViewById(R.id.txtRight)).setText("");
                }

                sectionListLyt.addView(view);

                i++;
            }
        }

    }

    @OnClick(R.id.goUp)
    public void goUp(){
        parallaxView.smoothScrollTo(0,0);
    }
    @Override
    public void onClick(View v) {

        String tag = (String) v.getTag();
        String[] tags = tag.split(",");

        if(tags[0].equals("category")){
            Toast.makeText(getActivity(),tags[1]+"",Toast.LENGTH_SHORT).show();
        }else if(tags[0].equals("card")) {
            Toast.makeText(getActivity(),tags[2]+"",Toast.LENGTH_SHORT).show();
        }


//                ((MagazineDetailsActivity) getActivity()).openSectionDetails(0);
    }
}
