package com.multi.culture_link.exhibition.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/exhibition")
public class ExhibitionViewController {

    @GetMapping
    public String exhibition() {

        return "/exhibition/exhibition";

    }
}