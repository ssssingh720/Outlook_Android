package com.app.outlook.activities;

import android.app.Application;
import android.util.Log;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by srajendrakumar on 02/11/15.
 */
public class OutlookApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("SocialChatApp", "Application Started");
//        Fabric.with(this, new Crashlytics());
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                        .setDefaultFontPath("fonts/Lato-Regular.ttf")
//                        .setFontAttrId(R.attr.fontPath)
//                        .build()
//        );
        Picasso.Builder builder = new Picasso.Builder(this);
        LruCache picassoCache = new LruCache(this);
        builder.memoryCache(picassoCache);
        Picasso.setSingletonInstance(builder.build());

        /*Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
//        built.setIndicatorsEnabled(false);
//        built.setLoggingEnabled(false);
        Picasso.setSingletonInstance(built);*/
    }
}
