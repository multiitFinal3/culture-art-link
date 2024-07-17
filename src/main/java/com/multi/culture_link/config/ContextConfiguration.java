package com.multi.culture_link.config;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.multi.culture_link")
public class ContextConfiguration {
	
	
	@Bean
	public OkHttpClient okHttpClient() {
		return new OkHttpClient();
		
	}

	
	@Bean
	public Gson gson() {
		
		return new Gson();
		
	}
	
	
	
	
	
}
