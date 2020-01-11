package top.cyixlq.network.utils;

import android.annotation.SuppressLint;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;

import top.cyixlq.core.utils.FormatUtil;

public class NetSecurityUtil {

    /**
     * 加密数据  -> json字符串  (处理两次)
     * @param result	服务器端返回的加密字符串
     * @return		json字符串
     */
    public static String decodeData(String result){
        String Base64Str = new String(Base64.decode(result.getBytes(), Base64.NO_WRAP));
        return new String(Base64.decode(Base64Str.getBytes(), Base64.NO_WRAP));
    }

    /**
     * json字符串 -> 加密数据 (处理一次)
     * @param jsonStr json字符串
     * @return	加密字符串
     */
    public static String encodeData(String jsonStr) {
        return new String(Base64.encode(jsonStr.getBytes(), Base64.NO_WRAP));
    }

    /**
     *  加密第二阶段参数
     * @param data 重新组装后的键值对
     * @param secret 加密密钥
     * @return 加密后的第二阶段参数字符串
     */
    public static String desEncodeUserParam(Map<String, Object> data, String secret) {
        String jsonParam = FormatUtil.INSTANCE.getGson().toJson(data);
        return encode(jsonParam, secret);
    }

    /**
     * DES
     * @param key DES加密密钥
     * @return DES加密后字符串
     */
    @SuppressLint("TrulyRandom")
    private static String encode(String content, String key) {
        try{
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            @SuppressLint("GetInstance")
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            byte[] finalBytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(finalBytes, Base64.DEFAULT);

        }catch(Throwable e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param data 参数键值对
     * @return 加密信息后的参数字符串
     */
    public static String dataEncrypt(Map<String, Object> data) {
        String jsonStr = FormatUtil.INSTANCE.getGson().toJson(data);
        String Base64Str = new String(Base64.encode(jsonStr.getBytes(), Base64.NO_WRAP));
        return new String(Base64.encode(Base64Str.getBytes(), Base64.NO_WRAP));
    }

    /**
     * @param time
     *            时间戳
     * @param type
     *            文档的type BY_User_login
     * @param data
     *            json数据
     * @param client_id
     *            给管理员有的 by565fa4e56a9241
     * @param notify_id
     *            随意 111
     * @return sign 签名
     */
    public static String getMD5Sign(String time, String type, String data, String client_id, String notify_id) {
        try {
            return getMD5(time + type + data + client_id + notify_id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * MD5加密
     * @param sourceStr 源字符串
     * @return 签名后的字符串
     */
    private static String getMD5(String sourceStr) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(sourceStr.getBytes());
        byte[] b = md.digest();
        int i;
        StringBuilder buf = new StringBuilder();
        for (byte b1 : b) {
            i = b1;
            if (i < 0)
                i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }
}
