package com.test.qianbailu.utils;

import android.annotation.SuppressLint;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class UnSafeHostnameVerifier implements HostnameVerifier {
    @SuppressLint("BadHostnameVerifier")
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}
