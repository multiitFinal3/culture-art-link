package com.multi.culture_link.culturalProperties.model.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YoutubeConfig {

    @Value("${API-KEY.youtubeKey}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}