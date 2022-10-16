package com.nanum.util.s3.presentation;

import com.nanum.util.s3.application.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.net.http.HttpHeaders;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

//@RequiredArgsConstructor
//@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@RequestMapping("/api/v1/images")
//public class S3Controller {
//    private final S3Service s3Uploader;
//    @PostMapping
//    public Mono<ResponseEntity<UploadResult>> uploadHandler(
//            @RequestHeader HttpHeaders headers,
//            @RequestBody Flux<ByteBuffer> imgFile) {
//        // ... see section 6
//        String upload = s3Uploader.upload(imgFile, "");
//        Map<String, Object> result = new HashMap<>();
//        result.put("result",upload);
//        return ResponseEntity.ok().body(result);
//    }
//}
