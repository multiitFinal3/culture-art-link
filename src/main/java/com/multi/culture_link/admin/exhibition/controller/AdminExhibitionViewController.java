package com.multi.culture_link.admin.exhibition.controller;

import com.multi.culture_link.admin.exhibition.service.ExhibitionApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/exhibition-regulate")
public class AdminExhibitionViewController {
    private final ExhibitionApiService exhibitionApiService;

    @GetMapping
    public String exhibitionManage() {

        return "/admin/exhibition/exhibitionRegulate";

    }
}