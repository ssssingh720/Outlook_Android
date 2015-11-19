package com.app.outlook.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.app.outlook.R;
import com.app.outlook.Utils.Util;
import com.app.outlook.modal.IntentConstants;

import java.lang.reflect.Method;

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
    @Bind(R.id.fontSeekBar)
    SeekBar fontSeekBar;
    @Bind(R.id.fontSizeBtn)
    ImageView fontSizeBtn;
    @Bind(R.id.nightModeBtn)
    ToggleButton nightModeBtn;
    private int fontSize;
    private String htmlContent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_section_details, null);
        ButterKnife.bind(this, mView);
        int position = getArguments().getInt(IntentConstants.CARD_HOLDER_POSITION, 0);
        String content = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"\\/>" + getArguments().getString(IntentConstants.WEB_CONTENT, "");
        final String mimeType = "text/html";
        final String encoding = "utf-8";
        htmlContent=content;
        webview.getSettings().setDefaultTextEncodingName(encoding);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setSupportZoom(false);
        webview.getSettings().setUseWideViewPort(false);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setFocusableInTouchMode(false);
        webview.setFocusable(false);
        webview.getSettings().setLoadsImagesAutomatically(true);
        if (Util.isNetworkOnline(getActivity())) {
            webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webview.loadDataWithBaseURL(null,content, mimeType, encoding,null);
        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        fontSize = webview.getSettings().getTextZoom();
        fontSeekBar.setProgress(50);
        fontSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int zoom = (fontSize * progress) / 50;
                webview.getSettings().setTextZoom(zoom);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return mView;
    }

    private void toggleNightMode(boolean nightMode,String content){
        String rawHTML = "";

        if(nightMode) {
            rawHTML = "<HTML>" +
                    "<head>" + "<style  type=\"text/css\">" +
                    "body,h1{color: #ffffff;" +
                    "background-color: #000000;}" +
                    "</style></head>" +
                    "<body><h1>" + content + "</h1></body>" +
                    "</HTML>";
            webview.loadData(rawHTML, "text/html; charset=UTF-8",null);
        }else {
            webview.loadData(content, "text/html; charset=UTF-8",null);
        }
        // webview.requestFocus();

        //webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

    }

    private void changeFontSize(){

    }
    @OnClick(R.id.goUp)
    public void goUp() {
        scrollView.smoothScrollTo(0, 0);
    }
    @OnClick(R.id.fontSizeBtn)
    public void manageFontSeekBar(){
        if (fontSeekBar.getVisibility()==View.VISIBLE){
            fontSeekBar.setVisibility(View.GONE);
        }
        else{
            fontSeekBar.setVisibility(View.VISIBLE);
        }
    }
    @OnClick(R.id.nightModeBtn)
    public void manageNightMode(){
        toggleNightMode(nightModeBtn.isChecked(),htmlContent);
    }
}

