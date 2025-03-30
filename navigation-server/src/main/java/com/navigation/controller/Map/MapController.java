package com.navigation.controller.Map;

import com.alibaba.fastjson.JSONObject;
import com.navigation.utils.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
/**
 * 高德地图
 */
@RestController
@RequestMapping("/map")
public class MapController {


    /**
     * 日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(MapController.class);

    /**
     * 公交路线规划
     * http://localhost:8080/map/bus?origin=116.20,39.56&destination=116.22,39.58&city1=110000&city2=110000
     *
     * @param origin      起点经纬度
     * @param destination 目的地
     * @param city1       起点所在城市
     * @param city2       目的地所在城市
     */
    @GetMapping("/bus")
    public String getBus(@RequestParam String origin, @RequestParam String destination,
                         @RequestParam String city1, @RequestParam String city2) {

        Map<String, String> params = new HashMap<>();

        params.put("origin", origin);
        params.put("destination", destination);
        params.put("city1", city1);
        params.put("city2", city2);

        // 获取包含原始内容的RouteResult
        MapUtils.RouteResult result = MapUtils.sendGet(MapUtils.WALKING_ROUTE_PLANNING, params);
        logger.info("请求公交路线规划,返回的结果为:{}", JSONObject.toJSONString(result.getParsedData()));
        return result.getRawContent();
    }

    /**
     * 步行路线规划
     * http://localhost:8080/map/walk?origin=116.20,39.56&destination=116.22,39.58&isindoor=0
     *
     * @param origin      起点信息
     * @param destination 目的地信息
     * @param isindoor    是否需要室内算路    0：不需要  1：需要
     * @return
     */
    @GetMapping("/walk")
    public String getWalk(@RequestParam String origin,
                          @RequestParam String destination,
                          @RequestParam String isindoor) {

        Map<String, String> params = new HashMap<>();
        params.put("origin", origin);
        params.put("destination", destination);
        params.put("isindoor", isindoor);

        logger.info("步行路线规划,入参请求为:{}", JSONObject.toJSONString(params));

        // 获取包含原始内容的RouteResult
        MapUtils.RouteResult result = MapUtils.sendGet(MapUtils.WALKING_ROUTE_PLANNING, params);

        // 记录解析后的结果
        logger.info("步行路线规划,解析结果为:{}", JSONObject.toJSONString(result.getParsedData()));

        // 直接返回原始内容
        logger.info("步行路线规划,原始响应为:{}", result.getRawContent());
        return result.getRawContent();
    }

    /**
     * 驾车路线规划
     * http://localhost:8080/map/drive?origin=116.20,39.56&destination=116.22,39.58
     *
     * @param origin      起点信息
     * @param destination 目的地信息
     * @return
     */
    @GetMapping("/drive")
    public String getDrive(@RequestParam String origin, @RequestParam String destination) {

        Map<String, String> params = new HashMap<>();

        params.put("origin", origin);
        params.put("destination", destination);
        logger.info("驾车路线规划,入参请求为:{}", JSONObject.toJSONString(params));
        // 获取包含原始内容的RouteResult
        MapUtils.RouteResult result = MapUtils.sendGet(MapUtils.WALKING_ROUTE_PLANNING, params);
        logger.info("驾车路线规划,返回的结果为:{}", JSONObject.toJSONString(result.getParsedData()));
        return result.getRawContent();
    }

    /**
     * 骑行路线规划
     * http://localhost:8080/map/riding?origin=116.20,39.56&destination=116.22,39.58
     *
     * @param origin      起点信息
     * @param destination 目的地信息
     * @return
     */
    @GetMapping("/riding")
    public String getRiding(@RequestParam String origin, @RequestParam String destination) {

        Map<String, String> params = new HashMap<>();

        params.put("origin", origin);
        params.put("destination", destination);
        logger.info("骑行路线规划,入参请求为:{}", JSONObject.toJSONString(params));
        // 获取包含原始内容的RouteResult
        MapUtils.RouteResult result = MapUtils.sendGet(MapUtils.WALKING_ROUTE_PLANNING, params);
        logger.info("骑行路线规划,返回的结果为:{}", JSONObject.toJSONString(result.getParsedData()));
        return result.getRawContent();
    }
}