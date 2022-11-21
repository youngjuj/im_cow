package com.example.wintopia.view.list

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.wintopia.R
import com.example.wintopia.databinding.ListItemBinding
import com.example.wintopia.view.info.InfoActivity

// List에 뿌려줄 item 구성 정보들
data class ListVO (val pic: String = "", // 이미지 url 주소
                   val name: String = "", // 젖소 이름
                   val id: String = "") // 젖소 고유번호

// RecyclerView 사용에 필수인 Adapter
class ListVOAdapter(private val data:MutableList<ListVO>):
    RecyclerView.Adapter<ListVOAdapter.ListVOViewHolder>(), ItemTouchHelperListener{

    // RecyclerView ViewHolder
    class ListVOViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListVOViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListVOViewHolder(ListItemBinding.bind(view))
    }

    // RecyclerView에 뿌려줄 item 속 data들 지정
    override fun onBindViewHolder(holder: ListVOViewHolder, position: Int) {
        holder.binding.wvItemImg.loadUrl(data[position].pic)
        holder.binding.tvItemName.text = data[position].name
        holder.binding.tvItemId.text = "고유번호 : ${data[position].id}"

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

        // item 옆으로 드래그시 수정 또는 삭제 보여주기


    }

    override fun getItemCount(): Int = data.size
    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean = false

    override fun onItemSwipe(position: Int) {
    }

    override fun onLeftClick(position: Int, viewHolder: RecyclerView.ViewHolder?) {
        Toast.makeText(viewHolder?.itemView?.context, "leftClick", Toast.LENGTH_SHORT).show()
    }

    override fun onRightClick(position: Int, viewHolder: RecyclerView.ViewHolder?) {
        Toast.makeText(viewHolder?.itemView?.context, "rightClick", Toast.LENGTH_SHORT).show()
    }


}
