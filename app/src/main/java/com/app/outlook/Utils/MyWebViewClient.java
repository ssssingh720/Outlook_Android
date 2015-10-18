package com.app.outlook.Utils;

/**
 * Created by srajendrakumar on 15/10/15.
 */

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

    private Context context;

    public MyWebViewClient(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.equals("hrupin://second_activity")) {
//            Intent i = new Intent(context, SecondActivity.class);
//            context.startActivity(i);
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }
}
