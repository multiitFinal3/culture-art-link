package com.multi.culture_link.exhibition.controller;

import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/exhibition")
public class ExhibitionViewController {
    // 지도
    @Value("${API-KEY.naverClientId}")
    private String naverClientId;

    @GetMapping
    public String searchExhibition(
            @AuthenticationPrincipal VWUserRoleDTO currentUser,
            Model model
    ) {
        System.out.println("currentUser.getUsername() : " + currentUser.getUsername());
        model.addAttribute("userName", currentUser.getUsername());
        return "/exhibition/exhibition";
    }

    @GetMapping("/detail/{exhibitionId}")
    public String detailExhibition(Model model) {
        model.addAttribute("naverClientId", naverClientId);
        return "/exhibition/exhibitionDetail";

    }


}