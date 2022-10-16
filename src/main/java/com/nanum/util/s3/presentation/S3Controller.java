package com.nanum.util.s3.presentation;



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
