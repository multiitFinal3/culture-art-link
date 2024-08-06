package com.multi.culture_link.admin.exhibition.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiResponseDto;
import com.multi.culture_link.admin.exhibition.service.ExhibitionApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/exhibition-regulate")
public class AdminExhibitionController {
    private final ExhibitionApiService exhibitionApiService;


    // api 정보 가져오기
    @GetMapping("/exhibition")
    public List<ExhibitionApiResponseDto.Item> listExhibitions(Model model) {
        ExhibitionApiResponseDto responseData= exhibitionApiService.fetchJsonData();
//        System.out.println("getNumOfRows: " + responseData.getBody().getNumOfRows());
//        System.out.println("pageNo: " + responseData.getBody().getPageNo());
//        System.out.println("totalCount: " + responseData.getBody().getTotalCount());
        // System.out.println(responseData.getBody().getItems().getItem().get(1));
        return responseData.getBody().getItems().getItem();
    }

    // 선택 api 정보 저장하기
    @PostMapping("/exhibition")
    @ResponseBody
    public ResponseEntity<String> saveExhibition(
            @RequestBody List<ExhibitionApiResponseDto.Item> data
    ) {

        exhibitionApiService.saveExhibition(data);
        return ResponseEntity.ok("Exhibition saved successfully");
    }

    // db 에서 정보 불러오기
    @GetMapping("/db-exhibition")
    public List<ExhibitionApiDto> getDbExhibitions(){
        return exhibitionApiService.getExhibition();
    }

    // db 정보 수정
    @PatchMapping("/db-exhibition")
    public void updateExhibition(
            @RequestBody List<ExhibitionApiDto> data
    ) {
        exhibitionApiService.updateExhibition(data);

    }
    
    // db 정보 삭제
    @DeleteMapping("/db-exhibition")
    public void deleteExhibition(
            @RequestBody List<Integer> id
    ) {
        exhibitionApiService.deleteExhibition(id);
    }

    // 서버가 켜져있다면 정각에 키워드 뽑기
    @Scheduled(cron = "0 0 0 * * ?")
    public void runDaily() throws Exception {
        exhibitionApiService.saveKeyword();

    }

    // for test
//    @Scheduled(fixedRate = 160000) // 60000 밀리초 = 1분
//    public void runEveryMinute() throws Exception {
//        exhibitionApiService.saveKeyword();
//    }

}
