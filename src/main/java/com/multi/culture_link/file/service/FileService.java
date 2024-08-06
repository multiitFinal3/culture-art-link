
package com.multi.culture_link.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
    private final AmazonS3Client amazonS3Client;

    // 넣을 버킷 이름
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 파일이 저장된 경로
    public String uploadFiles(MultipartFile multipartFile, String path) throws IOException {
        String uploadImageUrl = putS3(multipartFile, path.replaceFirst("^/", ""));

        return uploadImageUrl;
    }

    private String putS3(MultipartFile multipartFile, String path) throws IOException {
        String fileUrl = "";

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        try {
            // request 보낼 데이터 모음
            String filePath = path.replaceFirst("^/", "");
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucket, filePath, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead); // 접근 권한 설정
            amazonS3Client.putObject(putObjectRequest); // 업로드
            fileUrl = amazonS3Client.getUrl(bucket, filePath).toString();
        }catch (IOException e) {
            throw new IOException("파일 업로드 중 오류 발생", e);
        }
        return fileUrl;
    }

}
