package com.example.wintopia.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MyPageInfo {
    @SerializedName("user_farmname")
    @Expose
    private val user_farmname: String? = null

    @SerializedName("user_phone")
    @Expose
    private val user_phone :String? = null

    @SerializedName("totalCow")
    @Expose
    private val totalCow: Int? = null

    @SerializedName("babyCowCount")
    @Expose
    private val babyCowCount: Int? = null

    @SerializedName("cow")
    @Expose
    private val cow: Int? = null

    @SerializedName("bull")
    @Expose
    private val bull: Int? = null



    public fun getFarmName(): String? {
        return user_farmname
    }

    fun getPhone(): String? {
        return user_phone
    }

    fun getTotalCow(): Int? {
        return totalCow
    }

    fun getBaby(): Int? {
        return babyCowCount
    }

    fun getCow(): Int? {
        return cow
    }

    fun getBull(): Int? {
        return bull
    }



}