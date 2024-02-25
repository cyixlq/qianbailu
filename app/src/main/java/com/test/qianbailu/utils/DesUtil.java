package com.test.qianbailu.utils;

import android.util.Base64;

import androidx.annotation.NonNull;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DesUtil {
    /* 偏移变量，固定占8位字节 */
    private final static String IV_PARAMETER = "88886666";
    /* 密码 */
    private static final String PASSWORD = "7708807374520";
    /* 密钥算法 */
    private static final String ALGORITHM = "DES";
    /* 加密/解密算法-工作模式-填充模式 */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    /* 默认编码 */
    private static final String CHARSET = "utf-8";

    /**
     * Des加密
     * @param data 原始文本
     * @return 加密文本
     */
    @NonNull
    public static String encrypt(@NonNull String data) {
        try {
            Key secretKey = generateKey();
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(data.getBytes(CHARSET));
            //JDK1.8及以上可直接使用Base64，JDK1.7及以下可以使用BASE64Encoder
            //Android平台可以使用android.util.Base64
            return new String(Base64.encode(bytes, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * DES解密字符串
     *
     * @param data 待解密字符串
     * @return 解密后内容
     */
    @NonNull
    public static String decrypt(@NonNull String data) {
        try {
            Key secretKey = generateKey();
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return new String(cipher.doFinal(Base64.decode(data.getBytes(CHARSET), Base64.DEFAULT)), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 生成key
     *
     * @return 秘钥
     */
    private static Key generateKey() throws Exception {
        DESKeySpec dks = new DESKeySpec(PASSWORD.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }
}
