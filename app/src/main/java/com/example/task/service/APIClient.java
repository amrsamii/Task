package com.example.task.service;

import com.example.task.ApiResponses.ResetPassResponse;
import com.example.task.ApiResponses.SignResponse;
import com.example.task.ApiResponses.UpdatePhoneResponse;
import com.example.task.model.User;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
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



    @Headers("Accept:application/json")
    @PATCH("/api/v1/user/auth/update-password")
    Call<ResponseBody> changePass(
            @Query("api_token") String apiToken,
            @Query("old_password") String oldPass,
            @Query("new_password") String newPass);


    @Headers("Accept:application/json")
    @POST("/api/v1/user/auth/request-update-phone")
    Call<UpdatePhoneResponse> requestUpdatePhone(@Query("api_token") String apiToken,
                                                 @Query("phone") String phone);



    @Headers("Accept:application/json")
    @PATCH("/api/v1/user/auth/update-phone")
    Call<ResponseBody> updatePhone(@Query("api_token") String apiToken,
                           @Query("phone") String phone,
                           @Query("temp_phone_code") String code);


    @Headers("Accept:application/json")
    @POST("/api/v1/user/auth/password/email")
    Call<ResetPassResponse> requestPassReset(@Query("name") String name,
                                             @Query("reset_method") String method);

    @Headers("Accept:application/json")
    @PATCH("/api/v1/user/auth/password/reset")
    Call<SignResponse> resetPass(
            @Query("reset_password_code") String code,
            @Query("name") String name,
            @Query("password") String pass,
            @Query("reset_method") String resetMethod);



    @Multipart
    @Headers("Accept:application/json")
    @POST("/api/v1/user/auth/file/upload")
    Call<ResponseBody> uploadFile(
            @Part MultipartBody.Part image
            );

}
