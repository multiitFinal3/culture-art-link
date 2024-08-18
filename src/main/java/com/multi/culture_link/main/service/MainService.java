package com.multi.culture_link.main.service;

import java.util.ArrayList;
import java.util.HashMap;

public interface MainService {
	
	
	HashMap<String, Double> findCenterPositionByRegion(String regionName) throws Exception;
	
	HashMap<String, ArrayList> findAllCultureCategoryByRegion(int regionId) throws Exception;
}
