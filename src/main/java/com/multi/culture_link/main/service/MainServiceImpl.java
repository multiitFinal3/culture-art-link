package com.multi.culture_link.main.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class MainServiceImpl implements MainService {
	
	private OkHttpClient client;
	private Gson gson;
	
	@Value("${API-KEY.naverClientId}")
	private String naverClientId;
	
	@Value("${API-KEY.naverClientSecret}")
	private String naverClientSecret;
	
	public MainServiceImpl(OkHttpClient client, Gson gson) {
		this.client = client;
		this.gson = gson;
	}
	
	@Override
	public HashMap<String, Double> findCenterPositionByRegion(String regionName) throws Exception {
		
		
		Request request = new Request.Builder()
				.url("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + regionName)
				.addHeader("X-NCP-APIGW-API-KEY-ID", naverClientId)
				.addHeader("X-NCP-APIGW-API-KEY", naverClientSecret)
				.get()
				.build();
		
		
		Response response = client.newCall(request).execute();
		String responseBody = response.body().string();
		JsonObject json = gson.fromJson(responseBody, JsonObject.class);
		
		System.out.println("json: " + json.toString());
		
		JsonArray addresses = json.getAsJsonArray("addresses");
		
		double latitude = (long) 126.978388;
		double longtitude = (long) 37.56661;
		
		if (addresses != null && addresses.size() > 0) {
			
			JsonObject addresses0 = addresses.get(0).getAsJsonObject();
			latitude = addresses0.get("x").getAsDouble();
			longtitude = addresses0.get("y").getAsDouble();
			
		}
		
		HashMap<String, Double> position = new HashMap<>();
		position.put("latitude", latitude);
		position.put("longtitude", longtitude);
		
		
		return position;
		
		
	}
	
	
}
