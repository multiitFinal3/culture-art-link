package com.multi.culture_link.admin.exhibition.controller;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiResponseDto;
import com.multi.culture_link.admin.exhibition.service.ExhibitionApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/exhibition-regulate")
public class AdminExhibitionController {
    private final ExhibitionApiService exhibitionApiService;


    // api 정보 가져오기
    @GetMapping("/exhibition")
    public List<ExhibitionApiResponseDto.Item> listExhibitions(Model model) {
        ExhibitionApiResponseDto responseData= exhibitionApiService.fetchJsonData();
        // 나중에 client 에서 필요한 값만, 리스트로 보여주기
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
}
