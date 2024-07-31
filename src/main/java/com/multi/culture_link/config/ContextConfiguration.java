package com.multi.culture_link.config;

import com.google.gson.Gson;
import com.mongodb.client.MongoClients;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
@ComponentScan(basePackages = "com.multi.culture_link")
public class ContextConfiguration {
	
	@Value("${spring.data.mongodb.uri}")
	private String mongoUri;
	
	@Value("${spring.data.mongodb.database}")
	private String mongoDatabase;
	
	
	@Bean
	public OkHttpClient okHttpClient() {
		return new OkHttpClient();
		
	}
	
	
	@Bean
	public Gson gson() {
		
		return new Gson();
		
	}
	
	
	@Bean
	public SimpleMongoClientDatabaseFactory mongoClientDatabaseFactory() {
		return new SimpleMongoClientDatabaseFactory(MongoClients.create(mongoUri), mongoDatabase);
	}
	
	
	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClientDatabaseFactory());
	}
	
	
}
