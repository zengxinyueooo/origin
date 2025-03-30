package com.navigation.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    public static class RouteResult {
        private String rawContent; // 高德API原始响应
        private Map<String, String> parsedData; // 解析后的数据

        public RouteResult(String rawContent, Map<String, String> parsedData) {
            this.rawContent = rawContent;
            this.parsedData = parsedData;
        }

        // getters and setters
        public String getRawContent() {
            return rawContent;
        }
        public void setRawContent(String rawContent) {
            this.rawContent = rawContent;
        }
        public Map<String, String> getParsedData() {
            return parsedData;
        }
        public void setParsedData(Map<String, String> parsedData) {
            this.parsedData = parsedData;
        }
    }


    /**日志对象*/
    private static final Logger logger = LoggerFactory.getLogger(MapUtils.class);

    /**
     * 驾车路线规划
     */
    public final static String DRIVING_ROUTE_PLANNING ="https://restapi.amap.com/v5/direction/driving?parameters";

    /**
     * 步行路线规划
     */
    public final static String WALKING_ROUTE_PLANNING ="https://restapi.amap.com/v5/direction/walking?parameters";

    /**
     * 骑行路线规划
     */
    public final static String CYCLING_ROUTE_PLANNING ="https://restapi.amap.com/v5/direction/bicycling?parameters";

    /**
     * 公交路线规划
     */
    public final static String BUS_ROUTE_PLANNING ="https://restapi.amap.com/v5/direction/transit/integrated?parameters";

    /**
     * 高德key
     */
    public final static String KEY ="2c5617149f45866b04506b7b61a8bda7";

    /**
     * 发送get请求
     * @param url
     * @return
     */
    public static RouteResult sendGet(String url, Map<String, String> params) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String rawResponse = null;
        Map<String, String> parsedData = new HashMap<>();

        try {
            URI uri = getBuilder(url, params);
            HttpGet httpGet = new HttpGet(uri);
            CloseableHttpResponse response = httpclient.execute(httpGet);

            // 只读取一次响应内容
            rawResponse = EntityUtils.toString(response.getEntity(), "UTF-8");

            // 使用字符串内容进行解析
            parsedData = getRoute(rawResponse, url);

            response.close();
        } catch (Exception e) {
            e.printStackTrace();
            rawResponse = "{\"error\": \"" + e.getMessage() + "\"}";
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new RouteResult(rawResponse, parsedData);
    }

    /**
     * 根据不同的路径规划获取距离
     * @param jsonContent
     * @return
     */
    private static Map<String, String> getRoute(String jsonContent, String url) throws Exception {
        Map<String, String> map = new HashMap<>();
        String distance = "";
        String duration = "";

        JSONObject jsonObject = JSON.parseObject(jsonContent);
        if (jsonObject == null) {
            throw new RuntimeException("解析JSON失败");
        }

        JSONObject route = jsonObject.getJSONObject("route");
        if (route == null) {
            throw new RuntimeException("返回结果中缺少route字段");
        }

        if (BUS_ROUTE_PLANNING.equals(url)) {
            JSONArray transits = route.getJSONArray("transits");
            if (transits != null && !transits.isEmpty()) {
                JSONObject transit = transits.getJSONObject(0);
                distance = transit.getString("distance");
                JSONObject cost = transit.getJSONObject("cost");
                duration = cost != null ? cost.getString("duration") : "";
            }
        }
        else if (DRIVING_ROUTE_PLANNING.equals(url)) {
            JSONArray paths = route.getJSONArray("paths");
            if (paths != null && !paths.isEmpty()) {
                JSONObject path = paths.getJSONObject(0);
                distance = path.getString("distance");
                JSONObject cost = path.getJSONObject("cost");
                duration = cost != null ? cost.getString("duration") : "";
            }
        }
        else {
            JSONArray paths = route.getJSONArray("paths");
            if (paths != null && !paths.isEmpty()) {
                JSONObject path = paths.getJSONObject(0);
                distance = path.getString("distance");
                duration = path.getString("duration");
            }
        }

        map.put("distance", distance);
        map.put("duration", duration);
        return map;
    }

    /**
     * 封装URI
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    private static URI getBuilder(String url, Map<String, String> params) throws Exception{

        // 驾车路线规划
        String origin = params.get("origin");
        String destination = params.get("destination");
        String city1 = params.get("city1");
        String city2 = params.get("city2");

        // 步行路线规划
        String isindoor = params.get("isindoor");

        URIBuilder uriBuilder = new URIBuilder(url);
        // 公共参数
        uriBuilder.setParameter("key", KEY);
        uriBuilder.setParameter("origin", origin);
        uriBuilder.setParameter("destination", destination);
        uriBuilder.setParameter("show_fields", "cost");

        // 驾车路线规划
        if(StringUtils.isNotBlank(city1) && StringUtils.isNotBlank(city2)){

            uriBuilder.setParameter("city1", city1);
            uriBuilder.setParameter("city2", city2);
        }
        // 步行路线规划
        if(StringUtils.isNotBlank(isindoor)){

            uriBuilder.setParameter("isindoor", isindoor);
        }
        URI uri = uriBuilder.build();
        return uri;
    }
}