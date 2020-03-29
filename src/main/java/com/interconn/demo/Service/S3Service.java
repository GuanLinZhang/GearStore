package com.interconn.demo.Service;


import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    void uploadFileToS3Bucket(MultipartFile multipartFile,String maskedFilename, boolean enablePublicReadAccess);

    void deleteFileFromS3Bucket(String fileName);
}
