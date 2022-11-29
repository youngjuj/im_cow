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
    @SerializedName("cow_id") var id: String,
    @SerializedName("cow_name") var name: String,
    @SerializedName("cow_birth") var birth: String,
    @SerializedName("cow_variety") var variety: String,
    @SerializedName("cow_gender") var gender: String,
    @SerializedName("cow_vaccination") var vaccine: String,
    @SerializedName("cow_pregnancy") var pregnancy: String,
    @SerializedName("cow_milk") var milk: String,
    @SerializedName("cow_castration") var castration: String,
    @SerializedName("wish_list") var list: Int,
    @SerializedName("user_num") var num: Int,
)

data class CowInfo(
    @SerializedName("cow_id") var id: String,
    @SerializedName("cow_name") var name: String,
    @SerializedName("cow_birth") var birth: String,
    @SerializedName("cow_variety") var variety: String,
    @SerializedName("cow_gender") var gender: String,
    @SerializedName("cow_vaccination") var vaccine: String,
    @SerializedName("cow_pregnancy") var pregnancy: String,
    @SerializedName("cow_milk") var milk: String,
    @SerializedName("cow_castration") var castration: String,
    @SerializedName("wish_list") var list: Int,
    @SerializedName("user_num") var num: Int,
): Serializable

