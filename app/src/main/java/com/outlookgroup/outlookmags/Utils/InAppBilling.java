package com.outlookgroup.outlookmags.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.outlookgroup.outlookmags.modal.OutlookConstants;

import java.util.List;

/**
 * Created by Madhumita on 08-12-2015.
 */
public class InAppBilling implements IabHelper.QueryInventoryFinishedListener,
        IabHelper.OnIabPurchaseFinishedListener, IabHelper.OnConsumeMultiFinishedListener,
        IabHelper.OnConsumeFinishedListener{
    private static final String TAG = "InAppBilling";
    Context mContext;
    IabHelper mHelper;

    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inv) {

    }

    private Purchase purchaseInfo;
    public InAppBilling(Context context){
        mContext=context;
    }
    public void setUpAppBilling(){
        mHelper = new IabHelper(mContext, OutlookConstants.base64EncodedPublicKey);

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
    }
    public void purchaseFlow(Activity act,String sku,String issueId){
        mHelper.launchPurchaseFlow(act, sku, OutlookConstants.MAKE_GPAYMENT,
                this, issueId);
    }

    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase info) {
        if(info != null) {
            purchaseInfo = info;
            String json = info.getOriginalJson();
            Log.v(TAG, json);

            /*if(isSubcriptionClicked) {
                isSubcriptionClicked = false;
//                mHelper.consumeAsync(purchaseInfo, IssuesListingActivity.this);
                validateSubscription(purchaseInfo.getSku(), purchaseInfo.getToken(), purchaseInfo.getDeveloperPayload(),
                        selectedSubcription);
            }else {
                validatePurchase(purchaseInfo.getSku(), purchaseInfo.getToken(), purchaseInfo.getDeveloperPayload());
            }
            *//*
            *  has to be in only for subscription part*//*
            mHelper.consumeAsync(purchaseInfo, IssuesListingActivity.this);*/

        } else {
//            hideProgressDialog();
//            finish();
        }
    }
    public void consumeProduct(Purchase purchaseInf){
        mHelper.consumeAsync(purchaseInf,this);
    }

    @Override
    public void onConsumeFinished(Purchase purchase, IabResult result) {

    }

    @Override
    public void onConsumeMultiFinished(List<Purchase> purchases, List<IabResult> results) {

    }
}
