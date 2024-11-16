package com.seven.delivr.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.seven.delivr.base.AppService;
import com.seven.delivr.util.Utilities;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@Slf4j
public class FileService implements AppService {
    private final List<String> SUPPORTED = Arrays.asList(MediaType.IMAGE_JPEG_VALUE, "image/jpg", MediaType.IMAGE_PNG_VALUE);
    private final float SIZE_LIMIT = 512_000.0F;
    private final AmazonS3 s3Client;
    private final Environment environment;
    @Value("${aws.s3.bucket.name}")
    private String bucketName;
    @Value("${aws.s3.bucket.region}")
    private String bucketRegionName;

    public FileService(AmazonS3 s3Client, Environment environment) {

        this.s3Client = s3Client;
        this.environment = environment;
    }

    @Transactional
    public List<String> uploadProductImages(List<MultipartFile> mpFiles) {
        if (isValid(mpFiles)) {
            String pathString = String.format("src/main/resources/files%d/", System.currentTimeMillis());
            List<String> keyList = new ArrayList<>();
            File dir = null;
            String env = environment.getProperty("ENV");

            if (!"develop".equals(env) && !"prod".equals(env))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Unrecognized environment");

            try {
                dir = Files.createDirectory(Path.of(pathString)).toFile();
                mpFiles.stream().forEach(mpf -> {
                    try {
                        String safeName = Utilities.clean(mpf.getOriginalFilename(), "[ ]");
                        File f = new File(Path.of(pathString + "product" + System.currentTimeMillis() + safeName).toUri());
                        mpf.transferTo(f);

                        keyList.add(env + "/products/" + f.getName());
                    } catch (IOException ex) {
                        log.error("IO Exception: " + ex.getMessage());
                    }
                });

                TransferManager tm = TransferManagerBuilder.standard().withS3Client(s3Client).build();
                Transfer transfer = tm.uploadFileList(bucketName, env + "/products/", dir, Arrays.asList(dir.listFiles()));

                transfer.waitForCompletion();
                tm.shutdownNow(false);

                return keyList;
            } catch (InterruptedException ex) {
                log.error("Interrupted Exception: " + ex.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Apologies, something went wrong on our end");
            } catch (Exception ex) {
                log.error("Exception: " + ex.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Apologies, something went wrong on our end");
            } finally {
                //Clean
                if (dir != null) {
                    Arrays.stream(dir.listFiles()).forEach(File::delete);
                    dir.delete();
                }
            }
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only JPEG and PNG formats less than 500kb are allowed");
    }

    @Transactional
    public String uploadLogo(List<MultipartFile> files) {
        if (isValid(files)) {
            MultipartFile mpf = files.get(0);
            File file;
            try {
                file = Files.createTempFile("logo", "image/png".equals(mpf.getContentType()) ? ".png" : ".jpg").toFile();
                mpf.transferTo(file);
            } catch (Exception ex) {
                log.error("Exception: " + ex.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Apologies, something went wrong on our end");
            }
            String safeName = Utilities.clean(mpf.getOriginalFilename(), "[ ]");
            String env = environment.getProperty("ENV");

            if (!"develop".equals(env) && !"prod".equals(env))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Unrecognized environment");

            String key = env + "/logos/logo" + System.currentTimeMillis() + safeName;
            s3Client.putObject(bucketName, key, file);
            return key;
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only JPEG and PNG formats less than 500kb are allowed");
    }

    @Transactional
    public void delete(String key) {
        s3Client.deleteObject(bucketName, key);
    }

    private boolean isValid(List<MultipartFile> files) {
        for (MultipartFile f : files) {
            if (f.getSize() > SIZE_LIMIT || !SUPPORTED.contains(Objects.requireNonNull(f.getContentType()).toLowerCase()))
                return false;
        }
        return true;
    }
}
