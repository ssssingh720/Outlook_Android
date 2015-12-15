package com.app.outlook.fragments;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.app.outlook.OutLookApplication;
import com.app.outlook.R;
import com.app.outlook.Utils.APIMethods;
import com.app.outlook.Utils.Util;
import com.app.outlook.activities.ArticleDetailsActivity;
import com.app.outlook.activities.LogInActivity;
import com.app.outlook.activities.MagazineDetailsActivity;
import com.app.outlook.adapters.RegularGridViewAdapter;
import com.app.outlook.manager.SessionManager;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.Card;
import com.app.outlook.modal.Category;
import com.app.outlook.modal.Data;
import com.app.outlook.modal.DetailsObject;
import com.app.outlook.modal.FeedParams;
import com.app.outlook.modal.IntentConstants;
import com.app.outlook.modal.Item;
import com.app.outlook.modal.MagazineDetailsVo;
import com.app.outlook.modal.OutlookConstants;
import com.app.outlook.modal.YearListVo;
import com.google.android.gms.analytics.HitBuilders;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;
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
    @Bind(R.id.sectionBreifListLyt)
    LinearLayout sectionBreifListLyt;
    @Bind(R.id.bottom_holder)
    RelativeLayout mBottomHolder;
    @Bind(R.id.emptyViewMagazine)
    TextView emptyViewMagazine;

    TypedArray categoryIds;
    TypedArray cardIds;
    List<Category> mCategories = new ArrayList<>();
    int mSelectedCategory = 0;
    private String magazineID,magazineTitle;
    private String root;
    private LoadToast loadToast;
    private MagazineDetailsVo detailsObject;
    private String issueID;
    private DownloadFileFromURL task;
    private boolean isPurchased;
    private String adminMagazine;
    Dialog subscribePopUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_magazine_details, null);
        ButterKnife.bind(this, mView);
        initView();
        if (getArguments() != null && getArguments().getString(IntentConstants.MAGAZINE_ID) != null) {
            magazineID = getArguments().getString(IntentConstants.MAGAZINE_ID, "");
            magazineTitle=getArguments().getString(IntentConstants.MAGAZINE_NAME, "");
            issueID = getArguments().getString(IntentConstants.ISSUE_ID);
            isPurchased = getArguments().getBoolean(IntentConstants.IS_PURCHASED);
        } else {
            showToast("Sorry!! Unable to load magazine");
            getActivity().finish();
            return mView;
        }
        root = getActivity().getCacheDir().getAbsolutePath();
        //Environment.getExternalStorageDirectory().getAbsoluteFile().toString();
        if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_ADMIN)){
            if (Util.isNetworkOnline(getActivity())){
            fetchMagazineDetailsAdminMode();}
            else {
                showToast(getResources().getString(R.string.no_internet));
            }
        }
        else {
            fetchMagazineDetails();
        }
        return mView;
    }

    private void fetchMagazineDetailsAdminMode() {
        loadToast.show();
        try{
            HashMap<String,String> params = new HashMap<>();
            params.put(FeedParams.MAG_ID, magazineID + "");
            params.put("issue_id", issueID + "");
            params.put(FeedParams.USER_ID,SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID));
            params.put(FeedParams.TOKEN,SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN));
            placeRequest(APIMethods.MAGAZINE_DETAILS, MagazineDetailsVo.class, params, false, null);
        }catch (Exception e){
            e.printStackTrace();
        }
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

    private void fetchMagazineDetails() {
        String filePath = root + File.separator + "Outlook/Magazines/"+magazineID+"-magazine-" + issueID + ".json";
        try {
            Log.d(TAG, "Magazine Path::" + filePath);
            File file = new File(filePath);
            if (file.exists()) {
                String response = Util.readJsonFromSDCard(filePath);
                System.out.println("Response::" + response);
                JsonReader reader = new JsonReader(new StringReader(response));
                reader.setLenient(true);
                detailsObject = new Gson().fromJson(reader, MagazineDetailsVo.class);
                mCategories = detailsObject.getCategories();
                loadToast.success();

                loadCards();
            }
            else if (Util.isNetworkOnline(getActivity())) {
                task = new DownloadFileFromURL();
                task.execute(magazineID, issueID);
            }
            else if (!file.exists() && !Util.isNetworkOnline(getActivity())){
                emptyViewMagazine.setVisibility(View.VISIBLE);
            }
            OutLookApplication.tracker().send(new HitBuilders.EventBuilder(magazineTitle+magazineID, issueID)
                    .setLabel("Downloads")
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            loadToast.error();
            stopDownload(filePath);
            showToast("Something went wrong. Try again later");
        }
    }

    private void loadCards() {
        //sectionBreifListLyt
        LayoutInflater inflater = getActivity().getLayoutInflater();
        for(int i=0;i<mCategories.size();i++) {
            View title = inflater.inflate(R.layout.template_eight, null);
            ((TextView) title.findViewById(R.id.categoryTitle)).setText(mCategories.get(i).getCategoryName().toUpperCase());
            sectionBreifListLyt.addView(title);
            if(mCategories.get(i).getCategoryType().equals("Type1") && !mCategories.get(i).getCards().isEmpty() ) {
                List<Card> cards = mCategories.get(i).getCards();
                int m=0;
                for (int j = 0; j < cards.size(); j++) {

                    View cardView = loadCardsView(j,cards.get(j));

                    if(isPurchased || cards.get(j).getPaid()) {
                        cardView.setTag(i + "," + m + ",Type1");
                        m++;
                    }
                    cardView.setOnClickListener(this);
                    sectionBreifListLyt.addView(cardView);
                }
            }else if(mCategories.get(i).getCategoryType().equals("Type2")){
                View cardView = loadRegularView(i,mCategories.get(i).getCategories());
                sectionBreifListLyt.addView(cardView);
            }
        }

    }

    private View loadCardsView(int position,Card card){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View cardView = inflater.inflate(R.layout.template_six, null);
        cardView = loadCardItem(position, cardView, card);
        return cardView;
    }
    private View loadRegularView(int position,List<Category> data){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View cardView = inflater.inflate(R.layout.template_nine, null);
        cardView = loadRegularGridItem(position, cardView, data);
        return cardView;
    }

    private View loadRegularGridItem(int position,View view, List<Category> data){

        GridLayout categoryGrid = (GridLayout) view.findViewById(R.id.categoryGrid);

        for(int i= 0; i< data.size();i++) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View categoryGridView = inflater.inflate(R.layout.template_ten, null);
            ImageView image = (ImageView) categoryGridView.findViewById(R.id.categoryImg);
            TextView nameTxt = (TextView) categoryGridView.findViewById(R.id.categoryName);

            Category category = data.get(i);
            if (category.getCategoryIcon() != null && !category.getCategoryIcon().isEmpty())
                Picasso.with(getActivity()).load(category.getCategoryIcon()).fit().into(image);

            if (category.getCategoryName() != null && !category.getCategoryName().isEmpty())
                nameTxt.setText(category.getCategoryName());

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            width =((width - Util.dipToPixels(getActivity(), 32)) / 4);
            LinearLayout.LayoutParams linearLytParam = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            categoryGridView.setLayoutParams(linearLytParam);

            categoryGridView.setTag(position + "," + i + ",Type2");
            categoryGridView.setOnClickListener(this);
            categoryGrid.addView(categoryGridView);
        }
        return view;
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
                coverImg.setVisibility(View.VISIBLE);
                userImg.setVisibility(View.GONE);
                Picasso.with(getActivity()).load(data.getImage())
                        .fit().centerCrop().into(coverImg);
            }else{
                coverImg.setVisibility(View.GONE);
                userImg.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(data.getImage())
                        .fit().centerCrop().into(userImg);
            }
        } else {
            coverImg.setVisibility(View.GONE);
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
        if (tag!=null) {
            String[] tags = tag.split(",");
            if (tags[2].equals("Type1")) {
                Log.i("Type1",Integer.parseInt(tags[0])+"::"+Integer.parseInt(tags[1]));
                openSectionDetails(Integer.parseInt(tags[0]), Integer.parseInt(tags[1]));
            } else {
                Log.i("Type2",Integer.parseInt(tags[0])+"::"+Integer.parseInt(tags[1]));
                openRegularDetails(Integer.parseInt(tags[0]), Integer.parseInt(tags[1]));
                /*RegularsListingFragment fragment = new RegularsListingFragment();
                Bundle bundle = new Bundle();
                bundle.putString(IntentConstants.MAGAZINE_ID, magazineID);
                bundle.putString(IntentConstants.MAGAZINE_NAME, magazineID);
                bundle.putString(IntentConstants.ISSUE_ID, issueID);
                bundle.putString(IntentConstants.CATEGORY_POSITION, tags[0]);
                bundle.putString(IntentConstants.SUB_CATEGORY_POSITION, tags[1]);
                bundle.putSerializable(IntentConstants.CATEGORY, mCategories.get(Integer.parseInt(tags[0])).
                        getCategories().get(Integer.parseInt(tags[1])));
                bundle.putBoolean(IntentConstants.IS_PURCHASED, isPurchased);
                if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_ADMIN) && adminMagazine != null) {
                    bundle.putString(IntentConstants.ADMIN_MAGAZINE, adminMagazine);
                }
                fragment.setArguments(bundle);
                ((MagazineDetailsActivity) getActivity()).changeFragment(fragment, true);*/
            }
        }
        else{
            showPopUpSubscribe();
        }

    }

    private void openRegularDetails(int categoryPosition, int subCategoryPosition) {
        Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
        intent.putExtra(IntentConstants.CATEGORY_POSITION, categoryPosition);
        intent.putExtra(IntentConstants.SUB_CATEGORY_POSITION, subCategoryPosition);
        intent.putExtra(IntentConstants.CARD_POSITION, 0);
        intent.putExtra(IntentConstants.ISSUE_ID, issueID);
        intent.putExtra(IntentConstants.MAGAZINE_ID, magazineID);
        intent.putExtra(IntentConstants.CATEGORY_TYPE, "Type2");
        if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_ADMIN) && getArguments().getString(IntentConstants.ADMIN_MAGAZINE)!=null){
            intent.putExtra(IntentConstants.ADMIN_MAGAZINE, getArguments().getString(IntentConstants.ADMIN_MAGAZINE));
        }
        startActivity(intent);
    }

    private void showPopUpSubscribe() {
        subscribePopUp = new Dialog(getActivity(), R.style.DialogSlideAnim);
        subscribePopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        subscribePopUp.setContentView(R.layout.popup_subscription_msg);
        subscribePopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        subscribePopUp.getWindow().setGravity(Gravity.CENTER);
        subscribePopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button okBtn=(Button)subscribePopUp.findViewById(R.id.OkBtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidePopupDialog();
            }
        });

        subscribePopUp.show();
    }

    private void openSectionDetails(int categoryPosition, int cardPosition) {
//        ((MagazineDetailsActivity) getActivity()).openSectionDetails(categoryPosition,cardPosition);
        Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
        intent.putExtra(IntentConstants.CATEGORY_POSITION, categoryPosition);
        intent.putExtra(IntentConstants.CARD_POSITION, cardPosition);
        intent.putExtra(IntentConstants.ISSUE_ID, issueID);
        intent.putExtra(IntentConstants.MAGAZINE_ID, magazineID);
        intent.putExtra(IntentConstants.MAGAZINE_NAME, magazineTitle);
        if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_ADMIN) && adminMagazine!=null){
            intent.putExtra(IntentConstants.ADMIN_MAGAZINE, adminMagazine);
        }
        startActivity(intent);
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        String mPath;

        public DownloadFileFromURL() {
            this.mPath = root + File.separator + "Outlook/Magazines/"+magazineID+"-magazine-" + issueID + ".json";
            File file = new File(mPath);
            if (file.exists()) {
                file.delete();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadToast.show();
            File parentFolder = new File(root + File.separator + "Outlook");
            File subFolder = new File(root + File.separator + "Outlook/Magazines");
            if (!parentFolder.exists()) {
                parentFolder.mkdir();
            }
            if (!subFolder.exists()) {
                subFolder.mkdir();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            int count;
            try {
                URL url = new URL(APIMethods.BASE_URL + APIMethods.MAGAZINE_DETAILS + "?mag_id=" + params[0]
                +"&issue_id="+ params[1]+
                        "&"+ FeedParams.USER_ID+"="+ SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID)
                        + "&"+ FeedParams.TOKEN+"="+ SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN));

                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lenghtOfFile = connection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream(mPath);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                SessionManager.setDownloadFailed(getActivity(), true);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (SessionManager.isDownloadFailed(getActivity())) {
                    stopDownload(mPath);
                }
                fetchMagazineDetails();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void stopDownload(String mFileName) {
        File imageFile = new File(mFileName);
        imageFile.delete();
        SessionManager.setDownloadFailed(getActivity(), false);
        loadToast.error();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 1 second
                        getActivity().finish();
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if(task != null)
            task.cancel(true);
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        loadToast.success();
        if(apiMethod.equals(APIMethods.MAGAZINE_DETAILS)) {
            MagazineDetailsVo detailsObject=(MagazineDetailsVo)response;
            mCategories = detailsObject.getCategories();
            if (mCategories.size()>0) {
                emptyViewMagazine.setVisibility(View.GONE);
                loadCards();
                adminMagazine = new Gson().toJson(detailsObject);
            }
            else{
                emptyViewMagazine.setVisibility(View.VISIBLE);
            }
        }

        }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        loadToast.error();
        if(apiMethod.equals(APIMethods.MAGAZINE_DETAILS)) {
            emptyViewMagazine.setVisibility(View.VISIBLE);
        }
        }
    private void hidePopupDialog() {
        if (subscribePopUp.isShowing()) {
            subscribePopUp.dismiss();
            subscribePopUp.cancel();
        }
    }
}
