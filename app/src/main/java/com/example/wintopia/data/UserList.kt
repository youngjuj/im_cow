package com.example.wintopia.data

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName



class UserList {
    @SerializedName("id")
    @Expose
    private val id :String? = null

    @SerializedName("pw")
    @Expose
    private val pw: String? = null

    fun geId(): String? {
        return id
    }

    fun getPw(): String? {
        return pw
    }

}