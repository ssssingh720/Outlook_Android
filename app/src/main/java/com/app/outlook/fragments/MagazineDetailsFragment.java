package com.app.outlook.fragments;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.app.outlook.R;
import com.app.outlook.Utils.APIMethods;
import com.app.outlook.Utils.Util;
import com.app.outlook.activities.MagazineDetailsActivity;
import com.app.outlook.modal.Category;
import com.app.outlook.modal.Data;
import com.app.outlook.modal.DetailsObject;
import com.app.outlook.modal.Item;
import com.google.gson.Gson;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 22/09/15.
 */
public class MagazineDetailsFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "MagazineDetailsFragment";
    @Bind(R.id.sectionListLyt)
    LinearLayout sectionListLyt;
    @Bind(R.id.sectionBreifListLyt)
    LinearLayout sectionBreifListLyt;
    @Bind(R.id.parallaxView)
    ParallaxScrollView parallaxView;
    TypedArray categoryIds;
    TypedArray cardIds;
    List<Category> mCategories = new ArrayList<>();
    int mSelectedCategory = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_magazine_details, null);
        ButterKnife.bind(this, mView);
        initView();
        String id = "897";
        fetchMagazineDetails(id);
        return mView;
    }

    private void initView() {
        categoryIds = getResources().obtainTypedArray(R.array.categoryIds);
        cardIds = getResources().obtainTypedArray(R.array.cardIds);
        categoryIds.recycle();
        cardIds.recycle();
    }

    private void fetchMagazineDetails(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("post_id", id);
        placeRequest(APIMethods.MAGAZINE_DETAILS, DetailsObject.class, params);
    }

    // Array of Cards
    private void loadSectionBreifListLyt(List<Data> data) {
        removeAllSectionBreifListLyt();
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // replace with array of cards
        ArrayList<String> contents = new ArrayList<>();
        for (int j = 0; j < data.size(); j++) {
            ArrayList<Item> items = data.get(j).getItem();
            if (!items.isEmpty()) {
                View view = inflater.inflate(R.layout.template_one, null);
                loadCardView(view, j, items);
            }
            for (int k = 0; k < data.get(j).getItem().size(); k++) {
                contents.add(data.get(j).getItem().get(k).getContent());
            }
        }

        ((MagazineDetailsActivity) getActivity()).setContent(contents);
    }

    private void loadCardView(View cardView, int position, ArrayList<Item> items) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout itemsLyt = (LinearLayout) cardView.findViewById(R.id.itemsLyt);
        LinearLayout subtitleLyt = (LinearLayout) cardView.findViewById(R.id.subtitleLyt);
        TextView txtTitle = (TextView) cardView.findViewById(R.id.txtTitle);
        if (true) {
            txtTitle.setText("");
        } else {
            subtitleLyt.setVisibility(View.GONE);
        }

        for (int i = 0; i < items.size(); i++) {
            View subView = inflater.inflate(R.layout.template_three, null);
            subView = loadCardItem(subView, items.get(i));
            subView.setTag("card," + position + "," + i);
            subView.setOnClickListener(this);
            itemsLyt.addView(subView);
        }
        sectionBreifListLyt.setTag("");
        sectionBreifListLyt.addView(cardView);
    }

    public void removeAllSectionBreifListLyt() {
        sectionBreifListLyt.removeAllViews();
    }

    private View loadCardItem(View view, Item data) {

        TextView subtitle = (TextView) view.findViewById(R.id.subtitle);
        TextView sub_category_name = (TextView) view.findViewById(R.id.sub_category_name);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView author = (TextView) view.findViewById(R.id.author);
        ImageView userImg = (ImageView) view.findViewById(R.id.userImg);


        if (data.getSubtitle() != null && !data.getSubtitle().isEmpty()) {
            subtitle.setText("" + data.getSubtitle());
        } else {
            subtitle.setVisibility(View.GONE);
        }
        if (data.getSubCategoryName() != null && !data.getSubCategoryName().isEmpty()) {
            sub_category_name.setText("" + data.getSubCategoryName());
        } else {
            sub_category_name.setVisibility(View.GONE);
        }
        if (data.getDescription() != null && !data.getDescription().isEmpty()) {
            description.setText("" + data.getDescription());
        } else {
            description.setVisibility(View.GONE);
        }
        if (data.getAuthor() != null && !data.getAuthor().isEmpty()) {
            author.setText("" + data.getAuthor());
        } else {
            author.setVisibility(View.GONE);
        }
        if (data.getImage() != null && !data.getImage().isEmpty()) {
            Picasso.with(getActivity()).load("imageUrl")
                    .placeholder(R.drawable.dummy_12).fit().centerCrop().into(userImg);
        } else {
            userImg.setVisibility(View.GONE);
        }

        return view;
    }

    // Top section items
    private void loadSectionListLyt() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < mCategories.size(); i++) {
            items.add(mCategories.get(i).getCategoryName().replace("_", " ").toUpperCase());
        }

        int size = items.size() % 2 == 0 ? (items.size() / 2) : ((items.size() / 2) + 1);
        size = size + 3;
        for (int i = 0; i <= size; i++) {
            if (i < items.size()) {
                View view = inflater.inflate(R.layout.details_section_row_item, null);
                ((TextView) view.findViewById(R.id.txtLeft)).setText(items.get(i));
                ((TextView) view.findViewById(R.id.txtLeft)).setTag("category," + i);
                ((TextView) view.findViewById(R.id.txtLeft)).setOnClickListener(this);

                if (i + 1 < items.size()) {
                    ((TextView) view.findViewById(R.id.txtRight)).setText(items.get(i + 1));
                    ((TextView) view.findViewById(R.id.txtRight)).setTag("category," + (i + 1));
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
    public void goUp() {
        parallaxView.smoothScrollTo(0, 0);
    }

    @Override
    public void onClick(View v) {

        String tag = (String) v.getTag();
        String[] tags = tag.split(",");

        if (tags[0].equals("category")) {
            Toast.makeText(getActivity(), tags[1] + " Position", Toast.LENGTH_SHORT).show();
            mSelectedCategory = Integer.parseInt(tags[1]);
            switch (Integer.parseInt(tags[1])) {
                case 0:
                    loadSectionBreifListLyt(mCategories.get(0).getData());
                    break;
                case 1:
                    loadSectionBreifListLyt(mCategories.get(1).getData());
                    break;
                case 2:
                    loadSectionBreifListLyt(mCategories.get(2).getData());
                    break;
                case 3:
                    loadSectionBreifListLyt(mCategories.get(3).getData());
                    break;
                case 4:
                    loadSectionBreifListLyt(mCategories.get(4).getData());
                    break;
                case 5:
                    loadSectionBreifListLyt(mCategories.get(5).getData());
                    break;
                case 6:
                    loadSectionBreifListLyt(mCategories.get(6).getData());
                    break;
                case 7:
                    loadSectionBreifListLyt(mCategories.get(7).getData());
                    break;
                case 8:
                    loadSectionBreifListLyt(mCategories.get(8).getData());
                    break;
                case 9:
                    loadSectionBreifListLyt(mCategories.get(9).getData());
                    break;
                case 10:
                    loadSectionBreifListLyt(mCategories.get(10).getData());
                    break;
                case 11:
                    loadSectionBreifListLyt(mCategories.get(11).getData());
                    break;
                case 12:
                    loadSectionBreifListLyt(mCategories.get(12).getData());
                    break;
                case 13:
                    loadSectionBreifListLyt(mCategories.get(13).getData());
                    break;
                case 14:
                    loadSectionBreifListLyt(mCategories.get(14).getData());
                    break;
                case 15:
                    loadSectionBreifListLyt(mCategories.get(15).getData());
                    break;
                case 16:
                    loadSectionBreifListLyt(mCategories.get(16).getData());
                    break;
                case 17:
                    loadSectionBreifListLyt(mCategories.get(17).getData());
                    break;
                case 18:
                    loadSectionBreifListLyt(mCategories.get(18).getData());
                    break;
                case 19:
                    loadSectionBreifListLyt(mCategories.get(19).getData());
                    break;
                case 20:
                    loadSectionBreifListLyt(mCategories.get(20).getData());
                    break;

            }
        } else if (tags[0].equals("card")) {
            for (int i = 0; i < tags.length; i++) {
                Log.d("MagazineDetails", "Tags::" + tags[i]);
            }
            Toast.makeText(getActivity(), tags[2] + " Item", Toast.LENGTH_SHORT).show();
            openSectionDetails(Integer.parseInt(tags[1]), Integer.parseInt(tags[2]));
        }
    }

    private void openSectionDetails(int cardPosition, int item) {
        ((MagazineDetailsActivity) getActivity()).openSectionDetails(cardPosition, item);
    }


    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        Log.d(TAG, "onAPIResponse APIMethod::" + apiMethod);
        Log.d(TAG, "onAPIResponse Response::" + response);
        if (apiMethod.equals(APIMethods.MAGAZINE_DETAILS)) {
            DetailsObject detailsObject = (DetailsObject) response;
            Util.saveJsonToSDCard(new Gson().toJson(detailsObject), "");
            mCategories = detailsObject.getCategories();
            loadSectionListLyt();
            loadSectionBreifListLyt(mCategories.get(0).getData());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        Log.d(TAG, "onAPIResponse APIMethod::" + apiMethod);
        Log.d(TAG, "onAPIResponse Error::" + error);
    }
}
