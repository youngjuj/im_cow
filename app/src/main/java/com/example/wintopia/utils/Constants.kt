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
//    10.0.2.2
    const val BASE_URL: String = "http://172.31.99.93:11112/"

    const val GIT_URL: String = "https://api.github.com/"

    const val LOGIN : String = "loginstr/login"

    const val COWINFOONE : String = "info/cowInfoOne"

    const val COWINFOALL : String = "info/cowInfoAll"

    const val COWIMGUP : String = "image/cowImgUp"

    const val COWIMGOUT : String = "image/cowImgOut"

    const val COWIMGLIST : String = "image/cowImgList"

}