package util;

import java.io.*;
import java.net.URL;
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

    public static final String PROPERTY_RESOURCE_LOCATION = "configure.properties";
    /**
     * tSpeakUrl : http://openapi.youdao.com/ttsapi?q=repeat&langType=en&sign=EAA3753AF1A014959A08FD8458C9F12D&salt=1547829523287&voice=4&format=mp3&appKey=1a797e22a80daabd
     * web : [{"value":["REPEAT","repetition","duplication","Redo"],"key":"重复"},{"value":["repeat","repetition"],"key":"重覆"},{"value":["repeatability","repeability","RSD","Reproducibility"],"key":"重复性"}]
     * query : 重复
     * translation : ["repeat"]
     * errorCode : 0
     * dict : {"url":"yddict://m.youdao.com/dict?le=eng&q=%E9%87%8D%E5%A4%8D"}
     * webdict : {"url":"http://m.youdao.com/dict?le=eng&q=%E9%87%8D%E5%A4%8D"}
     * basic : {"explains":["repetition"]}
     * l : zh-CHS2EN
     * speakUrl : http://openapi.youdao.com/ttsapi?q=%E9%87%8D%E5%A4%8D&langType=zh-CHS&sign=B11D679A554019DCAA8F5BF0C13B00EA&salt=1547829523287&voice=4&format=mp3&appKey=1a797e22a80daabd
     */

    private String tSpeakUrl;
    private String query;
    private String errorCode;
    private DictBean dict;
    private WebdictBean webdict;
    private BasicBean basic;
    private String l;
    private String speakUrl;
    private List<WebBean> web;
    private List<String> translation;

    public static String query(String str) throws IOException {

//        Map<String, String> paramMap = loadProperty();
        Map<String, String> paramMap = new HashMap<>();

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
        paramMap.put("url", "https://openapi.youdao.com/api");
        paramMap.put("appKey", "1a797e22a80daabd");
        paramMap.put("secret", "MYtWzgtBQZVZdorPcoYHnDcGyVo9LVtp");
        paramMap.put("from", "auto");
        paramMap.put("to", "EN");
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

    public String getTSpeakUrl() {
        return tSpeakUrl;
    }

    public void setTSpeakUrl(String tSpeakUrl) {
        this.tSpeakUrl = tSpeakUrl;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public DictBean getDict() {
        return dict;
    }

    public void setDict(DictBean dict) {
        this.dict = dict;
    }

    public WebdictBean getWebdict() {
        return webdict;
    }

    public void setWebdict(WebdictBean webdict) {
        this.webdict = webdict;
    }

    public BasicBean getBasic() {
        return basic;
    }

    public void setBasic(BasicBean basic) {
        this.basic = basic;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getSpeakUrl() {
        return speakUrl;
    }

    public void setSpeakUrl(String speakUrl) {
        this.speakUrl = speakUrl;
    }

    public List<WebBean> getWeb() {
        return web;
    }

    public void setWeb(List<WebBean> web) {
        this.web = web;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public static class DictBean {

        /**
         * url : yddict://m.youdao.com/dict?le=eng&q=%E9%87%8D%E5%A4%8D
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class WebdictBean {

        /**
         * url : http://m.youdao.com/dict?le=eng&q=%E9%87%8D%E5%A4%8D
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class BasicBean {

        private List<String> explains;

        public List<String> getExplains() {
            return explains;
        }

        public void setExplains(List<String> explains) {
            this.explains = explains;
        }
    }

    public static class WebBean {

        /**
         * value : ["REPEAT","repetition","duplication","Redo"]
         * key : 重复
         */

        private String key;
        private List<String> value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }
}
