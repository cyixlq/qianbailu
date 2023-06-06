package com.test.qianbailu.utils;

import android.annotation.SuppressLint;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 *  信任所有证书
 */
public class TrustAllManager implements X509TrustManager {
    @SuppressLint("TrustAllX509TrustManager")
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {}

    @SuppressLint("TrustAllX509TrustManager")
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {}

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }
}
