package zip.agil.layar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;

@Service
public class StorageService {

    @Value("${spring.cloud.config.server.awss3.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.config.server.awss3.region}")
    private String region;

    @Value("${spring.cloud.config.server.awss3.bucket}")
    private String bucket;

    @Value("${spring.cloud.config.server.awss3.access-key}")
    private String accessKey;

    @Value("${spring.cloud.config.server.awss3.secret-key}")
    private String secretKey;

    AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

    S3Client s3Client = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build();

    public String upload(File file) {

        PutObjectResponse putObjectResponse = s3Client.putObject(request ->
                request
                        .bucket(bucket)
                        .key(file.getName()),
                file.toPath()
        );

        System.out.println(putObjectResponse.toString());

        return file.getName();
    }

}
