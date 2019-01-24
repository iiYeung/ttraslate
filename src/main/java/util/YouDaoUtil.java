package util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class YouDaoUtil {


    public YouDaoUtil() {
    }

    public static String query(String str) throws IOException {

        Map<String, String> paramMap = new HashMap<>();

        String result;

        paramMap.put("url", "https://openapi.youdao.com/api");
        paramMap.put("appKey", "1a797e22a80daabd");
        paramMap.put("secret", "MYtWzgtBQZVZdorPcoYHnDcGyVo9LVtp");
        paramMap.put("from", "auto");
        paramMap.put("to", "auto");
        String salt = String.valueOf(System.currentTimeMillis());
        paramMap.put("salt", salt);
        paramMap.put("sign", md5(paramMap.get("appKey") + str + salt + paramMap.get("secret")));
        paramMap.put("q", str);

        List<BasicNameValuePair> list = new ArrayList<>();

        for (Entry<String, String> stringStringEntry : paramMap.entrySet()) {

            String key = ((Entry) stringStringEntry).getKey().toString();

            String value = ((Entry) stringStringEntry).getValue().toString();

            if (!Objects.isNull(value)) {
                list.add(new BasicNameValuePair(key, value));
            }

        }

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(paramMap.get("url"));

        httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));

        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

        try {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "utf-8");
            EntityUtils.consume(httpEntity);
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 生成MD5
     */
    private static String md5(String string) {
        if (string == null) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //  使用指定的字节更新摘要
            mdInst.update(btInput);
            //  获得密文
            byte[] md = mdInst.digest();
            //  把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }


}
