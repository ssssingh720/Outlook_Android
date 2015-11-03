package com.app.outlook.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.vending.billing.IInAppBillingService;
import com.app.outlook.R;
import com.app.outlook.Utils.APIMethods;
import com.app.outlook.Utils.IabHelper;
import com.app.outlook.Utils.IabResult;
import com.app.outlook.Utils.Inventory;
import com.app.outlook.Utils.Purchase;
import com.app.outlook.Utils.SkuDetails;
import com.app.outlook.Utils.Util;
import com.app.outlook.adapters.OutlookGridViewAdapter;
import com.app.outlook.manager.SessionManager;
import com.app.outlook.modal.DetailsObject;
import com.app.outlook.modal.IntentConstants;
import com.app.outlook.modal.Issue;
import com.app.outlook.modal.IssuesVo;
import com.app.outlook.modal.Magazine;
import com.app.outlook.modal.Month;
import com.app.outlook.modal.OutlookConstants;
import com.app.outlook.modal.WeeklyIssueVo;
import com.app.outlook.modal.YearListVo;
import com.app.outlook.views.MonthYearPicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 10/09/15.
 */
public class IssuesListingActivity extends AppBaseActivity implements IabHelper.QueryInventoryFinishedListener, IabHelper.OnIabPurchaseFinishedListener, IabHelper.OnConsumeMultiFinishedListener,  IabHelper.OnConsumeFinishedListener{

    private static final String TAG = "CategoryListingActivity";
    private OutlookGridViewAdapter adapter;
    @Bind(R.id.gridView)
    GridView gridView;
    private int magazineType;
    private LoadToast loadToast;
    private String issueYear = "2015";
    private String root;
    IInAppBillingService mService;
    IabHelper mHelper;
    static final String ITEM_SKU = "android.test.purchased";
    private ArrayList<String> productIDList;
    private ArrayList<SkuDetails> skuList;
    private String selectedSKU;
    private ArrayList<Purchase> purchaseList;
    private Purchase purchaseInfo;
    private SkuDetails selectedSKUItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_listing);
        ButterKnife.bind(this);

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        mHelper = new IabHelper(this, OutlookConstants.base64EncodedPublicKey);

        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result)
                                       {
                                           if (!result.isSuccess()) {
                                               Log.d(TAG, "In-app Billing setup failed: " +
                                                       result);
                                           } else {
                                               Log.d(TAG, "In-app Billing is set up OK");
                                           }
                                       }
                                   });
        initView();
    }

    private void initView() {
        root = Environment.getExternalStorageDirectory().getAbsoluteFile().toString();

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

        magazineType = getIntent().getIntExtra(IntentConstants.TYPE, 0);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Magazine magazine = adapter.getItem(position);
                String postID = magazine.getPostId();
                if (postID != null) {
//                    Intent intent = new Intent(getBaseContext(), MagazineDetailsActivity.class);
//                    intent.putExtra(IntentConstants.MAGAZINE_ID, postID);
//                    startActivity(intent);
                    buyClick();
                }

            }
        });

        fetchIssueList();
    }

    @OnClick(R.id.back)
    public void onMBackClick() {
        finish();
    }

    @OnClick(R.id.calendarImg)
    public void onCalendaerClick() {
        MonthYearPicker myp = new MonthYearPicker(IssuesListingActivity.this);
        myp.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                textView1.setText(myp.getSelectedMonthName() + " >> " + myp.getSelectedYear());
            }
        }, null);
        myp.show();
    }

    private void fetchIssueList() {

        try {
            String filePath = root + File.separator + "Outlook/Magazines/issues-" + issueYear + ".json";
            Log.d(TAG,"Magazine Path::" + filePath);
            File file = new File(filePath);
            if (!Util.isNetworkOnline(IssuesListingActivity.this) && file.exists()) {
                loadGridView(filePath);

            } else if (Util.isNetworkOnline(IssuesListingActivity.this)) {
                new DownloadFileFromURL(issueYear).execute(magazineType+"",issueYear);
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

        YearListVo yearListVo = new Gson().fromJson(reader, YearListVo.class);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        adapter = new OutlookGridViewAdapter(this, R.layout.grid_item_two_layout,getMonthList(yearListVo.getMonths(),yearListVo.getYear()), width);
        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(adapter);
        AlphaInAnimationAdapter animationAlphaAdapter = new AlphaInAnimationAdapter(animationAdapter);
        animationAlphaAdapter.setAbsListView(gridView);
        gridView.setAdapter(animationAlphaAdapter);

    }

    private ArrayList<Magazine> getMonthList(List<Month> monthArray,String issueYear){

        ArrayList<Magazine> months = new ArrayList<Magazine>();

        for(int i=0; i< monthArray.size();i++){

            List<Issue> issueArray = monthArray.get(i).getIssues();

            for(int j=0 ; j< issueArray.size();j++){
                Magazine magazine = new Magazine();
                magazine.setImage(issueArray.get(j).getImage());
                magazine.setIssueDate(monthArray.get(i).getName()+", " + issueYear);
                magazine.setPostId(issueArray.get(j).getIssueId()+"");
                months.add(magazine);
            }

        }

        return months;

    }

    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase info) {
        if (result.isFailure()) {
            // Handle error
            return;
        }
//        else if (purchase.getSku().equals(ITEM_SKU)) {
//            consumeItem();
//            buyButton.setEnabled(false);
//        }

    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        String mPath;

        public DownloadFileFromURL(String issueYear) {
            this.mPath = root + File.separator + "Outlook/Magazines/issues-" + issueYear + ".json";
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
                "?mag_id="+params[0]+"&year="+params[1]);
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
        finish();
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

    public void buyClick() {
        mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,
                this, "mypurchasetokend");
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

    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
        if(inv!=null) {
            skuList = new ArrayList<SkuDetails>();
            purchaseList = new ArrayList<Purchase>();
            for (int i = 0; i < productIDList.size(); i++) {
                if (productIDList.get(i) != null) {
                    SkuDetails sku = inv.getSkuDetails(productIDList.get(i));
                    skuList.add(sku);
                    Purchase purchase = inv.getPurchase(productIDList.get(i));
                    if (purchase != null)
                        purchaseList.add(purchase);
                }
            }
            if (purchaseList != null && purchaseList.size() > 0) {
                mHelper.consumeAsync(purchaseList, this);
            }

        } else {
            showToast("Not able to retreive data. Please try again.");
            this.finish();
        }
    }
}
