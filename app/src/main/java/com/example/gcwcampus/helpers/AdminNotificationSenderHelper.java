package com.example.gcwcampus.helpers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminNotificationSenderHelper {
    private static final String FCM_BASE_URL = "https://fcm.googleapis.com/";

    public static void sendNotificationToTopic(Context context, String token, String title, String body){
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new AuthorizationInterceptor(token)).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FCM_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FcmApiService apiService = retrofit.create(FcmApiService.class);
        NotificationPayload payload = new NotificationPayload(token, title, body);

        Gson gson = new Gson();
        Log.d("Payload", gson.toJson(payload));

        apiService.sendNotification(payload).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()){
                    Log.d("Notification Sender", "Notification sent successfully");
                } else {
                    Log.d("Notification Sender", "Failed to send notification" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d("Notification Sender", "Error to send notification" + t.getMessage());
            }
        });
    }
}