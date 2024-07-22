package com.multi.culture_link.exhibition.controller;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import com.multi.culture_link.exhibition.service.ExhibitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exhibition")
public class ExhibitionController {
    private final ExhibitionService exhibitionService;
    @GetMapping("/search-exhibition")
    public List<ExhibitionApiDto> searchExhibition(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) String museum) {

        Map<String, String> searchParams = new HashMap<>();
        searchParams.put("title", title);
        searchParams.put("artist", artist);
        searchParams.put("museum", museum);

        return exhibitionService.searchExhibition(searchParams);
    }
}
