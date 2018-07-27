package cn.com.talkweb.jingyou.utils;
import java.util.UUID;
import java.util.Formatter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;  

/** 
 * @ClassName: 数字签名类 
 * @Description: 提供数字签名的加密算法
 * @author 张书镜
 * @date 2016-1-19 
 */
public class SignUtil {

	public static void main(String[] args) throws UnsupportedEncodingException {
    	String appId = "TEST_PRODUCT_21111";
        String appSecret = "7753E867A6DF40FAA0BBB4694D85D5F0";
        String param = "{\"data\":[{\"userId\": \"10000001\",\"msgContent\": \"2016年2月10日开家长会\",\"serviceId\": \"33434343\",\"mtId\": \"10001\"},{\"userId\": \"10000003\",\"msgContent\": \"明天上午不上课，全校放假\",\"serviceId\": \"33434343\",\"mtId\": \"10002\"}]}";
        String paramURLEncode = java.net.URLEncoder.encode(param,"UTF-8");
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String signature = sign(appId,appSecret,nonce_str,param,timestamp);
        System.out.println("signature:" + signature);
        System.out.println("param url encoade:" + paramURLEncode);
        System.out.println("param:" + param);
        System.out.println("appSecret:" + appSecret);
        System.out.println("appId:" + appId);
        System.out.println("nonce_str:" + nonce_str);
        System.out.println("timestamp:" + timestamp);
        System.out.println("http://localhost:8080/vpmc-openapi/send/low?appid="+appId+"&nonce_str="+nonce_str+"&param="+paramURLEncode+"&timestamp="+timestamp+"&sign="+signature);
    };

    public static String sign(String appId,String appSecret,String nonce_str, String param,String timestamp) {
        String string1;
        String signature = "";
        //注意这里参数名必须全部小写，且必须有序
        string1 ="appid"+appId+"noncestr"+nonce_str+"param"+param+"timestamp"+ timestamp+appSecret;

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return signature;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}
