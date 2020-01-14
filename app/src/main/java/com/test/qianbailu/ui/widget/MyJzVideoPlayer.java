package com.test.qianbailu.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.test.qianbailu.R;

import cn.jzvd.JzvdStd;

public class MyJzVideoPlayer extends JzvdStd {

    protected TextView tvSpeed;
    private int currentSpeedIndex = 2;
    private AlertDialog speedDialog;

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
            currentSpeedIndex = 2;
        } else {
            currentSpeedIndex = (int) jzDataSource.objects[0];
        }
        if (currentSpeedIndex == 2) {
            tvSpeed.setText("1.0x");
        } else {
            tvSpeed.setText(getSpeedFromIndex(currentSpeedIndex) + "x");
        }
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
        if (id == R.id.tv_speed) {//0.5 0.75 1.0 1.25 1.5 1.75 2.0
            if (speedDialog == null) {
                final String[] speedArgs = new String[]{"0.5", "0.75", "1.0", "1.25", "1.5", "1.75", "2.0"};
                speedDialog = new AlertDialog.Builder(getContext())
                        .setTitle("倍速选择")
                        .setSingleChoiceItems(speedArgs, currentSpeedIndex, (dialog, which) -> {
                            currentSpeedIndex = which;
                            mediaInterface.setSpeed(getSpeedFromIndex(currentSpeedIndex));
                            tvSpeed.setText(getSpeedFromIndex(currentSpeedIndex) + "x");
                            jzDataSource.objects[0] = currentSpeedIndex;
                            dialog.dismiss();
                        }).create();
            }
            speedDialog.show();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_my_jz_video_player;
    }

    private float getSpeedFromIndex(int index) {
        float ret = 0f;
        if (index == 0) {
            ret = 0.5f;
        } else if (index == 1) {
            ret = 0.75f;
        } else if (index == 2) {
            ret = 1.0f;
        } else if (index == 3) {
            ret = 1.25f;
        } else if (index == 4) {
            ret = 1.5f;
        } else if (index == 5) {
            ret = 1.75f;
        } else if (index == 6) {
            ret = 2.0f;
        }
        return ret;
    }

}