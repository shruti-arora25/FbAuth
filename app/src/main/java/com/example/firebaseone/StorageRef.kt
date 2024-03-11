package com.example.firebaseone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebaseone.databinding.ActivityStorageRefBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class StorageRef : AppCompatActivity() {

    private lateinit var bind: ActivityStorageRefBinding
    private lateinit var fbStore: FirebaseFirestore
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityStorageRefBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initVars()
        registerClick()


    }

    private fun registerClick() {

        bind.upload.setOnClickListener {

        }

        bind.show.setOnClickListener {

        }

    }


    private fun initVars() {
        storageRef = FirebaseStorage.getInstance().reference.child("Images")

    }
}