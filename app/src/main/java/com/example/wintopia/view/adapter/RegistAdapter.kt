package com.example.wintopia.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wintopia.R
import com.example.wintopia.databinding.RegistItemBinding
import java.lang.ref.WeakReference


class RegistAdapter(private val items: List<Uri?>) :
    RecyclerView.Adapter<RegistAdapter.ViewHolder>() {

    class ViewHolder(val binding: RegistItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private var view = WeakReference(binding)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.regist_item, parent, false)
        return ViewHolder(RegistItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var img = items[position]

//        holder.binding.imgRegistItem.imageMatrix.
        holder.binding.imgRegistItem.setImageURI(img)
    }

    override fun getItemCount(): Int = items.size

//    fun reload(imgdata: MutableList<Uri?>) {
//        this.data.clear()
//        this.data.addAll(imgdata)
//        notifyDataSetChanged()
//    }
}


