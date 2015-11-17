package com.app.outlook;

import android.app.Application;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by srajendrakumar on 11/09/15.
 */
public class OutLookApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        Picasso.Builder builder = new Picasso.Builder(this);
        LruCache picassoCache = new LruCache(this);
        builder.memoryCache(picassoCache);
        Picasso.setSingletonInstance(builder.build());
    }
}
