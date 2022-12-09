package com.example.wintopia.view.list

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wintopia.R
import com.example.wintopia.data.UserList
import com.example.wintopia.databinding.ListItemBinding
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.utils.SwipeHelperCallback
import com.example.wintopia.view.edit.EditActivity
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.info.InfoActivity
import com.example.wintopia.view.info.InfoViewModel
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference
import java.util.*
// RecyclerView 사용에 필수인 Adapter
class ListVOAdapter(private val data:MutableList<MilkCowInfoModel>):
    RecyclerView.Adapter<ListVOAdapter.ListVOViewHolder>(){

    private var listData = mutableListOf<MilkCowInfoModel>()

    // RecyclerView ViewHolder
    inner class ListVOViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            hiddenBtnEdt.setOnClickListener {

                    val intent = Intent(binding.root.context, EditActivity::class.java)
                    intent.putExtra("cowInfo", data[position])
                    binding.root.context.startActivity(intent)
            }

        }
        private val view = WeakReference(binding)
        private lateinit var clItem: FrameLayout
        private lateinit var hiddenBtnEdt: FrameLayout
        private lateinit var hiddenBtnDel: FrameLayout

        var index = 0

        var onDeleteClick:((RecyclerView.ViewHolder) -> Unit)? = null

        init {
            binding.root.let {
                clItem = binding.clItem
                hiddenBtnEdt = binding.hiddenBtnEdt
                hiddenBtnDel = binding.hiddenBtnDel

                binding.clItem.setOnClickListener{
                    if(binding.root.scrollX != 0) {
                        hiddenBtnDel.isClickable = true
                        hiddenBtnEdt.isClickable = true
                        binding.root.scrollTo(0, 0)
                    }
                }

                hiddenBtnDel.setOnClickListener {
                    InfoViewModel().cowInfoDelete(data[position].id)
                    onDeleteClick?.let{ onDeleteClick ->
                        onDeleteClick(this)
                    }
                }
            }
        }

        fun updateView() {
            binding.root.scrollTo(0, 0)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListVOViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListVOViewHolder(ListItemBinding.bind(view))
    }


    // RecyclerView에 뿌려줄 item 속 data들 지정
    override fun onBindViewHolder(holder: ListVOViewHolder, position: Int) {

        holder.bind(position)
        var cow_id = data[position].id
        var user_id = UserList().getId().toString()
        Glide.with(holder.itemView)
            .load("${API_.BASE_URL}image/cowImgOut?cow_id=$cow_id")
            .override(80, 80)
            .into(holder.binding.imgItemImg)

        holder.binding.tvItemName.text = data[position].name
        holder.binding.tvItemVariety.text = "품종 : ${data[position].variety}"
        holder.binding.tvItemBirth.text = "출생일 : ${data[position].birth}"
        holder.binding.imgItemWish.apply {
            if (data[position].list == 1)
                setImageResource(R.drawable.filledheart) else setImageResource(R.drawable.emptyheart)
        }

        holder.onDeleteClick = {
            removeItem(it)
        }

        // item 선택 onClickListener
        holder.binding.clItem.setOnClickListener {
            putText(holder, position)
        }

        holder.updateView()

    }

    fun reload(listdata: List<MilkCowInfoModel>) {
        this.listData.clear()
        this.listData.addAll(listdata)

        notifyDataSetChanged()
    }



    override fun getItemCount(): Int = data.size


    fun setListData(listData: MutableList<MilkCowInfoModel>) {
        this.listData = listData
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        var position = viewHolder.adapterPosition
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun swapData(fromPosition: Int, toPosition: Int) {
        Collections.swap(listData, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun putText(holder: ListVOViewHolder, position: Int){
        // 각 텍스트 가져오기
        var infoId = data[position].id
        var infoName = data[position].name
        var infoBirth = data[position].birth
        var infoVariety = data[position].variety
        var infoGender = data[position].gender
        var infoVaccine = data[position].vaccine
        var infoPregnancy = data[position].pregnancy
        var infoMilk = data[position].milk
        var infoCastration = data[position].castration
        var infoWish = data[position].list
        var userNum = data[position].num

        Log.v("data확인", data[position].name)

        val milkCowInfoModel = MilkCowInfoModel(infoId,
            infoName,infoBirth,infoVariety,infoGender,infoVaccine, infoPregnancy, infoMilk, infoCastration, infoWish, userNum)



        Log.d(Constants.TAG, " 수정완료 버튼 클릭, ${milkCowInfoModel}")

        val intent = Intent(holder.itemView?.context, InfoActivity::class.java)
        intent.putExtra("where", "list")
        intent.putExtra("infos", milkCowInfoModel)
        ContextCompat.startActivity(holder.itemView.context, intent, null)

    }

    fun cowInfo(userId: String) {
        Log.d(Constants.TAG,"웹서버로 이미지전송")

        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현


        val call = service.cowListAll(userId) //통신 API 패스 설정

        call?.enqueue(object : Callback<MutableList<MilkCowInfoModel>> {
            @SuppressLint("ClickableViewAccessibility")
            override fun onResponse(call: Call<MutableList<MilkCowInfoModel>>, response: Response<MutableList<MilkCowInfoModel>>) {
                if (response.isSuccessful) {
                    Log.d(Constants.TAG, "" + response?.body().toString())
                    val data = response?.body()!!
                    listData = data

                } else {

                }
            }

            override fun onFailure(call: Call<MutableList<MilkCowInfoModel>>, t: Throwable) {
            }
        })
    }

}
