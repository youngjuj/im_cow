package com.example.wintopia.view.retrofit

import android.util.Log
import com.example.wintopia.view.utils.API_.BASE_URL
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 싱글턴
object RetrofitClient {

    // 레트로핏 클라이언트(인스턴스) 선언
    private var instance : Retrofit? = null
//    private lateinit var instance : Retrofit

//    private val gson = GsonBuilder().setLenient().create()

    //레트로핏 클라이언트 가져오기
    fun getInstnace(baseUrl:String) : Retrofit {
        Log.d("retrofit instance", "Retrofit Client - getInstnace() called")

        if(instance == null){
            // 레트로핏 빌더를 통해 인스턴스 생성
            instance = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }
}

