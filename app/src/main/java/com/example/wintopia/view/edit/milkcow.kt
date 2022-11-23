package com.example.wintopia.view.edit

import com.google.gson.annotations.SerializedName

data class milkcow(

    @SerializedName("Name") var name: String,
    @SerializedName("id") var id: String,
    @SerializedName("birth") var birth: String,
    @SerializedName("gender") var gender: String,
    @SerializedName("vaccine") var vaccine: String,
    @SerializedName("kind") var kind: String

)
