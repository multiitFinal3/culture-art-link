package com.multi.culture_link.culturalProperties.model.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YoutubeConfig {

    @Value("${API-KEY.youtubeKey}")
    private String apiKey;

    @Value("${API-KEY.HS.youtubeKey}")
    private String apiKeyByHS;

    public String getApiKey() {
        return apiKey;
    }

    public String getApiKeyByHS() {
        return apiKeyByHS;
    }

}