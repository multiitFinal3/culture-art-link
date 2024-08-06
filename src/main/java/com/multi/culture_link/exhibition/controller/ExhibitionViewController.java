package com.multi.culture_link.exhibition.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
@RequestMapping("/exhibition")
public class ExhibitionViewController {
    // 지도
    @Value("${API-KEY.naverClientId}")
    private String naverClientId;

    @GetMapping
    public String searchExhibition() {

        return "/exhibition/exhibition";

    }

    @GetMapping("/detail/{exhibitionId}")
    public String detailExhibition(Model model) {
        model.addAttribute("naverClientId", naverClientId);
        return "/exhibition/exhibitionDetail";

    }


}