//package com.nanum.exception;
//
//import feign.Response;
//import feign.codec.ErrorDecoder;
//
//public class FeignError implements ErrorDecoder {
//    @Override
//    public Exception decode(String methodKey, Response response) {
//        switch (response.status()){
//            case 400:
//                break;
//            case 404:
//                if(methodKey.contains("getUser")){
//                    throw new UserNotFoundException("User is empty.");
//                }
//                if(methodKey.contains("getUsersById")){
//                    throw new UserNotFoundException("UserId is empty.");
//                }
//                break;
//            default:
//                return new Exception(response.reason());
//        }
//        return null;
//    }
//}