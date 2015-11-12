package com.app.outlook.activities;

import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.android.volley.VolleyError;
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
import com.app.outlook.manager.SharedPrefManager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    private DownloadFileFromURL task;
    private int currentYear;
    private int currentMonth;
    private YearListVo yearListVo;
    @Bind(R.id.toolbar_title)
    ImageView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.scale_exit);
        setContentView(R.layout.activity_category_listing);
        ButterKnife.bind(this);

        magazineType = getIntent().getIntExtra(IntentConstants.TYPE, 0);
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
                                               queryList();
                                           }
                                       }
                                   });
        setLogo();
        initView();
    }

    private void setLogo() {
        if(magazineType == 0){
            toolbar_title.setImageResource(R.drawable.logo_outlook);
        }
    }

    private void queryList(){
        List additionalSkuList = new ArrayList();
        additionalSkuList.add(ITEM_SKU);
//                                               additionalSkuList.add(SKU_BANANA);
        mHelper.queryInventoryAsync(true, additionalSkuList,
                this);
    }

    private void initView() {
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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Magazine magazine = adapter.getItem(position);
                String postID = magazine.getPostId();
                if (postID != null) {
//                    Intent intent = new Intent(getBaseContext(), MagazineDetailsActivity.class);
//                    intent.putExtra(IntentConstants.MAGAZINE_ID, magazineType + "");
//                    intent.putExtra(IntentConstants.ISSUE_ID, postID + "");
//                    startActivity(intent);
                    buyClick();
                }

            }
        });

        fetchIssueList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(task != null && task.isCancelled() && yearListVo == null ){
                fetchIssueList();
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

    private void fetchIssueList() {

        try {
            String filePath = root + File.separator + "Outlook/Magazines/"+magazineType+"-issues-" + issueYear + ".json";
            Log.d(TAG,"Magazine Path::" + filePath);
            File file = new File(filePath);
            if (!Util.isNetworkOnline(IssuesListingActivity.this) && file.exists()) {
                loadGridView(filePath);

            } else if (Util.isNetworkOnline(IssuesListingActivity.this)) {
                task = new DownloadFileFromURL(issueYear);
                task.execute(magazineType+"",issueYear);
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

        yearListVo = new Gson().fromJson(reader, YearListVo.class);

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
        Collections.reverse(monthArray);
        for(int i=0; i< monthArray.size();i++){

            List<Issue> issueArray = monthArray.get(i).getIssues();

            for(int j=0 ; j< issueArray.size();j++){
                Magazine magazine = new Magazine();
                magazine.setImage(issueArray.get(j).getImage());
                magazine.setIssueDate(monthArray.get(i).getName()+", " + issueYear);
                magazine.setPostId(issueArray.get(j).getIssueId()+"");
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
        String methodName = APIMethods.ISSUE_LIST +
                "?mag_id="+magazineType+"&year="+year+"&month="+month+
                "&"+ OutlookConstants.USERID+"="+ SharedPrefManager.getInstance().getSharedDataString(OutlookConstants.USERID)
                + "&"+ OutlookConstants.TOKEN+"="+ URLEncoder.encode(SharedPrefManager.getInstance().getSharedDataString(OutlookConstants.TOKEN), "UTF-8");

        placeRequest(methodName, YearListVo.class, null, false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        loadToast.success();

        YearListVo yearListVo = (YearListVo) response;

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

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        loadToast.error();
    }

    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase info) {
        if(info != null) {
            purchaseInfo = info;
            String json = info.getOriginalJson();
            Log.v(TAG,json);

            mHelper.consumeAsync(purchaseInfo, IssuesListingActivity.this);

            new AlertDialog.Builder(IssuesListingActivity.this)
                    .setTitle("Purchase Details")
                    .setMessage(json)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            try {
            File subFolder = new File(root + File.separator + "Outlook/Magazines");
            if (!subFolder.exists()) {
                subFolder.mkdir();
            }
            String mPath = root + File.separator + "Outlook/Magazines/" + "purchase.json";
            File file = new File(mPath);

            String content = info.getOrderId()+","+info.getToken()+","+info.getOriginalJson();
            FileWriter fw = null;

                fw = new FileWriter(mPath);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


//            json = "{ \"data\": " + json + ", \"ref\": \"" + UserInfoManager.getInstance().getUserRegistrationVO().getRef() + "\" }";
            try {
//                placeRequest(INAPP_CONFIRMATION, BooleanResponse.class, new JSONObject(json));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
//            hideProgressDialog();
            finish();
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
                "?mag_id="+params[0]+"&year="+params[1]+
                        "&"+ OutlookConstants.USERID+"="+ SharedPrefManager.getInstance().getSharedDataString(OutlookConstants.USERID)
                        + "&"+ OutlookConstants.TOKEN+"="+SharedPrefManager.getInstance().getSharedDataString(OutlookConstants.TOKEN)
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
//        if(inv!=null) {
//            String applePrice =
//                    inv.getSkuDetails(ITEM_SKU).getPrice();
//        } else {
//            showToast("Not able to retreive data. Please try again.");
//            this.finish();
//        }
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
