package com.example.firebaseone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firebaseone.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference


class HomeFrag : Fragment() {

    private lateinit var bind:FragmentHomeBinding
    private lateinit var storageRef:StorageReference
    private lateinit var fbfirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bind=FragmentHomeBinding.inflate(layoutInflater,container,false)
        return bind.root
    }



}