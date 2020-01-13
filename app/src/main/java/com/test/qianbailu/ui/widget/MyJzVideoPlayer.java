package com.test.qianbailu.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.test.qianbailu.R;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class MyJzVideoPlayer extends JzvdStd {
    TextView tvSpeed;
    int currentSpeedIndex = 2;

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

    public void setScreenNormal() {
        super.setScreenNormal();
        tvSpeed.setVisibility(View.GONE);
    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen) {
        super.setUp(jzDataSource, screen);
        if (screen == Jzvd.SCREEN_FULLSCREEN) {
            titleTextView.setVisibility(View.VISIBLE);
        } else {
            titleTextView.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            tvSpeed.setVisibility(View.VISIBLE);

        if (jzDataSource.objects == null) {
            jzDataSource.objects = new Object[]{2};
            currentSpeedIndex = 2;
        } else {
            currentSpeedIndex = (int) jzDataSource.objects[0];
        }
        if (currentSpeedIndex == 2) {
            tvSpeed.setText("倍速");
        } else {
            tvSpeed.setText(getSpeedFromIndex(currentSpeedIndex) + "X");
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        super.onClick(v);
        final int id = v.getId();
        if (id == R.id.tv_speed) {//0.5 0.75 1.0 1.25 1.5 1.75 2.0
            if (currentSpeedIndex == 6) {
                currentSpeedIndex = 0;
            } else {
                currentSpeedIndex += 1;
            }
            mediaInterface.setSpeed(getSpeedFromIndex(currentSpeedIndex));
            tvSpeed.setText(getSpeedFromIndex(currentSpeedIndex) + "X");
            jzDataSource.objects[0] = currentSpeedIndex;
        } else if (id == R.id.fullscreen) {
            if (state == STATE_AUTO_COMPLETE) return;
            if (screen == SCREEN_FULLSCREEN) {
                titleTextView.setVisibility(VISIBLE);
            } else {
                titleTextView.setVisibility(INVISIBLE);
            }
        } else if (id == R.id.back) {
            if (state == STATE_AUTO_COMPLETE) return;
            titleTextView.setVisibility(INVISIBLE);
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