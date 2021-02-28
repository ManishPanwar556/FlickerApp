package com.example.flickerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flickerapp.databinding.FlickrItemBinding
import com.example.flickerapp.retrofit.Photo

class MyRecyclerAdapter(val list: List<Photo>) :
    RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>() {
    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.flickr_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val view = FlickrItemBinding.bind(holder.itemView)
        Glide.with(holder.view).load(list[position].url_s).into(view.image)
    }

    override fun getItemCount() = list.size


}