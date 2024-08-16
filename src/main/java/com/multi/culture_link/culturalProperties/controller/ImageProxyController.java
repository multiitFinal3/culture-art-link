//package com.multi.culture_link.culturalProperties.controller;
//
//import com.amazonaws.util.IOUtils;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.util.concurrent.TimeUnit;
//
//@RestController
//public class ImageProxyController {
//
//    @GetMapping("/proxy/image")
//    public ResponseEntity<byte[]> proxyImage(@RequestParam("url") String imageUrl) throws IOException {
//        URL url = new URL(imageUrl);
//        try (InputStream in = url.openStream()) {
//            byte[] imageBytes = IOUtils.toByteArray(in);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_JPEG);
//            headers.setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS));
//            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
//        }
//    }
//}