package com.example.ggj_be.global.s3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.ggj_be.domain.member.Member;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    @Value("${spring.ncp.storage.endpoint}")
    private String endpoint;

    @Value("${spring.ncp.storage.access-key}")
    private String accessKey;

    @Value("${spring.ncp.storage.secret-key}")
    private String secretKey;

    @Value("${spring.ncp.storage.bucket-name}")
    private String bucketName;

    private AmazonS3 s3Client;

    @PostConstruct
    public void init() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

        // 엔드포인트 설정
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setSignerOverride("AWSS3V4SignerType");

        s3Client = AmazonS3Client.builder()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, ""))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withClientConfiguration(clientConfig)
                .build();
    }

    public String uploadProfileImage(MultipartFile file, String fileName) throws IOException {
        // S3로 파일 업로드
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // InputStream을 S3로 전송
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));
        log.info("File uploaded successfully");
        // 업로드된 파일의 URL 반환
        return s3Client.getUrl(bucketName, fileName).toString();
    }
}
