package com.example.firebaseone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseone.databinding.EachItemBinding
import com.squareup.picasso.Picasso

class adapter(private var list: List<String>) : RecyclerView.Adapter<adapter.myVh>() {

    private lateinit var bind: EachItemBinding


    class myVh(var bind: EachItemBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapter.myVh {
        bind = EachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return myVh(bind)
    }

    override fun onBindViewHolder(holder: adapter.myVh, position: Int) {
        with(holder.bind) {
            with(list[position]) {
                Picasso.get().load(this).into(imageView)


            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}