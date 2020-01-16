package com.test.qianbailu.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.test.qianbailu.R;

import cn.jzvd.JzvdStd;

public class MyJzVideoPlayer extends JzvdStd {

    protected TextView tvSpeed;
    private float currentSpeed = 1.0f;
    private SpeedDialogFragment speedDialog;

    public MyJzVideoPlayer(Context context) {
        super(context);
    }

    public MyJzVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        tvSpeed = findViewById(R.id.tv_speed);
        tvSpeed.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        //tvSpeed.setVisibility(View.GONE);
        titleTextView.setVisibility(View.INVISIBLE);

        if (jzDataSource.objects == null) {
            jzDataSource.objects = new Object[]{2};
            currentSpeed = 1.0f;
        } else {
            currentSpeed = (float) jzDataSource.objects[0];
        }
        tvSpeed.setText(currentSpeed + "x");
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        tvSpeed.setVisibility(View.VISIBLE);
        titleTextView.setVisibility(VISIBLE);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        super.onClick(v);
        final int id = v.getId();
        if (id == R.id.tv_speed) {//0.5 1.0 1.25 1.5 2.0
            if (speedDialog == null) {
                speedDialog = new SpeedDialogFragment();
                speedDialog.setOnSpeedSelectListener(speed -> {
                    currentSpeed = speed;
                    mediaInterface.setSpeed(currentSpeed);
                    tvSpeed.setText(currentSpeed + "x");
                    jzDataSource.objects[0] = currentSpeed;
                    speedDialog.dismiss();
                });
            }
            if (getContext() instanceof FragmentActivity) {
                FragmentManager manager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                speedDialog.show(manager, "speed");
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_my_jz_video_player;
    }

}