package com.example.wintopia.view.regist

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wintopia.data.UserList
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistViewModel: ViewModel() {

    val event = MutableLiveData<String>()
    val registEvent = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val id = MutableLiveData<String>()
    val birth = MutableLiveData<String>()
    val variety = MutableLiveData<String>()
    val gender = MutableLiveData<String>()
    val vaccine = MutableLiveData<String>()
    val pregnancy = MutableLiveData<String>()
    val milk = MutableLiveData<String>()
    val castration = MutableLiveData<String>()
    val fax = MutableLiveData<String>()
    val imgFileList = arrayListOf<MultipartBody.Part>()
    val imgList = arrayListOf<Uri?>()

    fun sendImage(cow_id:String, imgFileList: ArrayList<MultipartBody.Part>) {
        Log.d(Constants.TAG,"웹서버로 이미지전송")

        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현


        val call = service.cowImageList(imgFileList.toList()) //통신 API 패스 설정


        call?.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful) {
                    Log.d("로그 ","이미지 전송 :"+response?.body().toString())
                    event.value = "success"
                   // Toast.makeText(applicationContext,"통신성공", Toast.LENGTH_SHORT).show()
                } else {
                    event.value = "failed"
                  //  Toast.makeText(applicationContext,"통신실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.d("로그",t.message.toString())
            }
        })
    }

    // 이미지 처리랑 같이 고민해보기
    // 버튼 이벤트 전에 사진 올렸을 때 자동으로 개체 정보 확인해서
    // 소가 아니면 다시 찍어달라 다이얼로그, 등록 개체는 등록 불가, 미등록 개채는 나머지 정보 입력
    // 소 정보 신규 등록
    fun cowInfoRegist(user_id: String, item: MilkCowInfoModel) {
        Log.d(Constants.TAG,"소 신규 등록 요청")

        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현


        val call = service.cowInfoRegist(user_id, item) //통신 API 패스 설정

        call?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                Log.d(Constants.TAG, "등록가능..${response}")

                if (response.isSuccessful) {
//                    res = response.toString()
                    Log.d("로그 ","소 이미지 불러오기3 :"+ response.body().toString())

                    registEvent.value = "success"
                } else {
                    registEvent.value = "fail1"
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.d("로그",t.message.toString())
                registEvent.value = "fail2"
            }
        })
    }



}