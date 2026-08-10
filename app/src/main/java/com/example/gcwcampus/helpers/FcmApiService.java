package com.example.gcwcampus.helpers;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FcmApiService {
    @Headers({"Content-Type: application/json"})
    @POST("v1/projects/gcw-campus/messages:send")
    Call<Void> sendNotification(@Body NotificationPayload notificationPayload);
}