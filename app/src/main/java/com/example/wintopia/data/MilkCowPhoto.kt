package com.example.wintopia.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File

class MilkCowPhoto {
    @SerializedName("id")
    @Expose
    private val id :String? = null

    @SerializedName("photo")
    @Expose
    private val photo :String? = null

    fun getId(): String? {
        return id
    }

    fun getPhoto(): String? {
        return photo
    }

}