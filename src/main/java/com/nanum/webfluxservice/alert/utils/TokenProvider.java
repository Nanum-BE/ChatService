//package com.nanum.webfluxservice.alert.utils;
//
//import com.google.api.Authentication;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthException;
//import com.google.firebase.auth.FirebaseToken;
//import com.nanum.webfluxservice.alert.domain.Alert;
//import com.nanum.webfluxservice.alert.dto.AlertDto;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class TokenProvider {
//
//    public boolean validateToken(String token) {
//        FirebaseToken decodedToken = null;
//        try {
//            decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
//        } catch (FirebaseAuthException e) {
//            log.error("Firebase Exception {}", e.getLocalizedMessage());
//        }
//
//        if(decodedToken != null) {
//            return true;
//        } else
//
//            return false;
//    }
//}