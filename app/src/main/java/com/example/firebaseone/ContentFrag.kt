package com.example.firebaseone

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseone.databinding.FragmentContentBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class ContentFrag : Fragment() {

    private lateinit var bind: FragmentContentBinding
    private lateinit var fbfireStore: FirebaseFirestore
    private var list = mutableListOf<String>()
    private lateinit var adapter: adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentContentBinding.inflate(layoutInflater, container, false)

        initVars()
        getImages()

        return bind.root
    }


    private fun initVars() {

        fbfireStore = FirebaseFirestore.getInstance()
        bind.recyclerView.setHasFixedSize(true)
        bind.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = adapter(list)
        bind.recyclerView.adapter = adapter

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getImages() {

        fbfireStore.collection("images").get().addOnSuccessListener {
            for (i in it) {
                list.add(i.data["pic"].toString())

            }
            adapter.notifyDataSetChanged()
        }


    }


}