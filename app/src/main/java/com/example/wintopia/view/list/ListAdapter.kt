package com.example.wintopia.view.list

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wintopia.R
import com.example.wintopia.databinding.ListItemBinding
import com.example.wintopia.view.edit.EditActivity
import com.example.wintopia.view.info.InfoActivity
import java.lang.ref.WeakReference
import java.util.*

// List에 뿌려줄 item 구성 정보들
data class ListVO (
//    val pic: String = "", // 이미지 url 주소
                   val name: String = "", // 젖소 이름
                   val id: String = "",// 젖소 고유번호
                   val birth: String = "",// 젖소 출생일
                   val gender: String = "", // 젖소 성별
                   val vaccine: String = "", // 백신접종 여부
                   val kind: String = "") // 젖소 종류


// RecyclerView 사용에 필수인 Adapter
class ListVOAdapter(private val data:MutableList<ListVO>):
    RecyclerView.Adapter<ListVOAdapter.ListVOViewHolder>(){

    private var listData = mutableListOf<ListVO>()


    // RecyclerView ViewHolder
    class ListVOViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val view = WeakReference(binding)
        private lateinit var clItem: ConstraintLayout
        private lateinit var hiddenBtnEdt: TextView
        private lateinit var hiddenBtnDel: TextView

        var index = 0

        var onDeleteClick:((RecyclerView.ViewHolder) -> Unit)? = null

        init {
            binding.root.let {
                binding.root.setOnClickListener{
                    if(binding.root.scrollX != 0) {
                        binding.root.scrollTo(0, 0)
                    }
                }

                clItem = binding.clItem
                hiddenBtnEdt = binding.hiddenBtnEdt
                hiddenBtnDel = binding.hiddenBtnDel

                hiddenBtnEdt.setOnClickListener {
                    val intent = Intent(binding.root.context, EditActivity::class.java)
                    binding.root.context.startActivity(intent)
                }

                hiddenBtnDel.setOnClickListener {
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
//        holder.binding.wvItemImg.loadUrl(data[position].pic)
        holder.binding.wvItemImg.loadUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSrcg48Fej-S3muJwRGLbtfNcWcHwEKKfcbrA&usqp=CAU")
        holder.binding.tvItemName.text = data[position].name
        holder.binding.tvItemId.text = "고유번호 : ${data[position].id}"

        holder.onDeleteClick = {
            removeItem(it)
        }

        // webView에 띄울 이미지 관련 설정들
        holder.binding.wvItemImg.settings.useWideViewPort = true
        holder.binding.wvItemImg.settings.loadWithOverviewMode = true
        holder.binding.wvItemImg.settings.builtInZoomControls = true
        holder.binding.wvItemImg.settings.setSupportZoom(true)


        // item 선택 onClickListener
        holder.itemView.setOnClickListener {
            // item선택시 InfoActivity로 이동
            val intent = Intent(holder.itemView?.context, InfoActivity::class.java)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }

        holder.updateView()

    }

    fun reload(listdata: List<ListVO>) {
        this.listData.clear()
        this.listData.addAll(listdata)
        notifyDataSetChanged()
    }



    override fun getItemCount(): Int = data.size


    fun setListData(listData: MutableList<ListVO>) {
        this.listData = data
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

}
