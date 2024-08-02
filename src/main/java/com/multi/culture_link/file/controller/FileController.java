package com.multi.culture_link.file.controller;


import com.multi.culture_link.file.model.dto.NaverLocalSearchResponse;
import com.multi.culture_link.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class FileController {
    private final FileService fileService;

    // 상세 검색
    @PostMapping("/upload")
    public void uploadFile(
            @RequestParam("images") MultipartFile multipartFile,
            @RequestParam("dirName") String dirName
    ) {
//        fileService.uploadFiles(multipartFile, dirName);
    }

}
