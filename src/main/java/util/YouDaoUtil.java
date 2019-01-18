package util;

import domain.YouDaoQuery;
import java.io.*;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class YouDaoUtil {

    public static final String PROPERTY_RESOURCE_LOCATION = "configure.properties";

    public static String query(String str) throws IOException {

        Map<String, String> paramMap = loadProperty();

        String result = null;

        if (Objects.isNull(paramMap)) {
            return null;
        }

//        if (paramMap.containsKey("url")) {
//            query.setUrl(paramMap.get("url"));
//        }
//
//        if (paramMap.containsKey("appKey")) {
//            query.setAppKey(paramMap.get("appKey"));
//        }
//
//        if (paramMap.containsKey("from")) {
//            query.setFrom(paramMap.get("from"));
//        }
//
//        if (paramMap.containsKey("to")) {
//            query.setTo(paramMap.get("to"));
//        }
//
//        if (paramMap.containsKey("secret")) {
//            query.setSecret(paramMap.get("secret"));
//        }

        String salt = String.valueOf(System.currentTimeMillis());
        paramMap.put("salt", salt);
        paramMap.put("sign", md5(paramMap.get("appKey") + str + salt + paramMap.get("secret")));
        paramMap.put("q", str);

        List<BasicNameValuePair> list = new ArrayList<>();

        Iterator it = paramMap.entrySet().iterator();

        while (it.hasNext()) {
            Entry e = (Entry) it.next();

            String key = e.getKey().toString();

            String value = e.getValue().toString();

            if (!Objects.isNull(value)) {
                list.add(new BasicNameValuePair(key, value));
            }

        }

//        public static final MediaType JSON = MediaType.get("application/json; charset=utf-8")
//
//        OkHttpClient httpClient = new OkHttpClient();
//
//        RequestBody request = RequestBody.create(JSON, list);
//
//        httpClient.newCall(request)

        CloseableHttpClient httpClient = HttpClients.createDefault();
        /**HttpPost*/
        HttpPost httpPost = new HttpPost(paramMap.get("url"));
        httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
        /**HttpResponse*/
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

    public static Map<String, String> loadProperty() throws IOException {
        Map<String, String> map = new HashMap<>();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

//        URL url = classLoader.getResource(PROPERTY_RESOURCE_LOCATION);
        URL url = new URL(PROPERTY_RESOURCE_LOCATION);

        if (Objects.isNull(url)) {
            return map;
        }

        FileInputStream fis = new FileInputStream(PROPERTY_RESOURCE_LOCATION);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));

        String result = null;

        while ((result = bufferedReader.readLine()) != null) {
            String[] strArr = result.split("=");

            if (strArr.length > 1) {
                map.put(strArr[0], strArr[1]);
            }
        }

        return map;
    }

    /**
     * 生成MD5
     */
    public static String md5(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = string.getBytes("utf-8");
            /** 获得MD5摘要算法的 MessageDigest 对象 */
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            /** 使用指定的字节更新摘要 */
            mdInst.update(btInput);
            /** 获得密文 */
            byte[] md = mdInst.digest();
            /** 把密文转换成十六进制的字符串形式 */
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }

}
