package com.link.controllers;

import com.link.postservice.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:4200",  allowCredentials = "true")
@RestController
@RequestMapping("/api2")
public class ImageController {

    private S3Service s3Service;

    /**
     * Image endpoint that converts and passes an image file to be stored in an s3 bucket.
     * @param file Image file from HTTP request body (form-data).
     * @return String containing the file name that was uploaded.
     * @throws IOException
     */
    @PostMapping("/img/upload")
    public String uploadImg(@RequestParam("file") MultipartFile file) throws IOException {
        String keyName = file.getOriginalFilename();

        s3Service.uploadFile(keyName, file);
        return "success";
    }

    @Autowired
    public ImageController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public S3Service getS3Service() {
        return s3Service;
    }

    public void setS3Service(S3Service s3Service) {
        this.s3Service = s3Service;
    }
}
