package com.app.outlook.Utils;

import android.app.Activity;

import com.app.outlook.OutLookApplication;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.app.outlook.OutLookApplication.TrackerName;


/**
 * Created by Madhumita on 16-11-2015.
 */
public class AnalyticsTracker {
    Activity activity;
    Tracker mTracker;

    public AnalyticsTracker(Activity act) {
        activity = act;
        //mTracker = ((OutLookApplication) activity.getApplication()).getTracker(TrackerName.APP_TRACKER);
    }

    public void sendEvent(int categoryId, int actionEventId) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(activity.getString(categoryId))
                .setAction(activity.getString(actionEventId)).build());
    }
    public void sendScreenName(int screenName){
        mTracker.setScreenName("Track"+screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
