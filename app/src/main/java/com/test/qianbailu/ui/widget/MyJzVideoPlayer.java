package com.test.qianbailu.ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.test.qianbailu.R;
import com.test.qianbailu.model.ConstKt;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZUtils;
import cn.jzvd.JzvdStd;
import top.cyixlq.core.utils.DisplayUtil;

public class MyJzVideoPlayer extends JzvdStd {

    protected TextView tvSpeed;
    protected TextView tvTip;
    private int currentSpeedIndex = 1;
    private AlertDialog speedDialog;
    private boolean showTvSpeed = true;
    private boolean showNormalTitle = false;
    private boolean showNormalBack = false;

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
        tvTip = findViewById(R.id.tv_tip);
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        if (!showTvSpeed) {
            tvSpeed.setVisibility(View.GONE);
        }
        if (!showNormalTitle) {
            titleTextView.setVisibility(View.INVISIBLE);
        }
        if (showNormalBack) {
            backButton.setVisibility(VISIBLE);
            backButton.setOnClickListener(v -> {
                if (!JzvdStd.backPress()) {
                    if (getContext() instanceof Activity) {
                        ((Activity)getContext()).finish();
                    }
                }
            });
        }
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        if (showTvSpeed) {
            tvSpeed.setVisibility(View.VISIBLE);
        }
        titleTextView.setVisibility(VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setUp(JZDataSource jzDataSource, int screen) {
        super.setUp(jzDataSource, screen);
        if (jzDataSource.objects == null) {
            jzDataSource.objects = new Object[]{2};
            currentSpeedIndex = 1;
        } else {
            currentSpeedIndex = (int) jzDataSource.objects[0];
        }
        tvSpeed.setText(getSpeedFromIndex(currentSpeedIndex) + "x");
    }

    private void setWindow(boolean isFull) {
        final Window window = speedDialog.getWindow();
        if (window == null) return;
        if (isFull) {
            window.getAttributes().width = DisplayUtil.INSTANCE.dp2px(300f);
            window.getAttributes().height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setGravity(Gravity.END);
        } else {
            window.getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
            window.getAttributes().height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setGravity(Gravity.BOTTOM);
        }
    }

    public void showSpeed(final boolean show) {
        this.showTvSpeed = show;
    }

    public void setShowNormalTitle(final boolean show) {
        this.showNormalTitle = show;
    }

    public void setShowNormalBack(final  boolean show) {
        this.showNormalBack = show;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        super.onClick(v);
        final int id = v.getId();
        if (id == R.id.tv_speed) {//0.5 1.0 1.25 1.5 2.0
            if (speedDialog == null) {
                final String[] speed = new String[]{"0.5倍", "1.0倍", "1.25倍", "1.5倍", "2.0倍"};
                speedDialog = new AlertDialog.Builder(getContext(), R.style.SpeedDialog)
                        .setTitle("倍速选择")
                        .setSingleChoiceItems(speed, currentSpeedIndex, (dialog, which) -> {
                            currentSpeedIndex = which;
                            mediaInterface.setSpeed(getSpeedFromIndex(currentSpeedIndex));
                            tvSpeed.setText(getSpeedFromIndex(currentSpeedIndex) + "x");
                            jzDataSource.objects[0] = currentSpeedIndex;
                            dialog.dismiss();
                        }).create();
                if (speedDialog.getWindow() != null) {
                    View decorView = speedDialog.getWindow().getDecorView();
                    decorView.setPadding(0, 0, 0, 0);
                    speedDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_speed_dialog);
                }
                speedDialog.setOnDismissListener(dialog -> {
                    if (screen == SCREEN_FULLSCREEN)
                        JZUtils.hideSystemUI(getContext());
                });
            }
            setWindow(screen == SCREEN_FULLSCREEN);
            speedDialog.show();
        }
    }

    public void setTip(String tip) {
        if (tvTip != null) {
            tvTip.setText(tip);
        }
    }

    @Override
    public long getDuration() {
        final long superDuration = super.getDuration();
        return superDuration > ConstKt.TEN_MINUTE ? ConstKt.TEN_MINUTE : superDuration;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_my_jz_video_player;
    }

    private float getSpeedFromIndex(int index) {
        switch (index) {
            case 0:
                return 0.5f;
            case 2:
                return 1.25f;
            case 3:
                return 1.5f;
            case 4:
                return 2.0f;
            default:
                return 1.0f;
        }
    }
}