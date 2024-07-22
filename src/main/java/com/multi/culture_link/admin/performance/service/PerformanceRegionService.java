package com.multi.culture_link.admin.performance.service;


import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PerformanceRegionService {

    private static final Map<String, Integer> regionMap = new HashMap<>();

    static {
        regionMap.put("서울특별시", 11);
        regionMap.put("부산광역시", 21);
        regionMap.put("대구광역시", 22);
        regionMap.put("인천광역시", 23);
        regionMap.put("광주광역시", 24);
        regionMap.put("대전광역시", 25);
        regionMap.put("울산광역시", 26);
        regionMap.put("경기도", 31);
        regionMap.put("강원특별자치도", 32);
        regionMap.put("충청북도", 33);
        regionMap.put("충청남도", 34);
        regionMap.put("전라북도", 35);
        regionMap.put("전라남도", 36);
        regionMap.put("경상북도", 37);
        regionMap.put("경상남도", 38);
        regionMap.put("제주특별자치도", 50);
        regionMap.put("세종특별자치시", 45);
    }

    public int getRegionId(String regionName) {
        return regionMap.getOrDefault(regionName, 0); // 기본값으로 0을 사용
    }
}
