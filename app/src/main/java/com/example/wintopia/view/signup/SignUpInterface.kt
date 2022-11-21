package com.example.wintopia.view.signup

import com.example.wintopia.view.login.UserList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SignUpInterface {

    // @GET( EndPoint-자원위치(URI) )
    // DataClass > 요청 GET에 대한 응답데이터를 받아서 DTO 객체화할 클래스 타입 지정
    // getName > 메소드 명. 자유롭게 설정 가능, 통신에 영향x
    // @Path("post") String post > 매개변수. 매개변수 post가 @Path("post")를 보고 @GET 내부 {post}에 대입
    @POST("test/signUp")
    fun  // 모든 유저의 id값만 받아오는 메서드(id 중복체크를 위해)
            getName(@Query("id") id: String?, @Query("pw") pw: String?): Call<UserList?>?
}