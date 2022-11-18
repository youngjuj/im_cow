package com.example.wintopia.view.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("loginId")
    val id: String? = null,
    @SerializedName("loginPw")
    val pw: String? = null

//    @Override
//    public StringToString(){
//        return "PostResult 'id' : $id, 'pw' : $pw"
//    }
)

//    @SerializedName("userPw")
//    @Expose
//    val userPw: String

    // @SerializedName으로 일치시켜 주지않을 경우엔 클래스 변수명이 일치해야함
    // @SerializedName()로 변수명을 입치시켜주면 클래스 변수명이 달라도 알아서 매핑

