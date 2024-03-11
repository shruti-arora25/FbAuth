package com.example.firebaseone

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.firebaseone.databinding.FragmentHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class HomeFrag : Fragment() {

    private lateinit var bind: FragmentHomeBinding
    private lateinit var storageRef: StorageReference
    private lateinit var fbfirestore: FirebaseFirestore

    private lateinit var fbAuth: FirebaseAuth
    private lateinit var signINCliient: GoogleSignInClient


    private var imageUri: Uri?=null

    private val resultLauncher=registerForActivityResult(ActivityResultContracts.GetContent()){

        imageUri=it
        bind.addImage.setImageURI(it)


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        bind = FragmentHomeBinding.inflate(layoutInflater, container, false)

        initVars()
        registerClickEvent()



        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail().build()


        signINCliient = GoogleSignIn.getClient(requireActivity(), gso)
        fbAuth = FirebaseAuth.getInstance()

        bind.SIGNOUTT.setOnClickListener {
            signINCliient.signOut().addOnCompleteListener {
                Toast.makeText(requireActivity(), "Sign Out", Toast.LENGTH_SHORT).show()
                signOutFunc()

            }

        }
        return bind.root
    }


    private fun signOutFunc() {
        fbAuth.signOut()
        findNavController().navigate(
            R.id.action_homeFrag_to_signInFrag,
            null,
            NavOptions.Builder().setPopUpTo(R.id.homeFrag, true).build()
        )

    }

    private fun initVars() {

        storageRef = FirebaseStorage.getInstance().reference.child("Images")
        fbfirestore=FirebaseFirestore.getInstance()
    }

    private fun registerClickEvent() {
        bind.upload.setOnClickListener {
            uploadImage()

        }

        bind.show.setOnClickListener {
            findNavController().navigate(R.id.action_homeFrag_to_contentFrag)

        }


        bind.addImage.setOnClickListener {
            resultLauncher.launch("image/*")
        }
    }

    private fun uploadImage(){

        storageRef=storageRef.child(System.currentTimeMillis().toString())
        imageUri?.let { storageRef.putFile(it).addOnCompleteListener {task->

                if (task.isSuccessful){
                    storageRef.downloadUrl.addOnSuccessListener { uri->

                        val maps=HashMap<String,Any>()
                        maps["pic"]=uri.toString()

                        Log.d("TAG------------>","done")
                        fbfirestore.collection("images").add(maps).addOnCompleteListener { firestoreTask->

                            Log.d("TAG------------>","doneFirestore")


                            if (firestoreTask.isSuccessful){
                                Toast.makeText(context,"Uploaded Successfully",Toast.LENGTH_SHORT).show()
                            }
                            else{
                                Toast.makeText(context,task.exception?.message,Toast.LENGTH_SHORT).show()
                            }
                            bind.addImage.setImageResource(R.drawable.addimage)
                        }

                    }

                }
                else{
                    Toast.makeText(context,task.exception?.message,Toast.LENGTH_SHORT).show()
                }

            }

        }

    }


}