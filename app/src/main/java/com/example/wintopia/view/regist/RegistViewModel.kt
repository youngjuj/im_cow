package com.example.wintopia.view.regist

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wintopia.data.UserList
import com.example.wintopia.dialog.Custumdialog
import com.example.wintopia.dialog.MyCustomDialog
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.info.InfoActivity
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistViewModel: ViewModel() {

    val event = MutableLiveData<String>()
    val eventCowId = MutableLiveData<String>()
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
    val milkCowInfoModel = MutableLiveData<MilkCowInfoModel>()
    lateinit var cowInfoEvent: MilkCowInfoModel

    fun sendImage(cow_id:String, imgFileList: ArrayList<MultipartBody.Part>) {
        Log.d(Constants.TAG,"웹서버로 이미지전송")

        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현


        val call = service.cowImageList(imgFileList) //통신 API 패스 설정


        call?.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful) {
                    Log.d("로그 ","리스트 전송 :"+response?.body().toString())
                    event.value = "success"
                    eventCowId.value = response?.body().toString()

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
    fun registCowInfo(user_id: String, milkCowInfoModel: MilkCowInfoModel) {
        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현

        val call: Call<String>? = service.cowInfoRegist(user_id, milkCowInfoModel)
        call!!.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>?, response: Response<String?>) {
                Log.d(Constants.TAG, "registCowInfo onResponse")
                if (response.isSuccessful()) {
                    Log.e(Constants.TAG, "registCowInfo onResponse success")
//                        val result: UserList? = response.body()

                    // 서버에서 응답받은 데이터
                    val result = response.body()
                    Log.d(ContentValues.TAG, "$result")
                    registEvent.value = "success"

                } else {
                    // 서버통신 실패
                    registEvent.value = "fail1"
                    Log.e(Constants.TAG, "onResponse fail")
                }
            }
            override fun onFailure(call: Call<String?>?, t: Throwable) {
                // 통신 실패
                registEvent.value = "fail2"
                Log.e(Constants.TAG, "onFailure: " + t.message)
            }
        })
    }


    fun cowInfoOne(cow_id: String){
        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현

        val call: Call<List<MilkCowInfoModel>> = service.getData(cow_id)
        call!!.enqueue(object : Callback<List<MilkCowInfoModel>> {
            override fun onResponse(call: Call<List<MilkCowInfoModel>>, response: Response<List<MilkCowInfoModel>>) {
                Log.d(Constants.TAG, "onResponse")
                if (response.isSuccessful()) {

                    var result = response.body()

                    cowInfoEvent = result as MilkCowInfoModel

                    ///// 수정////////
//                    val intent = Intent(requireActivity(), InfoActivity::class.java)
//                    Log.d("값 확인", "${result.toString()}")
//                    Log.d("뭐야2222", "$result")
//
//                    intent.putExtra("where", "camera")
//                    intent.putExtra("camera", result as MilkCowInfoModel)
//                    startActivity(intent)

                } else {
                    // 서버통신 실패
//                    event.value = "fail1"
                    Log.e(Constants.TAG, "onResponse fail")
                }
            }
            override fun onFailure(call: Call<List<MilkCowInfoModel>>, t: Throwable) {
                // 통신 실패
//                event.value = "fail2"
                Log.e(Constants.TAG, "onFailure: " + t.message)
            }
        })
    }

}