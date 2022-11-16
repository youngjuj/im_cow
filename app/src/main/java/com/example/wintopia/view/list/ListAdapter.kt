package com.example.wintopia.view.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wintopia.R
import com.example.wintopia.databinding.ListItemBinding

data class ListVO (val pic: String = "",
                   val name: String = "",
                   val id: String = "")


class ListVOAdapter(private val data:MutableList<ListVO>):
    RecyclerView.Adapter<ListVOAdapter.ListVOViewHolder>(){

    class ListVOViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListVOViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListVOViewHolder(ListItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ListVOViewHolder, position: Int) {
        holder.binding.wvItemImg.loadUrl(data[position].pic)
        holder.binding.tvItemName.text = data[position].name
        holder.binding.tvItemId.text = "고유번호 : ${data[position].id}"
        holder.binding.wvItemImg.settings.useWideViewPort = true
        holder.binding.wvItemImg.settings.loadWithOverviewMode = true
        holder.binding.wvItemImg.settings.builtInZoomControls = true
        holder.binding.wvItemImg.settings.setSupportZoom(true)

    }

    override fun getItemCount(): Int = data.size


}
