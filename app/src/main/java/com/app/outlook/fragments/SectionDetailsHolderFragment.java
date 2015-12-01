package com.app.outlook.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.app.outlook.R;
import com.app.outlook.Utils.Util;
import com.app.outlook.activities.ArticleDetailsActivity;
import com.app.outlook.listener.OnArticleModeChangeListener;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.Card;
import com.app.outlook.modal.Category;
import com.app.outlook.modal.CategoryOptionsVo;
import com.app.outlook.modal.IntentConstants;
import com.app.outlook.modal.Magazine;
import com.app.outlook.modal.MagazineDetailsVo;
import com.app.outlook.modal.OutlookConstants;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 22/09/15.
 */
public class SectionDetailsHolderFragment extends BaseFragment implements SectionDetailsFragment.OnScrollChangeListener{

    private int mSelectedItem;
    @Bind(R.id.container)
    ViewPager mPager;
    private PageAdapter mAdapter;
    private int categoryPosition = 0,subCategoryPosition = 0;
    private int currentCardPosition = 0;
    private int currentPageCount = 0;
    ArrayList<String> mContents = new ArrayList<>();
    ArrayList<String> mTitles= new ArrayList<>();
    private String issueID,magazineID,magazineTitle;
    @Bind(R.id.cardsList)
    ListView cardsList;
    @Bind(R.id.articleOptionView)
    CardView articleOptionView;
    private ArrayList<CategoryOptionsVo> categoryOptions = new ArrayList<CategoryOptionsVo>();
    private String categoryType;
    private boolean isPurchased;
    private OnArticleModeChangeListener articleModeChangeListener;

    public OnArticleModeChangeListener getArticleModeChangeListener() {
        return articleModeChangeListener;
    }

    public void setArticleModeChangeListener(OnArticleModeChangeListener articleModeChangeListener) {
        this.articleModeChangeListener = articleModeChangeListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_section_details_holder, null);
        categoryPosition = getArguments().getInt(IntentConstants.CATEGORY_POSITION, 0);
        currentCardPosition = getArguments().getInt(IntentConstants.CARD_POSITION, 0);
        issueID = getArguments().getString(IntentConstants.ISSUE_ID);
        magazineID = getArguments().getString(IntentConstants.MAGAZINE_ID);
        magazineTitle=getArguments().getString(IntentConstants.MAGAZINE_NAME);
        categoryType = getArguments().getString(IntentConstants.CATEGORY_TYPE);
        currentPageCount = getArguments().getInt(IntentConstants.ITEM_POSITION, 0);
        subCategoryPosition = getArguments().getInt(IntentConstants.SUB_CATEGORY_POSITION, 0);
        isPurchased = getArguments().getBoolean(IntentConstants.IS_PURCHASED);
        ButterKnife.bind(this, mView);
        if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_ADMIN)){
            String adminMagazine = getArguments().getString(IntentConstants.ADMIN_MAGAZINE);
        loadAdminContents(adminMagazine);
        }
        else {
            loadContents();
        }
        initView();
        return mView;
    }

    private void loadAdminContents(String magazine) {
        MagazineDetailsVo detailsObject = new Gson().fromJson(magazine, MagazineDetailsVo.class);
        getMagazineCards(detailsObject);
    }

    public void onSelectionClick() {
        if(articleOptionView.getVisibility() == View.VISIBLE){
            articleOptionView.setVisibility(View.GONE);
        }else{
            articleOptionView.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.container)
    public void onContainerClick(){
        onSelectionClick();
    }

    private void loadContents() {

        String root = getActivity().getCacheDir().getAbsolutePath();
                //Environment.getExternalStorageDirectory().getAbsoluteFile().toString();
        String filePath = root + File.separator + "Outlook/Magazines/"+magazineID+"-magazine-" + issueID + ".json";
        try {
            File file = new File(filePath);
            if (file.exists()) {
                String response = Util.readJsonFromSDCard(filePath);
                System.out.println("Response::" + response);
                JsonReader reader = new JsonReader(new StringReader(response));
                reader.setLenient(true);
                MagazineDetailsVo detailsObject = new Gson().fromJson(reader, MagazineDetailsVo.class);
                getMagazineCards(detailsObject);


            } else {
                // error
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Something went wrong. Try again later");
        }
    }

    private void getMagazineCards(MagazineDetailsVo detailsObject) {
        List<Category> mCategories = detailsObject.getCategories();
        Category selectedCategory = mCategories.get(categoryPosition);
        if(categoryType != null && categoryType.equals("Type2")){
            selectedCategory = selectedCategory.getCategories().get(subCategoryPosition);
        }
        if(selectedCategory.getCategoryName() != null) {
            ((ArticleDetailsActivity)getActivity()).setTitle(selectedCategory.getCategoryName());
        }else{
            ((ArticleDetailsActivity)getActivity()).setTitle("Category Title");
        }
        List<Card> cards = selectedCategory.getCards();
        for (int i=0;i<cards.size();i++){
            if(isPurchased || cards.get(i).getPaid()) {
                mContents.add(cards.get(i).getContent());
                mTitles.add(cards.get(i).getTitle());
                CategoryOptionsVo obj = new CategoryOptionsVo();
                obj.setTitle(cards.get(i).getTitle());
                obj.setSubTitle(cards.get(i).getSubsection());
                categoryOptions.add(obj);
            }
        }

        CategoryOptionAdapter adapter = new CategoryOptionAdapter(getActivity(),R.layout.category_selection_row);
        cardsList.setAdapter(adapter);
        if(adapter.getCount() > 4){
            View item = adapter.getView(0, null, cardsList);
            item.measure(0, 0);
//                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (5.5 * item.getMeasuredHeight()));
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) cardsList.getLayoutParams();
            params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            params.height = (int) (5.5 * item.getMeasuredHeight());

            cardsList.setLayoutParams(params);
        }

        cardsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentCardPosition = position;
                mPager.setCurrentItem(currentCardPosition);
                articleOptionView.setVisibility(View.GONE);
            }
        });
    }


    private void initView() {
        mAdapter = new PageAdapter(getChildFragmentManager(), mContents,mTitles);
        mPager.setAdapter(mAdapter);
        mPager.setPageTransformer(true, new DepthPageTransformer());
        mPager.setCurrentItem(currentCardPosition);
        cardsList.setSelection(currentCardPosition);
        cardsList.setItemChecked(currentCardPosition, true);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentCardPosition = position;
                cardsList.setSelection(currentCardPosition);
                cardsList.setItemChecked(currentCardPosition, true);

            }

            @Override
            public void onPageSelected(int position) {
                articleOptionView.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private class PageAdapter extends FragmentStatePagerAdapter {

        ArrayList<String> mContents;
        ArrayList<String> mTitles;
        SectionDetailsFragment fragment;

        public PageAdapter(FragmentManager fragmentManager, ArrayList<String> contents,ArrayList<String> titles) {
            super(fragmentManager);
            mContents = contents;
            mTitles=titles;
        }

        @Override
        public Fragment getItem(int position) {
            final Bundle bundle = new Bundle();
            bundle.putInt(IntentConstants.POSITION, position);
            bundle.putString(IntentConstants.WEB_CONTENT, mContents.get(position));
            bundle.putString(IntentConstants.WEB_CONTENT_TITLE, mTitles.get(position));
            bundle.putString(IntentConstants.MAGAZINE_NAME,magazineTitle);
            bundle.putString(IntentConstants.ISSUE_ID,issueID);
            fragment = new SectionDetailsFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return mContents.size();
        }
        public void pagerNightMode(boolean mode){
            fragment.toggleNightMode(mode);
        }
        public void pagerTextResize(){
            //fragment.manageFontSeekBar();

        }
    }

    public class CategoryOptionAdapter extends ArrayAdapter<CategoryOptionsVo>{

        public CategoryOptionAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public int getCount() {
            return categoryOptions.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder;

            if (row == null) {
                LayoutInflater inflater = (getActivity()).getLayoutInflater();
                row = inflater.inflate(R.layout.category_selection_row, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) row.findViewById(R.id.txtTitle);
                holder.subTitle = (TextView) row.findViewById(R.id.txtTag);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            CategoryOptionsVo obj = categoryOptions.get(position);
            holder.title.setText(obj.getTitle());
            holder.subTitle.setText(obj.getSubTitle());

            return row;
        }

        class ViewHolder {
            TextView title;
            TextView subTitle;
        }
    }

    public void onNightMode(boolean mode) {
        showToast("Night Mode");
        ((SectionDetailsFragment)mAdapter.getItem(currentCardPosition)).toggleNightMode(mode);
    }

    public void onResizeText() {
        showToast("Text Mode");
        mAdapter.pagerTextResize();
    }

    @Override
    public void onScrollChange(ScrollState scrollState) {
        ((ArticleDetailsActivity)getActivity()).hideShowToolBar(scrollState);

    }
}
