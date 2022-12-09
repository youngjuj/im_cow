package com.example.wintopia.retrofit

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


// 싱글턴
object RetrofitClient {

    // 레트로핏 클라이언트(인스턴스) 선언
    private var instance : Retrofit? = null

    //레트로핏 클라이언트 가져오기
    fun getInstnace(baseUrl:String) : Retrofit {
        Log.d("retrofit instance", "Retrofit Client - getInstnace() called")

        val gson : Gson = GsonBuilder()
            .setLenient()
            .create()

        if(instance == null){
            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()

            // 레트로핏 빌더를 통해 인스턴스 생성
            instance = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }


        return instance!!
    }
}

