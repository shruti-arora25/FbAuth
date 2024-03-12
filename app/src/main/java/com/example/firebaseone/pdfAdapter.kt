package com.example.firebaseone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseone.databinding.EachPdfBinding


class pdfAdapter(private val listener:onClickListen) :
    androidx.recyclerview.widget.ListAdapter<PdfFile, pdfAdapter.myVh>(pdfDiffCallback()) {




    inner class myVh(private val bind: EachPdfBinding) : RecyclerView.ViewHolder(bind.root) {


        init {
            bind.root.setOnClickListener{
                listener.pdfClicked(getItem(adapterPosition))


            }
        }
        fun bind(data: PdfFile) {
            bind.pdfText.text=data.filename


        }


    }


    class pdfDiffCallback : DiffUtil.ItemCallback<PdfFile>() {
        override fun areItemsTheSame(oldItem: PdfFile, newItem: PdfFile): Boolean =
            oldItem.downloadurl == newItem.downloadurl

        override fun areContentsTheSame(oldItem: PdfFile, newItem: PdfFile): Boolean =
            oldItem == newItem
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myVh {



        val bind = EachPdfBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return myVh(bind)

    }

    override fun onBindViewHolder(holder: myVh, position: Int) {
        holder.bind(getItem(position))
    }


    interface onClickListen{

        fun pdfClicked(pdfFile: PdfFile)

    }


}