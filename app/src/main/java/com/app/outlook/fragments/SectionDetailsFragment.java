package com.app.outlook.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.app.outlook.R;
import com.app.outlook.Utils.Util;
import com.app.outlook.modal.IntentConstants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 22/09/15.
 */
public class SectionDetailsFragment extends BaseFragment {


    @Bind(R.id.containerLyt)
    RelativeLayout containerLyt;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.webview)
    WebView webview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_section_details, null);
        ButterKnife.bind(this, mView);
        int position = getArguments().getInt(IntentConstants.CARD_HOLDER_POSITION, 0);
        String content = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"\\/>" + getArguments().getString(IntentConstants.WEB_CONTENT, "");
        final String mimeType = "text/html";
        final String encoding = "utf-8";
        webview.getSettings().setDefaultTextEncodingName(encoding);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setSupportZoom(false);
        webview.getSettings().setUseWideViewPort(false);
        webview.setFocusableInTouchMode(false);
        webview.setFocusable(false);
        webview.getSettings().setLoadsImagesAutomatically(true);
        if (Util.isNetworkOnline(getActivity())) {
            webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
//        webview.loadData(content, mimeType, encoding);
        webview.loadDataWithBaseURL(null,content, mimeType, encoding,null);

        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return mView;
    }

    @OnClick(R.id.goUp)
    public void goUp() {
        scrollView.smoothScrollTo(0, 0);
    }
}

