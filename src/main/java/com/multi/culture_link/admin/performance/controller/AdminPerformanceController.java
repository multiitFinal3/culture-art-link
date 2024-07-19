package com.multi.culture_link.admin.performance.controller;

import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
import com.multi.culture_link.admin.performance.service.PerformanceAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("/admin/performance-regulate")
public class AdminPerformanceController {

    @Autowired
    private PerformanceAPIService performanceAPIService;

    @GetMapping
    public String performanceManage(Model model,
                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                    @RequestParam(name = "size", defaultValue = "20") int size) {
        try {
            Page<PerformanceDTO> performances = performanceAPIService.fetchData(page, size);
            model.addAttribute("performances", performances.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", performances.getTotalPages());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/admin/performance/performanceRegulate";
    }

}
