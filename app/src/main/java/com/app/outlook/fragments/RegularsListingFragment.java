package com.app.outlook.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.outlook.R;
import com.app.outlook.Utils.APIMethods;
import com.app.outlook.Utils.Util;
import com.app.outlook.activities.ArticleDetailsActivity;
import com.app.outlook.manager.SessionManager;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.Card;
import com.app.outlook.modal.Category;
import com.app.outlook.modal.IntentConstants;
import com.app.outlook.modal.MagazineDetailsVo;
import com.app.outlook.modal.MagazineTypeVo;
import com.app.outlook.modal.OutlookConstants;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.squareup.picasso.Picasso;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 22/09/15.
 */
public class RegularsListingFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "MagazineDetailsFragment";
    @Bind(R.id.sectionBreifListLyt)
    LinearLayout sectionBreifListLyt;
    @Bind(R.id.bottom_holder)
    RelativeLayout mBottomHolder;

    TypedArray categoryIds;
    TypedArray cardIds;
    List<Category> mCategories = new ArrayList<>();
    int mSelectedCategory = 0,mSelectedSubCategory = 0;
    private String magazineID;
    private String root;
    private LoadToast loadToast;
    private MagazineDetailsVo detailsObject;
    private String issueID;
    private boolean isPurchased;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_magazine_details, null);
        ButterKnife.bind(this, mView);
        initView();
        if (getArguments() != null && getArguments().getString(IntentConstants.MAGAZINE_ID) != null) {
            magazineID = getArguments().getString(IntentConstants.MAGAZINE_ID, "");
            issueID = getArguments().getString(IntentConstants.ISSUE_ID);
            mSelectedCategory = Integer.parseInt(getArguments().getString(IntentConstants.CATEGORY_POSITION));
            mSelectedSubCategory = Integer.parseInt(getArguments().getString(IntentConstants.SUB_CATEGORY_POSITION));
            isPurchased = getArguments().getBoolean(IntentConstants.IS_PURCHASED);
        } else {
            showToast("Sorry!! Unable to load magazine");
            getActivity().finish();
            return mView;
        }
        root = getActivity().getCacheDir().getAbsolutePath();
        //Environment.getExternalStorageDirectory().getAbsoluteFile().toString();
        loadCards();
        return mView;
    }

    private void initView() {
        loadToast = new LoadToast(getActivity());
        loadToast.setText("Downloading...");
        WindowManager wm = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.app_red));
        categoryIds = getResources().obtainTypedArray(R.array.categoryIds);
        cardIds = getResources().obtainTypedArray(R.array.cardIds);
        categoryIds.recycle();
        cardIds.recycle();
    }

    private void loadCards() {
        //sectionBreifListLyt
        mCategories = new ArrayList<>();
        Category category = (Category) getArguments().getSerializable(IntentConstants.CATEGORY);
        mCategories.add(category);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        for(int i=0;i<mCategories.size();i++) {
            View title = inflater.inflate(R.layout.template_eight, null);
            if(mCategories.get(i).getCategoryName() != null && !mCategories.get(i).getCategoryName().isEmpty())
                ((TextView) title.findViewById(R.id.categoryTitle)).setText(mCategories.get(i).getCategoryName().toUpperCase());
            sectionBreifListLyt.addView(title);
            if(mCategories.get(i).getCategoryType().equals("Type1") && !mCategories.get(i).getCards().isEmpty() ) {
                List<Card> cards = mCategories.get(i).getCards();
                for (int j = 0; j < cards.size(); j++) {
                    View cardView = loadCardsView(j,cards.get(j));
                    cardView.setTag(i + "," + j + ",Type1");

                    if(isPurchased || cards.get(j).getPaid())
                        cardView.setOnClickListener(this);

                    sectionBreifListLyt.addView(cardView);
                }
            }
        }

    }

    private View loadCardsView(int position,Card card){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View cardView = inflater.inflate(R.layout.template_six, null);
        cardView = loadCardItem(position, cardView, card);
        return cardView;
    }


    public void removeAllSectionBreifListLyt() {
        sectionBreifListLyt.removeAllViews();
    }

    private View loadCardItem(int position, View view, Card data) {

        TextView subtitle = (TextView) view.findViewById(R.id.txtTag);
        TextView sub_category_name = (TextView) view.findViewById(R.id.txtTitle);
        TextView description = (TextView) view.findViewById(R.id.txtDescp);
        ImageView userImg = (ImageView) view.findViewById(R.id.imgAuthor);
        ImageView coverImg = (ImageView) view.findViewById(R.id.imgCover);
        ImageView blockImg = (ImageView) view.findViewById(R.id.imgBlock);
        LinearLayout overlay = (LinearLayout) view.findViewById(R.id.overlay);


        if (data.getSubsection() != null && !data.getSubsection().isEmpty()) {
            subtitle.setText("" + data.getSubsection());
        } else {
            subtitle.setVisibility(View.GONE);
        }
        if (data.getTitle() != null && !data.getTitle().isEmpty()) {
            sub_category_name.setText("" + data.getTitle());
        } else {
            sub_category_name.setVisibility(View.GONE);
        }
        if (data.getByline() != null && !data.getByline().isEmpty()) {
            description.setText("" + Html.fromHtml(data.getByline()));
        } else {
            description.setVisibility(View.GONE);
        }
        if (data.getImage() != null && !data.getImage().isEmpty()) {
            if(position == 0) {
                Picasso.with(getActivity()).load(data.getImage())
                        .fit().centerCrop().into(coverImg);
            }else{
                Picasso.with(getActivity()).load(data.getImage())
                        .fit().centerCrop().into(userImg);
            }
        } else {
            userImg.setVisibility(View.GONE);
        }
        if (isPurchased || data.getPaid()) {
            blockImg.setVisibility(View.GONE);
            overlay.setVisibility(View.GONE);
        }else{
            blockImg.setVisibility(View.VISIBLE);
            overlay.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @OnClick(R.id.goUp)
    public void goUp() {
//        parallaxView.smoothScrollTo(0, 0);
    }

    @Override
    public void onClick(View v) {

        String tag = (String) v.getTag();
        String[] tags = tag.split(",");
        if(tags[2].equals("Type1")) {
            openSectionDetails(mSelectedCategory,mSelectedSubCategory, Integer.parseInt(tags[1]));
        }

    }

    private void openSectionDetails(int categoryPosition,int subCategoryPosition, int cardPosition) {
        Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
        intent.putExtra(IntentConstants.CATEGORY_POSITION, categoryPosition);
        intent.putExtra(IntentConstants.SUB_CATEGORY_POSITION, subCategoryPosition);
        intent.putExtra(IntentConstants.CARD_POSITION, cardPosition);
        intent.putExtra(IntentConstants.ISSUE_ID, issueID);
        intent.putExtra(IntentConstants.MAGAZINE_ID, magazineID);
        intent.putExtra(IntentConstants.CATEGORY_TYPE, "Type2");
        if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_ADMIN) && getArguments().getString(IntentConstants.ADMIN_MAGAZINE)!=null){
            intent.putExtra(IntentConstants.ADMIN_MAGAZINE, getArguments().getString(IntentConstants.ADMIN_MAGAZINE));
        }
        startActivity(intent);
    }

}
