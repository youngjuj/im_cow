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
//    const val BASE_URL: String = "http://10.0.2.2:11112/"
    const val BASE_URL: String = "http://intflow.serveftp.com:11112/"

    const val GIT_URL: String = "https://api.github.com/"

    const val LOGIN : String = "login/login"

    const val SIGNUP : String = "login/signUp"

    // 이미지 한장 업로드
    const val COWIMGUP : String = "image/cowImgUp"

    // 이미지 하나 받아오기
    const val COWIMGOUT : String = "image/cowImgOut"

    // 이미지 리스트로 내보내기
    const val COWIMGLIST : String = "image/cowImgList"

    // 소 즐겨찾기 통신
    const val COWWISH : String = "info/cowWish"

    // 마이페이지 정보 받아오기
    const val MYPAGEINFO : String = "info/myPageInfo"

    // 개체 정보 삭제
    const val COWINFODELETE : String = "info/cowInfoDelete"

    // 개체 정보 업데이트
    const val COWINFOUPDATE : String = "info/cowInfoUpdate"

    // 개체 신규등록
    const val COWINFOREGIST : String = "info/cowInfoRegist"

    // 개체 정보 전체 불러오기
    const val COWINFOALL : String = "info/cowInfoAll"

    // 개체 정보 하나 불러오기
    const val COWINFOONE : String = "info/cowInfoOne"

}