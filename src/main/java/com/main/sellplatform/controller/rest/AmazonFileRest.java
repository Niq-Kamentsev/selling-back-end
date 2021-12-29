package com.main.sellplatform.controller.rest;

import com.main.sellplatform.service.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/file")
public class AmazonFileRest {
    private final AmazonClient amazonClient;

    @Autowired
    public AmazonFileRest(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping(value = "/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam(value = "file")MultipartFile file) throws Exception {
        String link = amazonClient.uploadFile(file);
        Map<Object, Object> response = new HashMap<>();
        response.put("link", link);
        return ResponseEntity.ok(response);
    }


}
