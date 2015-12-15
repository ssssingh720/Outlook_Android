package com.app.outlook.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.outlook.R;
import com.app.outlook.modal.IntentConstants;
import com.app.outlook.views.TouchImageView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Madhumita on 14-12-2015.
 */
public class ImageViewActivity extends AppBaseActivity {
    @Bind(R.id.image_web)
    TouchImageView webImage;
    @Bind(R.id.toolbar_title)
    TextView titleTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_image);
        ButterKnife.bind(this);
        String imageLink = getIntent().getStringExtra(IntentConstants.WEB_IMAGE_LINK);
        String title = getIntent().getStringExtra(IntentConstants.WEB_CONTENT_TITLE);
        Picasso.with(this).load(imageLink).into(webImage);
        titleTxt.setText(title);
    }
    @OnClick(R.id.toolbar_back)
    public void onMBackClick() {
        onBackPressed();
    }
}
