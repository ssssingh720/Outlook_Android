package com.app.outlook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.app.outlook.R;
import com.daimajia.easing.BaseEasingMethod;
import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by srajendrakumar on 25/09/15.
 */
public class SplashScreen extends AppBaseActivity {

    @Bind(R.id.imgBackground)
    ImageView imgBackground;
    @Bind(R.id.imgLogo)
    ImageView imgLogo;
    private ObjectAnimator logoAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        
        init();
    }

    private void init() {
        logoAnimation = ObjectAnimator.ofFloat(imgLogo, "alpha", 0,0.25f, 0.5f, 0.75f, 1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(Glider.glide(Skill.CircEaseOut, 200, logoAnimation, new BaseEasingMethod.EasingListener() {
                    @Override
                    public void on(float time, float value, float start, float end, float duration) {
                    }
                })
        );
        animatorSet.setDuration(2000);
        animatorSet.start();

        logoAnimation.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                startInitialCall();
                finish();
                startActivity(new Intent(SplashScreen.this,HomeListingActivity.class));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });
    }


}
