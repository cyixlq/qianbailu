package com.test.qianbailu.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.test.qianbailu.MyApplication;
import com.test.qianbailu.utils.TaskExecutor;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class ScanWebView extends WebView {

    private static final String TAG = "ScanWebView";

    private final Pattern snifferMatch = Pattern.compile("http((?!http).){26,}?\\.(m3u8|mp4)\\?.*|http((?!http).){26,}\\.(m3u8|mp4)|http((?!http).){26,}?/m3u8\\?pt=m3u8.*|http((?!http).)*?default\\.ixigua\\.com/.*|http((?!http).)*?cdn-tos[^\\?]*|http((?!http).)*?/obj/tos[^\\?]*|http.*?/player/m3u8play\\.php\\?url=.*|http.*?/player/.*?[pP]lay\\.php\\?url=.*|http.*?/playlist/m3u8/\\?vid=.*|http.*?\\.php\\?type=m3u8&.*|http.*?/download.aspx\\?.*|http.*?/api/up_api.php\\?.*|https.*?\\.66yk\\.cn.*|http((?!http).)*?netease\\.com/file/.*");
    private ScanListener mScanListener;

    public ScanWebView(@NonNull Context context) {
        this(context, null);
    }

    public ScanWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        clearFocus();
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);// 隐藏缩放按钮
        webSettings.setUseWideViewPort(true);// 可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);// 保存表单数据
        webSettings.setJavaScriptEnabled(true);
        webSettings.setTextZoom(100);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportMultipleWindows(true);// 新加//我就是没有这一行，死活不出来。MD，硬是没有人写这一句！
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setMediaPlaybackRequiresUserGesture(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setGeolocationDatabasePath(MyApplication.INSTANCE.getDir("database", 0).getPath());
        webSettings.setGeolocationEnabled(true);
        CookieManager instance = CookieManager.getInstance();
        instance.setAcceptCookie(true);
        instance.setAcceptThirdPartyCookies(this, true);
        setWebViewClient(new MyWebViewClient());
    }

    @Override
    public void loadUrl(@NonNull String url) {
        Logger.t(TAG).d("loadUrl -> " + url);
        super.loadUrl(url);
    }

    public void setScanListener(ScanListener listener) {
        this.mScanListener = listener;
    }

    public void release() {
        stopLoading();
        loadUrl("about:blank");
        clearCache(true);
        removeAllViews();
        destroy();
    }

    public interface ScanListener {
        void onScanResult(String videoUrl, HashMap<String, String> headers);
        void onError(String msg);
    }

    private class MyWebViewClient extends WebViewClient {

        private final AtomicBoolean isFound = new AtomicBoolean(false);

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if(!request.getUrl().toString().startsWith("http")){
                return true;
            }else{
                return super.shouldOverrideUrlLoading(view, request);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(!url.startsWith("http")){
                return true;
            }else{
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

        /*解决ssl证书问题*/
        @SuppressLint("WebViewClientOnReceivedSslError")
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            WebResourceResponse res = scanVideo(url, null);
            if (res == null)
                return super.shouldInterceptRequest(view, url);
            return res;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            HashMap<String, String> webHeaders = new HashMap<>();
            try {
                Map<String, String> hds = request.getRequestHeaders();
                for (String k : hds.keySet()) {
                    if (k.equalsIgnoreCase("user-agent")
                            || k.equalsIgnoreCase("referer")
                            || k.equalsIgnoreCase("origin")) {
                        webHeaders.put(k, " " + hds.get(k));
                    }
                }
            } catch (Throwable ignore) {}
            WebResourceResponse res = scanVideo(url, webHeaders);
            if (res == null)
                return super.shouldInterceptRequest(view, request);
            return res;
        }

        /*@Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            post(() -> mScanListener.onError("解析视频出了些问题，请重新进入页面重试..."));
        }*/

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!isFound.get()) {
                TaskExecutor.INSTANCE.executeMain(() -> mScanListener.onError("视频解析失败，请重新进入界面重试..."));
            }
        }

        private WebResourceResponse scanVideo(final String url, HashMap<String, String> headers) {
            final boolean isImage = isImage(url);
            final boolean isVideo = isVideoFormat(url);
            Logger.t(TAG).d("scanVideo -> " + url + " ; isImage: " + isImage + "; isVideo: " + isVideo);
            if (isImage) {
                return new WebResourceResponse("image/png", null, null);
            }
            if (!isFound.get() && isVideo) {
                TaskExecutor.INSTANCE.executeMain(() -> {
                    mScanListener.onScanResult(url, headers);
                    Logger.t(TAG).d("scanVideo -> onScanResult: " + url);
                    stopLoading();
                });
                isFound.set(true);
                return new WebResourceResponse("text/plain", "utf-8", new ByteArrayInputStream("".getBytes()));
            }
            Logger.t(TAG).d("scanVideo -> isFound: " + isFound.get());
            return null;
        }

        private boolean isImage(String url) {
            return url.endsWith("/favicon.ico") || url.endsWith(".jpg")
                    || url.endsWith(".png") || url.endsWith(".gif");
        }

        private boolean isVideoFormat(final String url) {
            if (url.contains("=http") || url.contains("=https") || url.contains("=https%3a%2f") || url.contains("=http%3a%2f")) {
                return false;
            }
            if (snifferMatch.matcher(url).find()) {
                return !url.contains("cdn-tos") || (!url.contains(".js") && !url.contains(".css"));
            }
            return false;
        }
    }
}
