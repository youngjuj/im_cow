package com.example.wintopia.view.utilssd

import com.example.wintopia.view.edit.MilkCowInfoModel

object Constants {
    const val TAG : String = "로그"
}

enum class RESPONSE_STATE{
    OKAY,
    FAIL
}

object API_ {
    const val BASE_URL: String = "http://192.168.21.52:11112/"

    const val LOGIN : String = "test/login"

    const val INFOOUT : String = "info/infoIn"

}