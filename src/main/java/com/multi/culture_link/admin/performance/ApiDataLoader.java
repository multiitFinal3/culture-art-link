package com.multi.culture_link.admin.performance;

import com.multi.culture_link.admin.performance.service.PerformanceAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiDataLoader implements CommandLineRunner {

    @Autowired
    private PerformanceAPIService performanceAPIService;

    @Override
    public void run(String... args) throws Exception {
        try {
            performanceAPIService.fetchData(0, 20);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
