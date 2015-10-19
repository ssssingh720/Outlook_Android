package com.app.outlook.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.app.outlook.R;
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
//        String content = getArguments().getString(IntentConstants.WEB_CONTENT, "");
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
//        webview.getSettings().setUseWideViewPort(false);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
//        webview.setInitialScale((int) (100*webview.getScale()));
//        webview.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
//        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//        webview.setScrollbarFadingEnabled(true);
        webview.loadData(content, mimeType, encoding);
//        initView();
        return mView;
    }

    private void initView() {
        int position = getArguments().getInt(IntentConstants.POSITION);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = null;
        if (position % 2 == 0) {
            v = inflater.inflate(R.layout.template_four, null);
        } else {
            v = inflater.inflate(R.layout.template_five, null);
        }

        containerLyt.addView(v, 0);
    }

    @OnClick(R.id.goUp)
    public void goUp() {
        scrollView.smoothScrollTo(0, 0);
    }
}

