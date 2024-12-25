package zip.agil.layar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class StorageService {

    @Value("${spring.cloud.config.server.awss3.bucket}")
    private String bucket;

    @Autowired
    private S3Client s3Client;

    public String upload(MultipartFile multipartFile) {

        Long timeStamp = System.currentTimeMillis();

        String fileName = timeStamp + "_" + multipartFile.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        File file = multiPartFileToFile(multipartFile);

        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, file.toPath());

        System.out.println(putObjectResponse.toString());

        file.delete();

        return fileName;
    }

    private File multiPartFileToFile(MultipartFile multipartFile) {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try(FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return file;
    }

    public ResponseInputStream<GetObjectResponse> download(String fileName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        return s3Client.getObject(getObjectRequest);
    }

}
