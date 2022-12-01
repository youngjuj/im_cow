package com.example.wintopia.view.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.wintopia.R
import com.example.wintopia.databinding.RegistItemBinding
import java.lang.ref.WeakReference

//
//data class RegistVO {
//    val uri: Uri
//}

class RegistAdapter(private val items:ArrayList<Uri>) :
    RecyclerView.Adapter<RegistAdapter.ViewHolder>() {

    private  var data = mutableListOf<Uri>()
    class ViewHolder(val binding: RegistItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private var view = WeakReference(binding)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.regist_item, parent, false)
        return ViewHolder(RegistItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var img = data[position]

        holder.binding.imgRegistItem.setImageURI(img)
    }

    override fun getItemCount(): Int = items.size

}