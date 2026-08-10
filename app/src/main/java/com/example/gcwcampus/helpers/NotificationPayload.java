package com.example.gcwcampus.helpers;

public class NotificationPayload {
    String to;
    NotificationData notification;

    public NotificationPayload(String to, String title, String body){
        this.to = to;
        this.notification = new NotificationData(title, body);
    }

    private static class NotificationData{
        String title;
        String body;

        public NotificationData(String title, String body) {
            this.title = title;
            this.body = body;
        }
    }
}
