package com.app.outlook.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.app.outlook.R;
import com.app.outlook.Utils.APIMethods;
import com.app.outlook.Utils.IabHelper;
import com.app.outlook.Utils.IabResult;
import com.app.outlook.Utils.Inventory;
import com.app.outlook.Utils.Purchase;
import com.app.outlook.Utils.SkuDetails;
import com.app.outlook.Utils.Util;
import com.app.outlook.fragments.MagazineDetailsFragment;
import com.app.outlook.fragments.SectionDetailsHolderFragment;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.Category;
import com.app.outlook.modal.FeedParams;
import com.app.outlook.modal.IntentConstants;
import com.app.outlook.modal.Magazine;
import com.app.outlook.modal.OutlookConstants;
import com.app.outlook.modal.PurchaseResponseVo;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 21/09/15.
 */
public class MagazineDetailsActivity extends AppBaseActivity implements IabHelper.QueryInventoryFinishedListener,
        IabHelper.OnIabPurchaseFinishedListener, IabHelper.OnConsumeMultiFinishedListener,
        IabHelper.OnConsumeFinishedListener {
       public static final String EXTRA_NAME = "cheese_name";

    MagazineDetailsFragment magazineDetailsFragment;
    ArrayList<String> mContent = new ArrayList<>();
    private String TAG = "MagazineDetailsActivity";
    String magazineID,magazineTitle;
    @Bind(R.id.toolbar_title)
    ImageView toolbar_title;
    @Bind(R.id.shareImg)
    ImageView shareImg;
    @Bind(R.id.bottomLyt)
    LinearLayout bottomLyt;
    @Bind(R.id.subscribe_issue)
            Button subscribeIssue;
    CallbackManager callbackManager;
    IInAppBillingService mService;
    IabHelper mHelper;
    static final String ITEM_SKU = "outlook.five";
    String issueID;
    private ArrayList<String> subscriptionIDList;
    private ArrayList<SkuDetails> skuList;
    private ArrayList<Purchase> purchaseList;
    private boolean isSubcriptionClicked;
    private String selectedSubcription;
    private Purchase purchaseInfo;
    Account[] accounts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.scale_exit);
        setContentView(R.layout.activity_fragment);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        accounts = accountManager.getAccounts();
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
        magazineDetailsFragment = new MagazineDetailsFragment();
        magazineID = getIntent().getStringExtra(IntentConstants.MAGAZINE_ID);
        magazineTitle=getIntent().getStringExtra(IntentConstants.MAGAZINE_NAME);
        setLogo(Integer.parseInt(magazineID));

         issueID = getIntent().getStringExtra(IntentConstants.ISSUE_ID);
        subscriptionIDList = getIntent().getStringArrayListExtra(IntentConstants.SUBSCRIPTION_IDS);
         String sku = getIntent().getStringExtra(IntentConstants.SKU_ID);
        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.MAGAZINE_ID, magazineID);
        bundle.putString(IntentConstants.MAGAZINE_NAME, magazineTitle);
        bundle.putString(IntentConstants.ISSUE_ID, issueID);
        bundle.putBoolean(IntentConstants.IS_PURCHASED, getIntent().getBooleanExtra(IntentConstants.IS_PURCHASED, false));
        magazineDetailsFragment.setArguments(bundle);
        changeFragment(magazineDetailsFragment, false);
        if (getIntent().getBooleanExtra(IntentConstants.IS_PURCHASED, false)){
            shareImg.setVisibility(View.VISIBLE);
            bottomLyt.setVisibility(View.GONE);
        }
        else{
            shareImg.setVisibility(View.VISIBLE);
            bottomLyt.setVisibility(View.VISIBLE);
        }
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

        /*if(Integer.parseInt(magazineID) == 0){
            toolbar_title.setImageResource(R.drawable.logo_outlook);
        }*/
    }


    @OnClick(R.id.back)
    public void onMBackClick() {
        onBackPressed();
    }

    public void changeFragment(Fragment fragment, boolean backStack) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_enter, R.anim.slide_exit);
        transaction.add(R.id.contentPanel, fragment);
        if (backStack) {
            transaction.hide(magazineDetailsFragment);
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
@OnClick(R.id.shareImg)
public void onShareIssue(){
    if (Util.isNetworkOnline(MagazineDetailsActivity.this)) {
        postToFacebook();
    }
    else{
        showToast(getResources().getString(R.string.no_internet));
    }
}

    public void openSectionDetails(int categoryPosition, int cardPosition) {

        Intent intent = new Intent(MagazineDetailsActivity.this, ArticleDetailsActivity.class);
        intent.putExtra(IntentConstants.CATEGORY_POSITION, categoryPosition);
        intent.putExtra(IntentConstants.CARD_POSITION, cardPosition);
        intent.putExtra(IntentConstants.ISSUE_ID, getIntent().getStringExtra(IntentConstants.ISSUE_ID));
        intent.putExtra(IntentConstants.IS_PURCHASED, getIntent().getBooleanExtra(IntentConstants.IS_PURCHASED, false));

        startActivity(intent);
    }
    @OnClick(R.id.subscribe_issue)
    public void subscribeIssue(){
        if (Util.isNetworkOnline(this)) {
            if (accounts.length>0) {
            queryList();
                subscribeIssue.setEnabled(false);
        }
        else{
            showToast("Login to your Gmail account to proceed.");
        }
        }
        else{
            showToast(getResources().getString(R.string.no_internet));
        }
    }
    @OnClick(R.id.buy_single_issue)
    public void buyIssue(){
        if (accounts.length>0) {
            buyManagedProductClick(ITEM_SKU, issueID);
    }
    else{
        showToast("Login to your Gmail account to proceed.");
    }
    }
    public void buyManagedProductClick(String sku,String issueId) {
        mHelper.launchPurchaseFlow(this, ITEM_SKU, OutlookConstants.MAKE_GPAYMENT,
                this, issueId);
    }
    public ArrayList<String> getContent() {
        return mContent;
    }

    public void setContent(ArrayList<String> content) {
        this.mContent = content;
    }
    private void postToFacebook() {
        callbackManager = CallbackManager.Factory.create();
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Check out the latest Outlook Group Magazines")
                    .setContentDescription(
                            "")
                    .setContentUrl(Uri.parse("https://play.google.com/apps/testing/com.app.outlooktest"))
                    .build();

            shareDialog.show(linkContent);
        }
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager!=null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        mHelper.handleActivityResult(requestCode, resultCode, data);
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
            mHelper.consumeAsync(purchaseInfo, MagazineDetailsActivity.this);

        } else {
//            hideProgressDialog();
//            finish();
        }
    }

    private void validatePurchase(String productID,String purchaseToken,String issueId){
        //loadToast.show();

        HashMap<String,String> params = new HashMap<>();
        params.put(FeedParams.PLATFORM, OutlookConstants.PLATFORM);
        params.put(FeedParams.PACKAGE_NAME,getApplicationContext().getPackageName());
        params.put(FeedParams.PRODUCT_ID, productID);
        params.put(FeedParams.PURCHASE_TOKEN, purchaseToken);
        params.put(FeedParams.ISSUE_ID, issueId);
        params.put(FeedParams.MAGAZINE_ID, magazineID + "");

        placeRequest(APIMethods.VALIDATE_PURCHASE, PurchaseResponseVo.class, params, true, null);
    }

    private void validateSubscription(String productID,String purchaseToken,String issueDate,String duration){
        //loadToast.show();

        HashMap<String,String> params = new HashMap<>();
        params.put(FeedParams.PLATFORM, OutlookConstants.PLATFORM);
        params.put(FeedParams.USERID, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID));
        params.put(FeedParams.PACKAGE_NAME,getApplicationContext().getPackageName());
        params.put(FeedParams.PRODUCT_ID, productID);
        params.put(FeedParams.PURCHASE_TOKEN, purchaseToken);
        params.put(FeedParams.ISSUE_PUBLISH_DATE, issueDate);
        params.put(FeedParams.MAGAZINE_ID, magazineID + "");
        params.put(FeedParams.DURATION, duration);

        placeRequest(APIMethods.VALIDATE_SUBSCRIPTION, PurchaseResponseVo.class, params, true, "subscription");
    }
    /*
* sku sent to play store*/
    private void queryList(){

        mHelper.queryInventoryAsync(true, subscriptionIDList,
                this);
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
            subscribeIssue.setEnabled(true);
            //this.finish();
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
                buySubscriptionClick(skuList.get(0).getSku(),magazineID);
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
                buySubscriptionClick(skuList.get(1).getSku(), magazineID);
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
                buySubscriptionClick(skuList.get(2).getSku(), magazineID);
            }
        });

        subscriptionDialog.show();
    }
    public void buySubscriptionClick(String sku,String magazineId) {
        mHelper.launchPurchaseFlow(this, sku, OutlookConstants.MAKE_GPAYMENT,
                this, "date");
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        if(apiMethod.equals(APIMethods.VALIDATE_PURCHASE)){

            PurchaseResponseVo purchaseResponseVo = (PurchaseResponseVo) response;

            if(purchaseResponseVo.getResponse().getPurchaseState() == 0){
                // refresh list
                OutlookConstants.IS_BOUGHT=true;
               bottomLyt.setVisibility(View.GONE);
                String filePath = getCacheDir().getAbsolutePath() + File.separator + "Outlook/Magazines/"+magazineID+"-magazine-" + issueID + ".json";
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }
                FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();

                fragTransaction.detach(magazineDetailsFragment);
                fragTransaction.attach(magazineDetailsFragment);
                fragTransaction.commit();
            }else {
                showToast("Purchase failed.Please retry.");
            }

        }
        else if (apiMethod.equals(APIMethods.VALIDATE_SUBSCRIPTION)) {

            PurchaseResponseVo purchaseResponseVo = (PurchaseResponseVo) response;

            if(purchaseResponseVo.getResponse().getPurchaseState() == 0){
                // refresh list
                OutlookConstants.IS_BOUGHT=true;
                bottomLyt.setVisibility(View.GONE);
                String filePath = getCacheDir().getAbsolutePath() + File.separator + "Outlook/Magazines/"+magazineID+"-magazine-" + issueID + ".json";
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }
                FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();

                fragTransaction.detach(magazineDetailsFragment);
                fragTransaction.attach(magazineDetailsFragment);
                fragTransaction.commit();
            }else {
                showToast("Purchase failed.Please retry.");
            }
        }
    }
}
