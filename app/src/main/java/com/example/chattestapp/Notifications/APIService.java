package com.example.chattestapp.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAwDLCUkw:APA91bH_oEAW6JOJc1vR41WyEEKevuZPSGSdgJJvv5UtNi4ztyqcPedo5Fj8bLj12dAhd79UZrVEnn2aAly2aaciFh3fEOdnruuW4Nfp-rh8ptYDHT_IZztGNUjdt-I0C27i0QqgJTtH"
            }
    )

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
