package com.multi.culture_link.exhibition.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.exhibition.model.dao.ExhibitionDao;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExhibitionService {
    private final ExhibitionDao ExhibitionDao;
    private final OkHttpClient client;
    private final Gson gson;

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

//    public String findExhibitionYoutube(String title) {
//        Request request = new Request.Builder()
//                .url("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=10&order=relevance&q=" + title + "&regionCode=KR&videoDuration=any&type=video&videoEmbeddable=true" + "&key=" + youtubeKey)
//                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
//                .addHeader("Connection", "keep-alive")
//                .get()
//                .build();
//
//        Response response = client.newCall(request).execute();
//        String responseBody = response.body().string();
//        JsonObject json = gson.fromJson(responseBody, JsonObject.class);
//        JsonArray items = json.getAsJsonArray("items");
//
//        JsonObject item = items.get(1).getAsJsonObject();
//
//        JsonObject id = item.getAsJsonObject("id");
//        String youtubeId = id.get("videoId").getAsString();
//
//        System.out.println("title : " + title);
//        System.out.println("youtubeId : " + youtubeId);
//
//        return youtubeId;
//    }





}
