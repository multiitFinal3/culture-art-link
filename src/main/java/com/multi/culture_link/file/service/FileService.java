
package com.multi.culture_link.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {
//    private final AmazonS3Client amazonS3Client;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//
//    public String uploadFiles(MultipartFile multipartFile, String dirName) throws IOException {
//        File uploadFile = convert(multipartFile)
//                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));
//        return upload(uploadFile, dirName);
//    }
//
//    public String upload(File uploadFile, String filePath) {
//        String fileName = filePath + "/" + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
//        String uploadImageUrl = putS3(uploadFile, fileName);
//        removeNewFile(uploadFile);
//        return uploadImageUrl;
//    }
//
//    private String putS3(File uploadFile, String fileName) {
//        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
//        return amazonS3Client.getUrl(bucket, fileName).toString();
//    }
//
//    private void removeNewFile(File targetFile) {
//        if (targetFile.delete()) {
//            System.out.println("File delete success");
//            return;
//        }
//        System.out.println("File delete fail");
//    }
//
//    // 로컬에 파일 업로드 하기
//    private Optional<File> convert(MultipartFile file) throws IOException {
//        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
//        if (convertFile.createNewFile()) {
//            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
//                fos.write(file.getBytes());
//            }
//            return Optional.of(convertFile);
//        }
//        return Optional.empty();
//    }

}