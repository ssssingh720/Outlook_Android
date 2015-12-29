package com.app.outlook.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.android.volley.VolleyError;
import com.app.outlook.OutLookApplication;
import com.app.outlook.R;
import com.app.outlook.Utils.APIMethods;
import com.app.outlook.Utils.IabHelper;
import com.app.outlook.Utils.IabResult;
import com.app.outlook.Utils.Inventory;
import com.app.outlook.Utils.Purchase;
import com.app.outlook.Utils.SkuDetails;
import com.app.outlook.Utils.Util;
import com.app.outlook.adapters.OutlookGridViewAdapter;
import com.app.outlook.listener.OnIssueItemsClickListener;
import com.app.outlook.manager.SessionManager;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.FeedParams;
import com.app.outlook.modal.IntentConstants;
import com.app.outlook.modal.Issue;
import com.app.outlook.modal.Magazine;
import com.app.outlook.modal.Month;
import com.app.outlook.modal.OutlookConstants;
import com.app.outlook.modal.PurchaseResponseVo;
import com.app.outlook.modal.YearListVo;
import com.app.outlook.views.MonthYearPicker;
import com.google.android.gms.analytics.HitBuilders;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 10/09/15.
 */
public class IssuesListingActivity extends AppBaseActivity implements IabHelper.QueryInventoryFinishedListener,
        IabHelper.OnIabPurchaseFinishedListener, IabHelper.OnConsumeMultiFinishedListener,
        IabHelper.OnConsumeFinishedListener,OnIssueItemsClickListener {

    private static final String TAG = "CategoryListingActivity";
    private OutlookGridViewAdapter adapter;
    @Bind(R.id.gridView)
    GridView gridView;
    private String magazineType,magazineTitle;
    private LoadToast loadToast;
    private String issueYear = "2015";
    private String root;
    IInAppBillingService mService;
    IabHelper mHelper;
    static final String ITEM_SKU = "outlook.five";
    private int selectedPosition = -1;
    private Purchase purchaseInfo;
    private DownloadFileFromURL task;
    int downloadCount;
    private int currentYear;
    private int currentMonth;
    private YearListVo yearListVo;
    @Bind(R.id.toolbar_title)
    ImageView toolbar_title;
    @Bind(R.id.btnSubscribe)
    Button btnSubscribe;
    @Bind(R.id.calendarImg)
    ImageView calendarImg;
    @Bind(R.id.reset_filter)
    ImageView resetFilter;
    private ArrayList<String> subscriptionIDList;
    private ArrayList<SkuDetails> skuList;
    private ArrayList<Purchase> purchaseList;
    private boolean isSubcriptionClicked;
    private String selectedSubcription;
    @Bind(R.id.emptyView)
    TextView emptyView;
    Account[] accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.scale_exit);
        setContentView(R.layout.activity_category_listing);
        ButterKnife.bind(this);
Util.clearNotification(this);
        magazineType = getIntent().getStringExtra(IntentConstants.TYPE);
        magazineTitle = getIntent().getStringExtra(IntentConstants.MAGAZINE_NAME);

        subscriptionIDList = getIntent().getStringArrayListExtra(IntentConstants.SUBSCRIPTION_IDS);
        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        accounts = accountManager.getAccounts();
        Log.i("account",accounts.length+"");
        mHelper = new IabHelper(this, OutlookConstants.base64EncodedPublicKey);

        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result) {
                                           if (!result.isSuccess()) {
                                               Log.d(TAG, "In-app Billing setup failed: " +
                                                       result);
                                           } else {
                                               Log.d(TAG, "In-app Billing is set up OK");
                                           }
                                       }
                                   });
        setLogo(Integer.parseInt(magazineType));
        initView();

    }

    private void setLogo(int logoType) {
        switch (logoType){
            case 0:
                toolbar_title.setImageResource(R.drawable.outlook_english);
                break;
            case 1:
                toolbar_title.setImageResource(R.drawable.outlook_money);
                break;
            case 2:
                toolbar_title.setImageResource(R.drawable.outlook_business);
                break;
            case 3:
                toolbar_title.setImageResource(R.drawable.outlook_hindi);
                break;
            case 4:
                toolbar_title.setImageResource(R.drawable.outlook_traveller);
                break;
            case 5:
                toolbar_title.setImageResource(R.drawable.outlook_traveller_gateway);
                break;
            default:
                toolbar_title.setImageResource(R.drawable.outlook_group);
                break;
        }

        /*if(Integer.parseInt(magazineType) == 0){
            toolbar_title.setImageResource(R.drawable.logo_outlook);
        }*/
    }
/*
* sku sent to play store*/
    private void queryList(){
        mHelper.queryInventoryAsync(true, subscriptionIDList,
                this);
    }

    private void initView() {
        if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_ADMIN)){
            btnSubscribe.setVisibility(View.GONE);
        }
        root =  getCacheDir().getAbsolutePath();
                //Environment.getExternalStorageDirectory().getAbsoluteFile().toString();

        final Calendar instance = Calendar.getInstance();
        currentMonth = instance.get(Calendar.MONTH);
        currentYear = instance.get(Calendar.YEAR);
        issueYear = currentYear+"";

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        loadToast = new LoadToast(this);
        loadToast.setText("Loading...");
        int height = size.y;
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.app_red));
        if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_ADMIN)){
            fetchIssueListAdminMode();
        }
        else{
        fetchIssueList();}
    }

    private void fetchIssueListAdminMode() {
        if (Util.isNetworkOnline(this)) {
            getDefaultList();
        }
        else{
            showToast(getResources().getString(R.string.no_internet));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(task != null && task.isCancelled() && yearListVo == null ){
            if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_ADMIN)){
                fetchIssueListAdminMode();
            }
            else{
                fetchIssueList();}
        }
if (OutlookConstants.IS_BOUGHT){
    OutlookConstants.IS_BOUGHT=false;
    fetchIssueList();
}

        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.back)
    public void onMBackClick() {
        finish();
    }

    @OnClick(R.id.calendarImg)
    public void onCalendaerClick() {
        final MonthYearPicker myp = new MonthYearPicker(IssuesListingActivity.this);
        myp.build(currentMonth, currentYear, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentMonth = myp.getSelectedMonth();
                currentYear = myp.getSelectedYear();
                getFilteredList(myp.getSelectedYear(), myp.getSelectedMonth() + 1);
            }
        }, null);
        myp.show();
    }
    @OnClick(R.id.reset_filter)
    public void resetFilter(){
        getDefaultList();

    }

    @OnClick(R.id.btnSubscribe)
    public void onSubscribeCLicked(){
        if (Util.isNetworkOnline(this)) {
            if (accounts.length>0) {
                queryList();
            }
            else{
                showToast("Login to your Gmail account to proceed.");
            }
        }
        else{
            showToast(getResources().getString(R.string.no_internet));
        }
    }

    private void fetchIssueList() {

        try {
            String filePath = root + File.separator + "Outlook/Magazines/"+magazineType+"-issues-" + issueYear + ".json";
            Log.d(TAG,"Magazine Path::" + filePath);
            File file = new File(filePath);
            if (!Util.isNetworkOnline(IssuesListingActivity.this) && file.exists()) {
                loadGridView(filePath);

            } else if (Util.isNetworkOnline(IssuesListingActivity.this)) {
                downloadCount++;
                task = new DownloadFileFromURL(issueYear);
                task.execute(magazineType+"",issueYear);
            }
            else{
                emptyView.setVisibility(View.VISIBLE);
                    btnSubscribe.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Error Parsing content");
        }
    }

    private void loadGridView(String filePath){
        gridView.setVisibility(View.VISIBLE);
        String response = Util.readJsonFromSDCard(filePath);
        System.out.println("Response::" + response);
        JsonReader reader = new JsonReader(new StringReader(response));
        reader.setLenient(true);
try {
    yearListVo = new Gson().fromJson(reader, YearListVo.class);
    setGridAdapter(yearListVo);
}
catch (Exception e){
    emptyView.setVisibility(View.VISIBLE);
    btnSubscribe.setVisibility(View.GONE);
}


    }

    private void setGridAdapter(YearListVo yearListVo) {
        if (yearListVo!=null){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        adapter = new OutlookGridViewAdapter(this, R.layout.grid_item_two_layout,getMonthList(yearListVo.getMonths(),
                yearListVo.getYear()), width,magazineType,this);
        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(adapter);
        AlphaInAnimationAdapter animationAlphaAdapter = new AlphaInAnimationAdapter(animationAdapter);
        animationAlphaAdapter.setAbsListView(gridView);
        gridView.setAdapter(animationAlphaAdapter);
            calendarImg.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            btnSubscribe.setVisibility(View.VISIBLE);
        }
        else{
            emptyView.setVisibility(View.VISIBLE);
                btnSubscribe.setVisibility(View.GONE);

        }
        if (adapter!=null && adapter.getCount()==0){
            emptyView.setVisibility(View.VISIBLE);
                btnSubscribe.setVisibility(View.GONE);

        }
        else{
            emptyView.setVisibility(View.GONE);
            btnSubscribe.setVisibility(View.VISIBLE);
        }

    }

    private ArrayList<Magazine> getMonthList(List<Month> monthArray,String issueYear){

        ArrayList<Magazine> months = new ArrayList<Magazine>();
        Collections.reverse(monthArray);
        for(int i=0; i< monthArray.size();i++){

            List<Issue> issueArray = monthArray.get(i).getIssues();

            for(int j=0 ; j< issueArray.size();j++){
                Magazine magazine = new Magazine();
                magazine.setImage(issueArray.get(j).getImage());
                magazine.setIssueDate(monthArray.get(i).getName() + ", " + issueYear);
                magazine.setPostId(issueArray.get(j).getIssueId() + "");
                magazine.setIsPurchased(issueArray.get(j).getPurchase());
                magazine.setSku(issueArray.get(j).getSku());
                magazine.setIssue_published_date(issueArray.get(j).getIssue_published_date());
                months.add(magazine);
            }
            if(issueArray.size() % 2 == 1){
                months.add(new Magazine());
            }
        }
        return months;

    }

    private void getFilteredList(int year, int month){
        loadToast.show();
        try{
            HashMap<String,String> params = new HashMap<>();
            params.put(FeedParams.MAG_ID, magazineType + "");
            params.put(FeedParams.YEAR,year+"");
            params.put(FeedParams.MONTH,month+"");
            params.put(FeedParams.TOKEN,SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN));

        placeRequest(APIMethods.ISSUE_LIST, YearListVo.class, params, false, null);
            resetFilter.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void getDefaultList(){
        loadToast.show();
        try{
            HashMap<String,String> params = new HashMap<>();
            params.put(FeedParams.MAG_ID, magazineType + "");
            params.put(FeedParams.YEAR,issueYear+"");
            params.put(FeedParams.TOKEN,SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN));
            params.put(FeedParams.USER_ID,SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID));
            placeRequest(APIMethods.ISSUE_LIST, YearListVo.class, params, false, null);
            resetFilter.setVisibility(View.GONE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void validatePurchase(String productID,String purchaseToken,String issueId){
        loadToast.show();

        HashMap<String,String> params = new HashMap<>();
        params.put(FeedParams.PLATFORM, OutlookConstants.PLATFORM);
        params.put(FeedParams.PACKAGE_NAME,getApplicationContext().getPackageName());
        params.put(FeedParams.PRODUCT_ID, productID);
        params.put(FeedParams.PURCHASE_TOKEN, purchaseToken);
        params.put(FeedParams.ISSUE_ID, issueId);
        params.put(FeedParams.MAGAZINE_ID, magazineType + "");

        placeRequest(APIMethods.VALIDATE_PURCHASE, PurchaseResponseVo.class, params, true, null);
    }

    private void validateSubscription(String productID,String purchaseToken,String issueDate,String duration){
        loadToast.show();

        HashMap<String,String> params = new HashMap<>();
        params.put(FeedParams.PLATFORM, OutlookConstants.PLATFORM);
        params.put(FeedParams.USERID, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID));
        params.put(FeedParams.PACKAGE_NAME,getApplicationContext().getPackageName());
        params.put(FeedParams.PRODUCT_ID, productID);
        params.put(FeedParams.PURCHASE_TOKEN, purchaseToken);
        params.put(FeedParams.ISSUE_PUBLISH_DATE, issueDate);
        params.put(FeedParams.MAGAZINE_ID, magazineType + "");
        params.put(FeedParams.DURATION, duration);

        placeRequest(APIMethods.VALIDATE_SUBSCRIPTION, PurchaseResponseVo.class, params, true, "subscription");
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        loadToast.success();

        if(apiMethod.equals(APIMethods.ISSUE_LIST)) {
            YearListVo yearListVo = (YearListVo) response;
if (yearListVo!=null) {
    Display display = getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    int width = size.x;
    if (gridView.getVisibility() == View.GONE) {
        gridView.setVisibility(View.VISIBLE);
    }
    adapter = new OutlookGridViewAdapter(this, R.layout.grid_item_two_layout,
            getMonthList(yearListVo.getMonths(), yearListVo.getYear()), width, magazineType, this);
    SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(adapter);
    AlphaInAnimationAdapter animationAlphaAdapter = new AlphaInAnimationAdapter(animationAdapter);
    animationAlphaAdapter.setAbsListView(gridView);
    gridView.setAdapter(animationAlphaAdapter);
    calendarImg.setVisibility(View.VISIBLE);
    emptyView.setVisibility(View.GONE);
    if (!SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_ADMIN)) {
        btnSubscribe.setVisibility(View.VISIBLE);
    }
}
            else{
    emptyView.setVisibility(View.VISIBLE);
        btnSubscribe.setVisibility(View.GONE);

}
            if (adapter.getCount()==0){
                emptyView.setVisibility(View.VISIBLE);
                    btnSubscribe.setVisibility(View.GONE);

            }
            else{
                emptyView.setVisibility(View.GONE);
                if (!SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_ADMIN)) {

                    btnSubscribe.setVisibility(View.VISIBLE);
                }
            }
        }else if(apiMethod.equals(APIMethods.VALIDATE_PURCHASE)){

            PurchaseResponseVo purchaseResponseVo = (PurchaseResponseVo) response;

            if(purchaseResponseVo.getResponse().getPurchaseState() == 0){
                if(selectedPosition != -1) {
                    Magazine magazine = adapter.getItem(selectedPosition);
                    magazine.setIsPurchased(true);
                    adapter.notifyDataSetChanged();

                    onCoverImageClicked(selectedPosition);
                    selectedPosition = -1;
                }
            }else {
                Toast.makeText(IssuesListingActivity.this,"Purchase failed.Please retry.",Toast.LENGTH_SHORT).show();
            }

        }/*else if(apiMethod.equals(APIMethods.VALIDATE_SUBSCRIPTION)){

            PurchaseResponseVo purchaseResponseVo = (PurchaseResponseVo) response;

            if(purchaseResponseVo.getResponse().getPurchaseState() == 0){
                task = new DownloadFileFromURL(issueYear);
                task.execute(magazineType+"",issueYear);
            }else {
                Toast.makeText(IssuesListingActivity.this,"Purchase failed.Please retry.",Toast.LENGTH_SHORT).show();
            }

        }*/
        else if (apiMethod.equals(APIMethods.VALIDATE_SUBSCRIPTION)) {

            PurchaseResponseVo purchaseResponseVo = (PurchaseResponseVo) response;

            if(purchaseResponseVo.getResponse().getPurchaseState() == 0){
                task = new DownloadFileFromURL(issueYear);
                task.execute(magazineType+"",issueYear);
            }else {
                Toast.makeText(IssuesListingActivity.this,"Purchase failed.Please retry.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        loadToast.error();

        if(apiMethod.equals(APIMethods.VALIDATE_PURCHASE)){
            Toast.makeText(IssuesListingActivity.this,"Purchase failed.Please retry.",Toast.LENGTH_SHORT).show();
        }
    }

    // on payment complition
    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase info) {

        if(info != null) {
            purchaseInfo = info;
            String json = info.getOriginalJson();
            Log.v(TAG, json);

            if(isSubcriptionClicked) {
                isSubcriptionClicked = false;
//                mHelper.consumeAsync(purchaseInfo, IssuesListingActivity.this);
                validateSubscription(purchaseInfo.getSku(), purchaseInfo.getToken(), purchaseInfo.getDeveloperPayload(),
                        selectedSubcription);
            }else {
                validatePurchase(purchaseInfo.getSku(), purchaseInfo.getToken(), purchaseInfo.getDeveloperPayload());
            }
            /*
            *  has to be in only for subscription part*/
            mHelper.consumeAsync(purchaseInfo, IssuesListingActivity.this);

        } else {
//            hideProgressDialog();
//            finish();
        }
    }


    @Override
    public void onBuyClicked(int position) {
        selectedPosition = position;
//            Toast.makeText(IssuesListingActivity.this,"onBuyClicked",Toast.LENGTH_SHORT).show();
        Magazine magazine = adapter.getItem(position);
        if (accounts.length>0) {
            buyManagedProductClick(magazine.getSku(), magazine.getPostId());
        }
        else{
            showToast("Login to your Gmail account to proceed.");
        }
    }

    @Override
    public void onDownloadClicked(int position) {
//        Toast.makeText(IssuesListingActivity.this,"onDownloadClicked",Toast.LENGTH_SHORT).show();

        Magazine magazine = adapter.getItem(position);
        String postID = magazine.getPostId();
        String skuID =magazine.getSku();
        if (postID != null) {
            Intent intent = new Intent(getBaseContext(), MagazineDetailsActivity.class);
            intent.putExtra(IntentConstants.MAGAZINE_ID, magazineType + "");
            intent.putExtra(IntentConstants.MAGAZINE_NAME,magazineTitle);
            intent.putExtra(IntentConstants.ISSUE_ID, postID + "");
            intent.putExtra(IntentConstants.SKU_ID, skuID + "");
            intent.putExtra(IntentConstants.IS_PURCHASED, true);
            intent.putExtra(IntentConstants.SUBSCRIPTION_IDS,subscriptionIDList);
            startActivity(intent);
            OutLookApplication.tracker().send(new HitBuilders.EventBuilder(magazineTitle,postID)
                    .setLabel("Data")
                    .build());
        }
    }

    @Override
    public void onCoverImageClicked(int position) {
//        Toast.makeText(IssuesListingActivity.this,"onCoverImageClicked",Toast.LENGTH_SHORT).show();
        if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_ADMIN)){
            if (Util.isNetworkOnline(this)){
                goToMagazineDetailsFromCoverImg(position);
            }
            else{
                showToast(getString(R.string.no_internet));
            }
        }
else{
            goToMagazineDetailsFromCoverImg(position);
        }


    }

    private void goToMagazineDetailsFromCoverImg(int position) {
        Magazine magazine = adapter.getItem(position);
        String postID = magazine.getPostId();
        String skuID =magazine.getSku();
        if (postID != null) {
            Intent intent = new Intent(getBaseContext(), MagazineDetailsActivity.class);
            intent.putExtra(IntentConstants.MAGAZINE_ID, magazineType + "");
            intent.putExtra(IntentConstants.MAGAZINE_NAME, magazineTitle);
            intent.putExtra(IntentConstants.ISSUE_ID, postID + "");
            intent.putExtra(IntentConstants.SKU_ID, skuID + "");
            intent.putExtra(IntentConstants.IS_PURCHASED, magazine.isPurchased());
            intent.putExtra(IntentConstants.SUBSCRIPTION_IDS,subscriptionIDList);
            startActivity(intent);
            OutLookApplication.tracker().send(new HitBuilders.EventBuilder(magazineTitle, postID)
                    .setLabel("Data")
                    .build());
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        String mPath;

        public DownloadFileFromURL(String issueYear) {
            this.mPath = root + File.separator + "Outlook/Magazines/"+magazineType+"-issues-" + issueYear + ".json";
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
                URL url = new URL(APIMethods.BASE_URL + APIMethods.ISSUE_LIST +
                "?mag_id="+params[0]+"&issue_year="+params[1]+
                        "&"+ FeedParams.USER_ID+"="+ SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID)
                        + "&"+ FeedParams.TOKEN+"="+SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN)
                );
                Log.d(TAG, "Download Json URL::" + url);
                URLConnection connection = url.openConnection();
                connection.connect();
                    // getting file length
                    int lenghtOfFile = connection.getContentLength();

                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);

                    // Output stream to write file
                    OutputStream output = new FileOutputStream(mPath);
//                Log.d("GalleryFragment", "DOWNLOAD Output Path::" + output.toString());

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
                SessionManager.setDownloadFailed(IssuesListingActivity.this, true);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "Downloaded JSON Successfully::");
            if (SessionManager.isDownloadFailed(IssuesListingActivity.this)) {
                stopDownload(mPath);
            }
            loadGridView(mPath);
            loadToast.success();
        }
    }

    private void stopDownload(String mFileName) {
        File imageFile = new File(mFileName);
        imageFile.delete();
        SessionManager.setDownloadFailed(IssuesListingActivity.this, false);
        loadToast.error();
        if (downloadCount==1 && Util.isNetworkOnline(IssuesListingActivity.this)) {
            task = new DownloadFileFromURL(issueYear);
            task.execute(magazineType+"",issueYear);
        }
        else{
            finish();
        }
    }

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }

    public void buyManagedProductClick(String sku,String issueId) {
        mHelper.launchPurchaseFlow(this, ITEM_SKU, OutlookConstants.MAKE_GPAYMENT,
                this, issueId);
    }

    public void buySubscriptionClick(String sku,String magazineId) {
        mHelper.launchPurchaseFlow(this, sku, OutlookConstants.MAKE_GPAYMENT,
                this, adapter.getItem(adapter.getCount()-1).getIssue_published_date());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHelper.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConsumeFinished(Purchase purchase, IabResult result) {
        Log.i(TAG, "onConsumeFinished");
    }

    @Override
    public void onConsumeMultiFinished(List<Purchase> purchases, List<IabResult> results) {
        Log.i(TAG, "onConsumeMultiFinished");
    }

    /**
     * details of the sku- (desc,etc issues)*/
    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
        if(inv!=null) {
            skuList = new ArrayList<SkuDetails>();
            purchaseList = new ArrayList<Purchase>();
            for (int i = 0; i < subscriptionIDList.size(); i++) {
                if (subscriptionIDList.get(i) != null) {
                    SkuDetails sku = inv.getSkuDetails(subscriptionIDList.get(i));
                    skuList.add(sku);
                    Purchase purchase = inv.getPurchase(subscriptionIDList.get(i));
                    if (purchase != null)
                        purchaseList.add(purchase);
                }
            }
            if (purchaseList != null && purchaseList.size() > 0) {
                mHelper.consumeAsync(purchaseList, this);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    promptUserToBuySubscription();
                }
            });
        } else {
            showToast("Not able to retrieve data. Please try again.");
           // this.finish();
        }

    }

    private void promptUserToBuySubscription() {

        final Dialog subscriptionDialog = new Dialog(this);
        subscriptionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        subscriptionDialog.setContentView(R.layout.dialog_subcription);
        subscriptionDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        subscriptionDialog.getWindow().setGravity(Gravity.CENTER);
        subscriptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btn1 = (Button) subscriptionDialog.findViewById(R.id.btn_annual);
        btn1.setText(skuList.get(0).getTitle()+" for "+skuList.get(0).getPrice());
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscriptionDialog.dismiss();
                isSubcriptionClicked = true;
                selectedSubcription = OutlookConstants.TWELVE;
                buySubscriptionClick(skuList.get(0).getSku(),magazineType);
            }
        });
        Button btn2 = (Button) subscriptionDialog.findViewById(R.id.btn_half_year);
        btn2.setText(skuList.get(0).getTitle()+" for "+skuList.get(0).getPrice());
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscriptionDialog.dismiss();
                isSubcriptionClicked = true;
                selectedSubcription = OutlookConstants.SIX;
                buySubscriptionClick(skuList.get(1).getSku(), magazineType);
            }
        });
        Button btn3 = (Button) subscriptionDialog.findViewById(R.id.btn_quater);
        btn3.setText(skuList.get(0).getTitle()+" for "+skuList.get(0).getPrice());
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscriptionDialog.dismiss();
                isSubcriptionClicked = true;
                selectedSubcription = OutlookConstants.THREE;
                buySubscriptionClick(skuList.get(2).getSku(), magazineType);
            }
        });

        subscriptionDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(task != null) {
            task.cancel(true);
            loadToast.error();
        }
    }


}
