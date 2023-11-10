package com.test.qianbailu.ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.test.qianbailu.R;
import com.test.qianbailu.model.ConstKt;
import com.test.qianbailu.utils.TaskExecutor;

import java.util.ArrayDeque;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import top.cyixlq.core.utils.DisplayUtil;

public class MyJzVideoPlayer extends JzvdStd {

    private static final String TAG = Jzvd.TAG + "-MyJzVideoPlayer";

    protected TextView tvSpeed;
    protected TextView tvTip;
    protected FrameLayout portraitButton;
    protected ImageView ivPortraitFull;
    protected TextView tvPortrait;
    protected Dialog mTempSpeedDialog;
    private int currentSpeedIndex = 1;
    private AlertDialog speedDialog;
    private boolean showTvSpeed = true;
    private boolean showNormalTitle = false;
    private boolean showNormalBack = true;
    private boolean isPortraitFull;
    private boolean isSeekLastPosition = false;
    private long realDuration = -1L;
    private final MutableLiveData<Integer> stateChangedListener = new MutableLiveData<>();

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
        portraitButton = findViewById(R.id.portraitFull);
        ivPortraitFull = findViewById(R.id.ivPortraitFull);
        tvPortrait = findViewById(R.id.tvPortrait);
        portraitButton.setOnClickListener(this);
        if (!showNormalTitle) {
            titleTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        isPortraitFull = false;
        fullscreenButton.setVisibility(VISIBLE);
        portraitButton.setVisibility(VISIBLE);
        tvPortrait.setVisibility(VISIBLE);
        ivPortraitFull.setImageResource(R.drawable.jz_enlarge);
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
        if (isPortraitFull) fullscreenButton.setVisibility(GONE);
        else portraitButton.setVisibility(GONE);
        ivPortraitFull.setImageResource(R.drawable.jz_shrink);
        tvPortrait.setVisibility(GONE);
        backButton.setVisibility(VISIBLE);
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
            setWindow(screen == SCREEN_FULLSCREEN && !isPortraitFull);
            speedDialog.show();
        } else if (id == R.id.portraitFull) {
            if (state == STATE_AUTO_COMPLETE) return;
            if (screen == SCREEN_FULLSCREEN) {
                //quit fullscreen
                backPress();
            } else {
                Log.d(TAG, "toFullscreenActivity [" + this.hashCode() + "] ");
                gotoPortraitScreenFullscreen();
            }
        }
    }
    public void gotoPortraitScreenFullscreen() {
        isPortraitFull = true;
        ViewGroup vg = (ViewGroup) getParent();
        vg.removeView(this);
        cloneAJzvd(vg);
        CONTAINER_LIST.add(vg);
        vg = (ViewGroup) (JZUtils.scanForActivity(getContext())).getWindow().getDecorView();//和他也没有关系
        vg.addView(this, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setScreenFullscreen();
        JZUtils.hideStatusBar(getContext());
        JZUtils.setRequestedOrientation(getContext(), NORMAL_ORIENTATION);
        JZUtils.hideSystemUI(getContext());//华为手机和有虚拟键的手机全屏时可隐藏虚拟键 issue:1326

    }


    public void setTip(String tip) {
        if (tvTip != null) {
            tvTip.setText(tip);
        }
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        if (realDuration < 0) {
            realDuration = getDuration();
        }
        if (isSeekLastPosition) {
            Snackbar.make(this, R.string.already_seek_to_last_position, BaseTransientBottomBar.LENGTH_SHORT)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                    .setAnchorView(R.id.anchorView)
                    .show();
            isSeekLastPosition = false;
        }
        mediaInterface.setSpeed(getSpeedFromIndex(currentSpeedIndex));
    }

    public long getRealDuration() {
        return realDuration < 0 ? 0 : realDuration;
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        stateChangedListener.setValue(STATE_AUTO_COMPLETE);
    }

    public void addStateChangedListener(LifecycleOwner owner, final Observer<Integer> listener) {
        this.stateChangedListener.observe(owner, listener);
    }

    /**
     * 跳转至上次播放的位置
     * @param lastPosition 上一次播放位置
     */
    public void seekToLsatPosition(long lastPosition) {
        seekToInAdvance = lastPosition;
        isSeekLastPosition = true;
    }

    @Override
    public void resetProgressAndTime() {
        super.resetProgressAndTime();
        if (CURRENT_JZVD != null) {
            CURRENT_JZVD.progressBar.setProgress(0);
            CURRENT_JZVD.progressBar.setProgress(0);
            if (CURRENT_JZVD instanceof JzvdStd) {
                JzvdStd jzvdStd = (JzvdStd) CURRENT_JZVD;
                jzvdStd.bottomProgressBar.setProgress(0);
                jzvdStd.bottomProgressBar.setSecondaryProgress(0);
            }
        }
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

    private boolean isTempSpeed = false;
    private final Runnable tempSpeed = () -> {
        isTempSpeed = true;
        mediaInterface.setSpeed(3);
        showTempSpeedDialog(true);
    };

    // 摘自JzvdStd START
    //doublClick 这两个全局变量只在ontouch中使用，就近放置便于阅读
    private long lastClickTime = 0;
    private final ArrayDeque<Runnable> delayTask = new ArrayDeque<>();
    // 摘自JzvdStd END

    // 整合Jzvd和JzvdStd的onTouch部分代码
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int id = v.getId();
        if (id == R.id.surface_container) {
            long doubleTime = 200;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i(TAG, "onTouch surfaceContainer actionDown [" + this.hashCode() + "] ");
                    mTouchingProgressBar = true;
                    mDownX = x;
                    mDownY = y;
                    mChangeVolume = false;
                    mChangePosition = false;
                    mChangeBrightness = false;
                    final long upTime = SystemClock.uptimeMillis();
                    TaskExecutor.INSTANCE.getMainHandler().postAtTime(tempSpeed, upTime + 1000);
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i(TAG, "onTouch surfaceContainer actionMove [" + this.hashCode() + "] ");
                    if (!isTempSpeed) {
                        float deltaX = x - mDownX;
                        float deltaY = y - mDownY;
                        float absDeltaX = Math.abs(deltaX);
                        float absDeltaY = Math.abs(deltaY);
                        if (screen == SCREEN_FULLSCREEN) {
                            if (!mChangePosition && !mChangeVolume && !mChangeBrightness) {
                                if (absDeltaX > THRESHOLD || absDeltaY > THRESHOLD) {
                                    getHandler().removeCallbacks(tempSpeed);
                                    cancelProgressTimer();
                                    if (absDeltaX >= THRESHOLD) {
                                        // 全屏模式下的CURRENT_STATE_ERROR状态下,不响应进度拖动事件.
                                        // 否则会因为mediaplayer的状态非法导致App Crash
                                        if (state != STATE_ERROR) {
                                            mChangePosition = true;
                                            mGestureDownPosition = getCurrentPositionWhenPlaying();
                                        }
                                    } else {
                                        //如果y轴滑动距离超过设置的处理范围，那么进行滑动事件处理
                                        if (mDownX < mScreenWidth * 0.5f) {//左侧改变亮度
                                            mChangeBrightness = true;
                                            WindowManager.LayoutParams lp = JZUtils.getWindow(getContext()).getAttributes();
                                            if (lp.screenBrightness < 0) {
                                                try {
                                                    mGestureDownBrightness = Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                                                    Log.i(TAG, "current system brightness: " + mGestureDownBrightness);
                                                } catch (Settings.SettingNotFoundException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                mGestureDownBrightness = lp.screenBrightness * 255;
                                                Log.i(TAG, "current activity brightness: " + mGestureDownBrightness);
                                            }
                                        } else {//右侧改变声音
                                            mChangeVolume = true;
                                            mGestureDownVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                        }
                                    }
                                }
                            }
                        }
                        if (mChangePosition) {
                            long totalTimeDuration = getMaxStepDuration();
                            mSeekTimePosition = (int) (mGestureDownPosition + (deltaX - THRESHOLD) * totalTimeDuration / mScreenWidth);
                            if (mSeekTimePosition > totalTimeDuration)
                                mSeekTimePosition = totalTimeDuration;
                            String seekTime = JZUtils.stringForTime(mSeekTimePosition);
                            String totalTime = JZUtils.stringForTime(getDuration());

                            showProgressDialog(deltaX, seekTime, mSeekTimePosition, totalTime, getDuration());
                        }
                        if (mChangeVolume) {
                            deltaY = -deltaY;
                            int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                            int deltaV = (int) (max * deltaY * 3 / mScreenHeight);
                            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mGestureDownVolume + deltaV, 0);
                            //dialog中显示百分比
                            int volumePercent = (int) (mGestureDownVolume * 100 / max + deltaY * 3 * 100 / mScreenHeight);
                            showVolumeDialog(-deltaY, volumePercent);
                        }
                        if (mChangeBrightness) {
                            deltaY = -deltaY;
                            int deltaV = (int) (255 * deltaY * 3 / mScreenHeight);
                            WindowManager.LayoutParams params = JZUtils.getWindow(getContext()).getAttributes();
                            if (((mGestureDownBrightness + deltaV) / 255) >= 1) {//这和声音有区别，必须自己过滤一下负值
                                params.screenBrightness = 1;
                            } else if (((mGestureDownBrightness + deltaV) / 255) <= 0) {
                                params.screenBrightness = 0.01f;
                            } else {
                                params.screenBrightness = (mGestureDownBrightness + deltaV) / 255;
                            }
                            JZUtils.getWindow(getContext()).setAttributes(params);
                            //dialog中显示百分比
                            int brightnessPercent = (int) (mGestureDownBrightness * 100 / 255 + deltaY * 3 * 100 / mScreenHeight);
                            showBrightnessDialog(brightnessPercent);
//                        mDownY = y;
                        }
                        if (mChangeBrightness || mChangePosition || mChangeVolume) {
                            TaskExecutor.INSTANCE.getMainHandler().removeCallbacks(tempSpeed);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "onTouch surfaceContainer actionUp [" + this.hashCode() + "] ");
                    TaskExecutor.INSTANCE.getMainHandler().removeCallbacks(tempSpeed);
                    if (isTempSpeed) {
                        mediaInterface.setSpeed(getSpeedFromIndex(currentSpeedIndex));
                        showTempSpeedDialog(false);
                        isTempSpeed = false;
                    } else {
                        mTouchingProgressBar = false;
                        dismissProgressDialog();
                        dismissVolumeDialog();
                        dismissBrightnessDialog();
                        if (mChangePosition) {
                            mediaInterface.seekTo(mSeekTimePosition);
                            long duration = getDuration();
                            int progress = (int) (mSeekTimePosition * 100 / (duration == 0 ? 1 : duration));
                            progressBar.setProgress(progress);
                        }
                        if (mChangeVolume) {
                            //change volume event
                        }
                        startProgressTimer();

                        // 摘自JzvdStd START
                        startDismissControlViewTimer();
                        if (mChangePosition) {
                            long duration = getDuration();
                            int progress = (int) (mSeekTimePosition * 100 / (duration == 0 ? 1 : duration));
                            bottomProgressBar.setProgress(progress);
                        }
                        //加上延时是为了判断点击是否是双击之一，双击不执行这个逻辑
                        Runnable task = () -> {
                            if (!mChangePosition && !mChangeVolume) {
                                onClickUiToggle();
                            }
                        };
                        v.postDelayed(task, doubleTime + 20);
                        delayTask.add(task);
                        while (delayTask.size() > 2) {
                            delayTask.pollFirst();
                        }

                        long currentTimeMillis = System.currentTimeMillis();
                        if (currentTimeMillis - lastClickTime < doubleTime) {
                            for (Runnable taskItem : delayTask) {
                                v.removeCallbacks(taskItem);
                            }
                            if (state == STATE_PLAYING || state == STATE_PAUSE) {
                                Log.d(TAG, "doublClick [" + this.hashCode() + "] ");
                                startButton.performClick();
                            }
                        }
                        lastClickTime = currentTimeMillis;
                        // 摘自JzvdStd END
                    }
                    break;
            }
        }
        // 摘自JzvdStd START
        else if (id == R.id.bottom_seek_progress) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    cancelDismissControlViewTimer();
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    break;
            }
        }
        // 摘自JzvdStd END
        return false;
    }

    public void showTempSpeedDialog(boolean isShow) {
        if (mTempSpeedDialog == null) {
            @SuppressLint("InflateParams")
            final View localView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_temp_speed, null);
            mTempSpeedDialog = createDialogWithView(localView);
            final Window window = mTempSpeedDialog.getWindow();
            window.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP);
        }
        if (isShow) {
            if (!mTempSpeedDialog.isShowing())
                mTempSpeedDialog.show();
        } else {
            if (mTempSpeedDialog.isShowing())
                mTempSpeedDialog.dismiss();
        }
    }

    // 获取滑动最大快进档位时长
    private long getMaxStepDuration() {
        final long totalDuration = getDuration();
        final long nowPosition = getCurrentPositionWhenPlaying();
        long result = nowPosition + ConstKt.MAX_STEP_DURATION;
        return Math.min(result, totalDuration);
    }
}