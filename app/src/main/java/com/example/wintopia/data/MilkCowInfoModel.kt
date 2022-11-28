package com.example.wintopia.view.edit

import android.telecom.Call
import com.example.wintopia.view.utilssd.API_.INFOOUT
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.Serializable

data class MilkCowInfoModel(
    @SerializedName("name") var name: String,
    @SerializedName("id") var id: String,
    @SerializedName("birth") var birth: String,
    @SerializedName("gender") var gender: String,
    @SerializedName("vaccine") var vaccine: String,
    @SerializedName("kind") var kind: String
)

data class CowInfo(
    @SerializedName("name") var name: String,
    @SerializedName("id") var id: String,
    @SerializedName("birth") var birth: String,
    @SerializedName("gender") var gender: String,
    @SerializedName("vaccine") var vaccine: String,
    @SerializedName("kind") var kind: String
): Serializable

