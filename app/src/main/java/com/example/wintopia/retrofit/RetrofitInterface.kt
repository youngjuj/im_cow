package com.example.wintopia.retrofit

import androidx.lifecycle.MutableLiveData
import com.example.wintopia.data.MyPageInfo
import com.example.wintopia.data.UserList
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.utilssd.API_
import com.google.gson.JsonObject
import com.squareup.moshi.Json
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

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
    @POST("${API_.SIGNUP}")
    fun  // 모든 유저의 id값만 받아오는 메서드(id 중복체크를 위해)
            getSignUp(@Query("id") id: String?, @Query("pw") pw: String?): Call<UserList?>?

    // 사진 하나 저장
    @Multipart
    @POST("${API_.COWIMGUP}")
    fun getPhoto(
        @Part file: MultipartBody.Part
    ): Call<String?>?

    // cow_id에 맞는 사진 하나
    @Multipart
    @POST("${API_.COWIMGOUT}")
    fun cowImage(
        @Query ("user_id") id: String,
        @Query ("cow_id") cow_id: String,
        @Part ("num") num:String
    ): Call<MultipartBody.Part>

    // cow_id에 맞는 사진 리스트 업로드
    @Multipart
    @PUT("${API_.COWIMGLIST}")
    fun cowImageList(
        @Part files: ArrayList<MultipartBody.Part>
    ) : Call<String?>

    // cow wish list
    @GET("${API_.COWWISH}")
    fun cowWish(@Query ("cow_id") cow_id: String): Call<String>

    // myPage Info
    @GET("${API_.MYPAGEINFO}")
    fun mypageInfo(@Query ("user_id") user_id: String): Call<MyPageInfo>

    //    @Multipart
    // cow 전체 정보 불러오기
    @GET("${API_.COWINFOALL}")
    fun cowListAll(@Query ("user_id") user_id: String
    ): Call<MutableList<MilkCowInfoModel>>

    // cow_id에 맞는 정보 가져오기
    @GET("${API_.COWINFOONE}")
    fun getData(@Query("cow_id") cow_id: String): Call<List<MilkCowInfoModel>>

    //    @FormUrlEncoded
    // cow Info Update
    @PUT("${API_.COWINFOUPDATE}")
    fun infoUpdate(@Query("cow_id") cow_id: String,
                   @Body item: MilkCowInfoModel
    ): Call<String>?

    // Info Delete
    @DELETE("${API_.COWINFODELETE}")
    fun cowInfoDelete(@Query ("cow_id") cow_id: String): Call<String>

    // new Cow registration
    @POST("${API_.COWINFOREGIST}")
    fun cowInfoRegist(@Query ("user_id") user_id: String,
                      @Body item: MilkCowInfoModel
    ): Call<String>?

    // result of new cow registration
    @GET("${API_.COWINFOREGIST}")
    fun getResult(@Query("cow_id") cow_id: String): Call<String>?


//    @Multipart
//    @POST("경로")
//    fun writeStory(
//        @Header("Authorization") Authorization: String,
//        @Part imageList : List<MultipartBody.Part?>,
//        @Part("postData") postData : RequestBody
//    ) : Call<StoryResponse>


}