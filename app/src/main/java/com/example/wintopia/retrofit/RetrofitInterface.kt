package com.example.wintopia.retrofit

import com.example.wintopia.data.MilkCowPhoto
import com.example.wintopia.data.UserList
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.utilssd.API_
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

import retrofit2.http.POST

import retrofit2.http.Multipart





interface RetrofitInterface {

    // @GET( EndPoint-자원위치(URI) )
    // DataClass > 요청 GET에 대한 응답데이터를 받아서 DTO 객체화할 클래스 타입 지정
    // getName > 메소드 명. 자유롭게 설정 가능, 통신에 영향x
    // @Path("post") String post > 매개변수. 매개변수 post가 @Path("post")를 보고 @GET 내부 {post}에 대입
    @GET("${API_.LOGIN}")
    fun  // 모든 유저의 id값만 받아오는 메서드(id 중복체크를 위해)
            getName(@Query("id") id: String?, @Query("pw") pw: String?): Call<UserList?>?
    
    @POST("test/signUp")
    fun  // 모든 유저의 id값만 받아오는 메서드(id 중복체크를 위해)
            getSignUp(@Query("id") id: String?, @Query("pw") pw: String?): Call<UserList?>?

//    @FormUrlEncoded
    @POST("${API_.INFOOUT}")
//    fun getData(@Query("item") item: String?): Call<String>?
    fun getData(@Body item: MilkCowInfoModel): Call<String>?


    @Multipart
    @POST("${API_.PHOTOINFOOUT}")
    fun profileSend(
        @Part file : MultipartBody.Part
    ): Call<String>


    //api를 관리해주는 인터페이스
    @Multipart
    @POST("${API_.PHOTOINFOOUT}")
    fun getPhoto(
        @Part id: String,
        @Part photo: MultipartBody.Part): Call<String?>?




//    @Multipart
//    @POST("경로")
//    fun writeStory(
//        @Header("Authorization") Authorization: String,
//        @Part imageList : List<MultipartBody.Part?>,
//        @Part("postData") postData : RequestBody
//    ) : Call<StoryResponse>


}