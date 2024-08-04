package com.multi.culture_link.exhibition.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.multi.culture_link.admin.exhibition.model.dao.ExhibitionKeywordDao;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionKeywordDto;
import com.multi.culture_link.culturalProperties.model.dto.Video;
import com.multi.culture_link.culturalProperties.model.dto.YoutubeConfig;
import com.multi.culture_link.exhibition.model.dao.ExhibitionDao;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExhibitionService {
    private final ExhibitionDao ExhibitionDao;
    private final ExhibitionKeywordDao exhibitionKeywordDao;
    private final OkHttpClient client;
    private final Gson gson;
    private final YoutubeConfig youtubeConfig;

    @Value("${API-KEY.youtubeKey}")
    private String youtubeKey;

    public List<ExhibitionApiDto> searchExhibition(Map<String, String> searchParams) {
        return ExhibitionDao.searchExhibition(searchParams);
    }

    public ExhibitionDto getExhibitionById(int userId, int exhibitionId){
        return ExhibitionDao.getExhibitionById(userId, exhibitionId);
    }

    public void setInterested(int userId, int exhibitionId, String state){
        ExhibitionDao.setInterested(userId, exhibitionId, state);
    }

    public List<ExhibitionDto> getExhibition(int userId){
        return ExhibitionDao.getExhibition(userId);
    }

    public List<ExhibitionApiDto> getUserInterestedExhibitions(int id){
        return ExhibitionDao.getUserInterestedExhibitions(id);
    }

    public List<Video> crawlYouTubeVideos(String query) {
        List<Video> videos = new ArrayList<>();
        try {
            String apiKey = youtubeConfig.getApiKeyByHS();
            String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=2&order=relevance&q=" + query + "&type=video&key=" + apiKey;


            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            JSONObject jsonObject = new JSONObject(response);
            JSONArray items = jsonObject.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject snippet = item.getJSONObject("snippet");

                String title = snippet.getString("title");
                String videoId = item.getJSONObject("id").getString("videoId");
                String link = "https://www.youtube.com/watch?v=" + videoId;
                String thumbnailUrl = snippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url");

                videos.add(new Video(title, link, thumbnailUrl));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videos;
    }


    public HashMap<String, List<ExhibitionKeywordDto>> getKeyword(int exhibitionId) {

        HashMap<String, List<ExhibitionKeywordDto>> result = new HashMap<String, List<ExhibitionKeywordDto>>();

        List<ExhibitionKeywordDto> exhibitionKeyword = exhibitionKeywordDao.getExhibtionKeyword(exhibitionId);
        List<ExhibitionKeywordDto> exhibitionCommentKeyword = exhibitionKeywordDao.getExhibtionCommentKeyword(exhibitionId);

        result.put("keyword", exhibitionKeyword);
        result.put("keywordComment", exhibitionCommentKeyword);

        return result;
    }

}
