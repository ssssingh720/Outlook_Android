package com.app.outlook.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Picture;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.app.outlook.OutLookApplication;
import com.app.outlook.R;
import com.app.outlook.Utils.Util;
import com.app.outlook.activities.ArticleDetailsActivity;
import com.app.outlook.listener.OnArticleModeChangeListener;
import com.app.outlook.modal.IntentConstants;
import com.daimajia.easing.BaseEasingMethod;
import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.gms.analytics.HitBuilders;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 22/09/15.
 */
public class SectionDetailsFragment extends BaseFragment implements ObservableScrollViewCallbacks {


    @Bind(R.id.containerLyt)
    RelativeLayout containerLyt;
    @Bind(R.id.scrollView)
    ObservableScrollView scrollView;
    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.parentArticleLyt)
    RelativeLayout parentArticleLyt;
    @Bind(R.id.bottomGoUp)
    View bottomGoUp;
    private int fontSize;
    private String htmlContent;
    OnScrollChangeListener mOnScrollChangeListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_section_details, null);
        ButterKnife.bind(this, mView);
        int position = getArguments().getInt(IntentConstants.CARD_HOLDER_POSITION, 0);
        htmlContent = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"\\/>" + getArguments().getString(IntentConstants.WEB_CONTENT, "");
        String title=getArguments().getString(IntentConstants.WEB_CONTENT_TITLE, "");
        String magazineTitle=getArguments().getString(IntentConstants.MAGAZINE_NAME, "");
        String issueID=getArguments().getString(IntentConstants.ISSUE_ID, "");
        boolean isNightMode=getArguments().getBoolean(IntentConstants.IS_NIGHT_MODE, false);
        int textSize=getArguments().getInt(IntentConstants.WEB_TEXT,0);
        final String mimeType = "text/html";
        final String encoding = "utf-8";

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

        webview.loadDataWithBaseURL(null, htmlContent, mimeType, encoding, null);
        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mOnScrollChangeListener != null) {
                    mOnScrollChangeListener.onTouchView();
                }
                return true;
            }
        });
/*
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
        scrollView.setScrollViewCallbacks(this);
       webview.setPictureListener(new WebView.PictureListener() {
           @Override
           public void onNewPicture(WebView webView, Picture picture) {
               int childHeight = containerLyt.getHeight();
               boolean isScrollable = scrollView.getHeight() < childHeight + scrollView.getPaddingTop() + scrollView.getPaddingBottom();
               if (isScrollable) {
                   bottomGoUp.setVisibility(View.VISIBLE);
                   webView.setPictureListener(null);
               }

           }
       });*/
        OutLookApplication.tracker().send(new HitBuilders.EventBuilder(magazineTitle, issueID + "")
                .setLabel(title)
                .build());

        parentArticleLyt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mOnScrollChangeListener != null) {
                    mOnScrollChangeListener.onTouchView();
                }
                return true;
            }
        });
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAttachFragment(getParentFragment());

    }

    public void toggleNightMode(boolean nightMode){
        String rawHTML = "";
        if(nightMode) {
            rawHTML = "<HTML>" +
                    "<head>" + "<style  type=\"text/css\">" +
                    "*{color: #ffffff !important;" +
                    "background-color: #000000;}" +
                    "</style></head>" +
                    "<body>" + htmlContent + "</body>" +
                    "</HTML>";
            webview.loadData(rawHTML, "text/html; charset=UTF-8",null);
            parentArticleLyt.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
        }else {
            webview.loadData(htmlContent, "text/html; charset=UTF-8",null);
            parentArticleLyt.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
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
   /* @OnClick(R.id.nightModeBtn)
    public void manageNightMode(){
        toggleNightMode(nightModeBtn.isChecked());
    }
    @OnClick(R.id.fontSizeBtn)
    public void manageFontSeekBar(){
        if (fontSeekBar.getVisibility()==View.VISIBLE){
            fontSeekBar.setVisibility(View.GONE);
        }
        else{
            fontSeekBar.setVisibility(View.VISIBLE);
        }
    }*/
    /*@OnClick(R.id.fontSizeBtn)
    public void manageFontSeekBar(){
        if (fontSeekBar.getVisibility()==View.VISIBLE){
            fontSeekBar.setVisibility(View.GONE);
        }
        else{
            fontSeekBar.setVisibility(View.VISIBLE);
        }
    }
    @OnClick(R.id.nightMode)
    public void manageNightMode(){
        toggleNightMode(nightModeBtn.isChecked());
    }
*/

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (mOnScrollChangeListener != null)
        {
            mOnScrollChangeListener.onScrollChange(scrollState);
        }
      /*  if (scrollState == ScrollState.UP) {
            if (bottomLayout.getVisibility()==View.VISIBLE || fontSeekBar.getVisibility()==View.VISIBLE) {
                bottomLayout.setVisibility(View.GONE);
                bottomLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));

                fontSeekBar.setVisibility(View.GONE);
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (bottomLayout.getVisibility()==View.GONE ) {
                bottomLayout.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2));
                bottomLayout.setVisibility(View.VISIBLE);

            }
        }*/

    }

    public interface OnScrollChangeListener
    {
        public void onScrollChange(ScrollState scrollState);
        public void onTouchView();
    }
    public void onAttachFragment(Fragment fragment)
    {
        try
        {
            mOnScrollChangeListener = (OnScrollChangeListener)fragment;

        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnScrollChangeListener");
        }
    }


}

