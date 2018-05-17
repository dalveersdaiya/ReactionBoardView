package com.nipunbirla.sampleproject;

import android.content.Context;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codemybrainsout.onboarder.views.FlowingGradientClass;
import com.facebook.keyframes.KeyframesDrawable;
import com.facebook.keyframes.KeyframesDrawableBuilder;
import com.facebook.keyframes.deserializers.KFImageDeserializer;
import com.facebook.keyframes.model.KFImage;

import elasticity.ElasticAction;
import reaction.OnSelectNotification;
import reaction.ReactionView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivSelectedEmoji;
    ImageView ivSelectedEmojiFlip;
    KeyframesDrawable kfDrawable;
    KeyframesDrawable kfDrawableFlip;
    TextView tvNotice;
    Vibrator vibrator;
    ReactionView rvl;
    RelativeLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        findViewByIds();
        showReactions();
        applyClickListeners();
        setGradientBackground();
    }

    public void setGradientBackground() {

        FlowingGradientClass grad = new FlowingGradientClass();
        grad.setBackgroundResource(R.drawable.translate)
                .onRelativeLayout(parentLayout)
                .setTransitionDuration(4000)
                .start();
    }

    public void findViewByIds() {
        ivSelectedEmoji = (ImageView) findViewById(R.id.ivSelectedEmoji);
        ivSelectedEmojiFlip = (ImageView) findViewById(R.id.ivSelectedEmojiFlip);
        rvl = (ReactionView) findViewById(R.id.reaction);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        tvNotice = (TextView) findViewById(R.id.tvNotice);
        parentLayout = (RelativeLayout)findViewById(R.id.activity_main);
    }

    public void applyClickListeners() {
        tvNotice.setOnClickListener(this);
    }

    public void showReactions() {
        rvl.init();
        rvl.setVisibility(View.VISIBLE);
        rvl.setOnSelectNotification(new OnSelectNotification() {
            @Override
            public void onSelect(int pos) {
                showSelectedEmoji(pos);
            }

            @Override
            public void onDeselect(int pos) {
            }
        });

    }

    public void showSelectedEmoji(int pos) {
        KFImage kfImage = null;
        KFImage kfImageFlip = null;
        String asset = "";
        String assetFlip = "";
        switch (pos) {
            case 0:
                asset = "Like.json";
                assetFlip = "Anger.json";
                break;
            case 1:
                asset = "Love.json";
                assetFlip = "Haha.json";
                break;
            case 2:
                asset = "Haha.json";
                assetFlip = "Like.json";
                break;
            case 3:
                asset = "Wow.json";
                assetFlip = "Haha.json";
                break;
            case 4:
                asset = "Sorry.json";
                assetFlip = "Wow.json";
                break;
            case 5:
                asset = "Anger.json";
                assetFlip = "Sorry.json";
                break;

            default:
                break;
        }

        InputStream stream = null;
        InputStream streamFlip = null;

        try {
            stream = getResources().getAssets().open(asset);
            streamFlip = getResources().getAssets().open(assetFlip);
            kfImage = KFImageDeserializer.deserialize(stream);
            kfImageFlip = KFImageDeserializer.deserialize(streamFlip);
        } catch (IOException e) {
            e.printStackTrace();
        }

        kfDrawable = new KeyframesDrawableBuilder().withImage(kfImage).build();
        kfDrawableFlip = new KeyframesDrawableBuilder().withImage(kfImageFlip).build();

        ivSelectedEmoji.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ivSelectedEmoji.setImageDrawable(kfDrawable);
        ivSelectedEmojiFlip.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ivSelectedEmojiFlip.setImageDrawable(kfDrawableFlip);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ivSelectedEmoji.setImageAlpha(0);
            ivSelectedEmojiFlip.setImageAlpha(0);
        }
        kfDrawable.startAnimation();
        kfDrawableFlip.startAnimation();
//        kfDrawable.playOnce();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ivSelectedEmoji.setVisibility(View.GONE);
//                ivSelectedEmojiFlip.setVisibility(View.GONE);
//            }
//        }, kfDrawable.getAnimationDuration());
    }

    public void setElasticity(View view, float scaleXY, int duration, boolean wannaVibrate) {
        ElasticAction.doAction(view, duration, scaleXY, scaleXY);
        if (wannaVibrate) {
            vibrator.vibrate(50);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        kfDrawable.stopAnimation();
        kfDrawableFlip.stopAnimation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        kfDrawable.stopAnimation();
        kfDrawableFlip.stopAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (kfDrawable != null && kfDrawableFlip != null) {
            kfDrawable.startAnimation();
            kfDrawableFlip.startAnimation();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    ivSelectedEmoji.setVisibility(View.GONE);
//                    ivSelectedEmojiFlip.setVisibility(View.GONE);
//                }
//            }, kfDrawable.getAnimationDuration());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvNotice:
                setElasticity(v, 0.40f, 500, true);
                break;
            default:
                break;

        }
    }
}
