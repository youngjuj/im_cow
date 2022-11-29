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
    const val BASE_URL: String = "http://192.168.100.126:11112/"

    const val GIT_URL: String = "https://api.github.com/"

    const val LOGIN : String = "loginstr/login"

    const val INFOOUT : String = "info/infoIn"

    const val INFOIN : String = "info/infoOut"

    const val PHOTOINFOOUT : String = "image/upImages"

    const val COWIMG : String = "images/getImages"


}