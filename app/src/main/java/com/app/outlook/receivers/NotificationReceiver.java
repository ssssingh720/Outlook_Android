package com.app.outlook.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.outlook.activities.HomeListingActivity;
import com.app.outlook.activities.IssuesListingActivity;
import com.app.outlook.activities.SignUpActivity;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.IntentConstants;
import com.app.outlook.modal.NotificationVo;
import com.app.outlook.modal.OutlookConstants;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Madhumita on 25-11-2015.
 */
public class NotificationReceiver extends BroadcastReceiver {
    String notificationMessage;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPrefManager prefManager = SharedPrefManager.getInstance();
        prefManager.init(context);
        notificationMessage = intent.getExtras().getString(IntentConstants.NOTIFICATION_MESSAGE);
        NotificationVo notification = new Gson().fromJson(notificationMessage, NotificationVo.class);
        if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_LOGGEDIN)) {
            Intent a = new Intent(context.getApplicationContext(), HomeListingActivity.class);
            a.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(a);

            ArrayList<String> subscriptionIDList = new ArrayList<>();
            subscriptionIDList.add(notification.getQuarterly_sku());
            subscriptionIDList.add(notification.getHalf_yearly_sku());
            subscriptionIDList.add(notification.getAnnual_sku());
            Intent b = new Intent(context.getApplicationContext(), IssuesListingActivity.class);
            b.putExtra(IntentConstants.TYPE, notification.getMagazine_id());
            b.putExtra(IntentConstants.MAGAZINE_NAME, notification.getMagazine_title());
            b.putExtra(IntentConstants.SUBSCRIPTION_IDS, subscriptionIDList);
            b.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(b);
        }
        else{
            Intent a = new Intent(context.getApplicationContext(), SignUpActivity.class);
            a.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(a);
        }
    }
}
