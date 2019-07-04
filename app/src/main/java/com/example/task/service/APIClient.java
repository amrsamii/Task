package com.example.task.service;

import com.example.task.ApiResponses.SignResponse;
import com.example.task.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIClient {


    @Headers("Accept:application/json")
    @POST("/api/v1/user/auth/signup")
    Call<SignResponse> signup(
            @Query("name") String name,
            @Query("phone") String phone,
            @Query("password") String password,
            @Query("email") String email);


    @Headers("Accept:application/json")
    @POST("/api/v1/user/auth/signin")
    Call<SignResponse> signin(
            @Query("name") String name,
            @Query("password") String password);


    @Headers("Accept:application/json")
    @PATCH("/api/v1/user/auth/update-profile")
    Call<SignResponse> updateProfile(
            @Query("api_token") String apiToken,
            @Query("name") String name,
            @Query("email") String email,
            @Query("image") String imageURl);
}
