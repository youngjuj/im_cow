package com.example.wintopia.data

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class UserList {
    // @SerializedName으로 일치시켜 주지않을 경우엔 클래스 변수명이 일치해야함
    // @SerializedName()로 변수명을 일치시켜주면 클래스 변수명이 달라도 알아서 매핑
    @SerializedName("id")
    @Expose
    private val id :String? = null

    @SerializedName("pw")
    @Expose
    private val pw: String? = null

    fun getId(): String? {
        return id
    }

    fun getPw(): String? {
        return pw
    }

}