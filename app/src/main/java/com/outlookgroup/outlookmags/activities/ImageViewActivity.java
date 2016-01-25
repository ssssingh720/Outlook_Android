package com.outlookgroup.outlookmags.activities;

import android.os.Bundle;
import android.widget.ImageView;

import com.outlookgroup.outlookmags.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Madhumita on 14-12-2015.
 */
public class ImageViewActivity extends AppBaseActivity {
    @Bind(R.id.close_ad_view)
    ImageView close_ad_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_image);
        ButterKnife.bind(this);
        /*String imageLink = getIntent().getStringExtra(IntentConstants.WEB_IMAGE_LINK);
        String title = getIntent().getStringExtra(IntentConstants.WEB_CONTENT_TITLE);
        Picasso.with(this).load("http://52.76.121.132/wp-content/uploads/2015/12/image002.jpg").into(webImage);
        titleTxt.setText(title)*/;
    }
    @OnClick(R.id.close_ad_view)
    public void onMBackClick() {
        onBackPressed();
    }
}
