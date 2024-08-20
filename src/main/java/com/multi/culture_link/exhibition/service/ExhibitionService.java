package com.multi.culture_link.exhibition.service;

import com.google.gson.Gson;
import com.multi.culture_link.admin.exhibition.model.dao.ExhibitionKeywordDao;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionKeywordDto;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionKeywordPageDto;
import com.multi.culture_link.admin.exhibition.model.dto.api.PageResponseDto;
import com.multi.culture_link.culturalProperties.model.dto.Video;
import com.multi.culture_link.culturalProperties.model.dto.YoutubeConfig;
import com.multi.culture_link.exhibition.model.dao.ExhibitionDao;
import com.multi.culture_link.exhibition.model.dto.ExhibitionDto;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExhibitionService {
    private final ExhibitionDao exhibitionDao;
    private final ExhibitionKeywordDao exhibitionKeywordDao;
    private final OkHttpClient client;
    private final Gson gson;
    private final YoutubeConfig youtubeConfig;

    @Value("${API-KEY.youtubeKey}")
    private String youtubeKey;

    public List<ExhibitionApiDto> searchExhibition(Map<String, String> searchParams) {
        return exhibitionDao.searchExhibition(searchParams);
    }

    public ExhibitionDto getExhibitionById(int userId, int exhibitionId) {
        return exhibitionDao.getExhibitionById(userId, exhibitionId);
    }

    //    @Transactional
//    public void setInterested(int userId, int exhibitionId, String state){
//        // 현재 상태 확인
//        String currentState = exhibitionDao.getInterestState(userId, exhibitionId);
//
//        // 새로운 상태가 현재 상태와 같으면 아무 것도 하지 않음
//        if (state != null && state.equals(currentState)) {
//            //delete logic 추가
//
//            return;
//        }
//
//        exhibitionDao.setInterested(userId, exhibitionId, state);
//
//        List<String> keywords = exhibitionKeywordDao.getExhibitionKeywordById(exhibitionId);
//
//        // 키워드 업데이트
//        int countChange = 0;
//        if (state == null && currentState != null) {
//            // 관심 상태 취소
//            countChange = "interested".equals(currentState) ? -1 : 1;
//        } else if (state != null && currentState == null) {
//            // 새로운 관심 상태 설정
//            countChange = "interested".equals(state) ? 1 : -1;
//        } else if (state != null && currentState != null && !state.equals(currentState)) {
//            // 관심 상태 변경 (찜 -> 관심없음 또는 관심없음 -> 찜)
//            countChange = "interested".equals(state) ? 2 : -2;
//        }
//
//        for (String keyword : keywords) {
//            exhibitionKeywordDao.updateUserKeyword(userId, keyword, countChange);
//        }
//    }
//
    @Transactional
    public void setInterested(int userId, int exhibitionId, String newState) {
        // 현재 상태 확인
        String currentState = exhibitionDao.getInterestState(userId, exhibitionId);

        List<String> keywords = exhibitionKeywordDao.getExhibitionKeywordById(exhibitionId);

        // 키워드 업데이트
        int countChange = 0;
        if (currentState == null) {
            // 현재 상태가 없으면 새로운 상태를 설정
            countChange = "interested".equals(newState) ? 1 : -1;
            exhibitionDao.setInterested(userId, exhibitionId, newState);
        } else if (currentState.equals(newState)) {
            // 현재 상태와 새로운 상태가 같으면 관심 상태를 제거
            countChange = "interested".equals(currentState) ? -1 : 1;
            exhibitionDao.removeInterest(userId, exhibitionId);
        } else {
            // 현재 상태와 새로운 상태가 다르면 상태를 업데이트
            countChange = "interested".equals(currentState) ? -2 : 2;
            exhibitionDao.setInterested(userId, exhibitionId, newState);
        }

//        for (String keyword : keywords) {
//            exhibitionKeywordDao.updateUserKeyword(userId, keyword, countChange);
//        }
        saveKeyword(keywords, userId, countChange);
    }

    public void saveKeyword(List<String> keywords, int userId, int countChange) {
        System.out.println("keywords : " + keywords);

        for (String keyword : keywords) {
            exhibitionKeywordDao.updateUserKeyword(userId, keyword, countChange);
        }
    }

//    public void setInterested(int userId, int exhibitionId, String newState) {
//        String currentState = exhibitionDao.getInterestState(userId, exhibitionId);
//
//        if (currentState == null) {
//            // 현재 상태가 없으면 새로운 상태를 설정
//            exhibitionDao.setInterested(userId, exhibitionId, newState);
//        } else if (currentState.equals(newState)) {
//            // 현재 상태와 새로운 상태가 같으면 관심 상태를 제거
//            exhibitionDao.removeInterest(userId, exhibitionId);
//        } else {
//            // 현재 상태와 새로운 상태가 다르면 상태를 업데이트
//            exhibitionDao.setInterested(userId, exhibitionId, newState);
//        }
//
//    }


    public List<ExhibitionDto> getExhibition(int userId) {
        return exhibitionDao.getExhibition(userId);
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


    @Transactional
    public HashMap<String, List<ExhibitionKeywordDto>> getKeyword(int exhibitionId) {

        HashMap<String, List<ExhibitionKeywordDto>> result = new HashMap<String, List<ExhibitionKeywordDto>>();

        List<ExhibitionKeywordDto> exhibitionKeyword = exhibitionKeywordDao.getExhibitionKeyword(exhibitionId);
        List<ExhibitionKeywordDto> exhibitionCommentKeyword = exhibitionKeywordDao.getExhibitionCommentKeyword(exhibitionId);

        result.put("keyword", exhibitionKeyword);
        result.put("keywordComment", exhibitionCommentKeyword);

        return result;
    }


    @Transactional
    public PageResponseDto<ExhibitionKeywordPageDto> getAllKeyword(String cursor, int size) {
        List<ExhibitionKeywordPageDto> exhibitionAllKeyword = exhibitionKeywordDao.getExhibitionAllKeyword(cursor, size);

        String nextCursor = "";
        ExhibitionKeywordPageDto lastItem = exhibitionAllKeyword.get(exhibitionAllKeyword.size() - 1);
        nextCursor = lastItem.getNextCursor();

        PageResponseDto<ExhibitionKeywordPageDto> result = new PageResponseDto<ExhibitionKeywordPageDto>();
        result.setData(exhibitionAllKeyword);
        result.setNextCursor(nextCursor);

        return result;
    }


    @Transactional
    public List<ExhibitionKeywordDto> getAllKeywordByUser(Boolean isInterested, VWUserRoleDTO currentUser) {
        List<ExhibitionKeywordDto> exhibitionAllKeyword = exhibitionKeywordDao.getExhibitionAllKeywordByUser(isInterested, currentUser.getUserId());
        System.out.println("isInterested : " + isInterested);
        return exhibitionAllKeyword;
    }


    @Transactional
    public List<ExhibitionKeywordDto> getAllKeywordByUserAll(String orderBy) {
        List<ExhibitionKeywordDto> exhibitionInterestedKeyword = exhibitionKeywordDao.getExhibitionInterestedKeyword(orderBy);
        List<ExhibitionKeywordDto> exhibitionKeyword = exhibitionKeywordDao.getExhibitionKeywordAll(orderBy);

        Set<String> uniqueKeywords = new HashSet<>();

        for (ExhibitionKeywordDto dto : exhibitionInterestedKeyword) {
            uniqueKeywords.add(dto.getKeyword()); // 키워드가 무엇인지에 따라 변경
        }

        List<ExhibitionKeywordDto> combinedList = new ArrayList<>(exhibitionInterestedKeyword);

        for (ExhibitionKeywordDto dto : exhibitionKeyword) {
            if (!uniqueKeywords.contains(dto.getKeyword())) {
                combinedList.add(dto);
            }
        }

        return combinedList;
    }

    public List<ExhibitionApiDto> getLikeExhibition(int userId) {
        return exhibitionDao.getLikeExhibition(userId);
    }

    public List<ExhibitionApiDto> getUnlikeExhibition(int userId) {
        return exhibitionDao.getUnlikeExhibition(userId);
    }


    public List<ExhibitionDto> getUserRecommend(int userId) {
        // 1. 사용자의 관심 키워드 가져오기 (count >= 10)
        List<String> userKeyword = exhibitionKeywordDao.findKeywordsByCount(userId);
        if (userKeyword.isEmpty()) {
            return new ArrayList<>();
        }
        System.out.println("userKeyword: " + userKeyword);

        // 2. 해당 키워드를 가진 전시회 ID 가져오기
        List<Integer> exhibitionId = exhibitionKeywordDao.findExhibitionIdByKeywords(userKeyword);
        System.out.println("exhibitionId: " + exhibitionId);

        // 3. 전시회 정보 가져오기
        List<ExhibitionDto> exhibitions = exhibitionKeywordDao.findAllById(exhibitionId);
        System.out.println("exhibitions: " + exhibitions);

        return exhibitions;

    }

    public void toggleKeyword(int userId, List<Integer> keywordId, boolean isInterested) {
        for (Integer keyword : keywordId) {
            System.out.println("keywordId: " + keyword);
            int count = exhibitionKeywordDao.getKeywordCount(userId, keyword);

            if (count >= 10 || count < 0) {
                exhibitionKeywordDao.toggleKeyword(userId, keyword, 0);
            } else if (isInterested) {  // 찜에서 눌렀을 때
                exhibitionKeywordDao.toggleKeyword(userId, keyword, 10);
            } else if (!isInterested) { // 관심없음에서 눌렀을 때
                exhibitionKeywordDao.toggleKeyword(userId, keyword, -10);
            }

        }
    }


    public void saveKeywordByUser(int userId, String exhibitionKeyword, String type) {
        String[] keywords = exhibitionKeyword.trim().split(" ");
        System.out.println("type: " + type);

        for (String s : keywords) {
            System.out.println("keyword: " + s);
            ExhibitionKeywordDto dto = exhibitionKeywordDao.getExhibitionInterestedKeywordByKeyword(s, userId);

            if (dto == null) {
                exhibitionKeywordDao.updateUserKeyword(userId, s, (type.equals("L") ? 10 : -10));
            }

            if (type.equals("L")) {
                if (dto.getFrequency() < 10) {
                    exhibitionKeywordDao.updateUserKeyword(userId, s, 10 - dto.getFrequency());
                }
            } else {
                if (dto.getFrequency() > -10) {
                    exhibitionKeywordDao.updateUserKeyword(userId, s, -10 - dto.getFrequency());
                }
            }

        }
    }
}
