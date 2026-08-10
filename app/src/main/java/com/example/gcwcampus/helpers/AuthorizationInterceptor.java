package com.example.gcwcampus.helpers;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthorizationInterceptor implements Interceptor {

    String token;

    public AuthorizationInterceptor(String token){
        this.token = token;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request modifiedRequest = originalRequest.newBuilder().header("Authorization", "Bearer " + token).build();
        return chain.proceed(modifiedRequest);
    }
}
