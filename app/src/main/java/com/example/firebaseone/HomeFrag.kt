package com.example.firebaseone

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.firebaseone.databinding.FragmentHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class HomeFrag : Fragment() {

    private lateinit var bind: FragmentHomeBinding
    private lateinit var storageRef: StorageReference

    private lateinit var RealtimedbRef:DatabaseReference
    private lateinit var storageRefPdf: StorageReference

    private lateinit var fbfirestore: FirebaseFirestore

    private lateinit var fbAuth: FirebaseAuth
    private lateinit var signINCliient: GoogleSignInClient


    private var imageUri: Uri?=null
    private var pdfUri: Uri? = null

    private val resultLauncherImg=registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri=it
        bind.addImage.setImageURI(it)
    }

    private val launcherPdf=registerForActivityResult(ActivityResultContracts.GetContent()){uri->
        pdfUri=uri
        val filename=uri?.let {
            DocumentFile.fromSingleUri(requireContext(),it)?.name}
            bind.fileName.text=filename.toString()

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
        registerClickImage()

        initvar()
        registerPdf()




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







    private fun initvar(){
        storageRefPdf=FirebaseStorage.getInstance().reference.child("pdf")
        RealtimedbRef=FirebaseDatabase.getInstance().reference.child("pdf")

    }

    private fun registerPdf(){
        bind.addPdf.setOnClickListener {
            launcherPdf.launch("application/pdf")

        }

        bind.uploadPdf.setOnClickListener {
            if (pdfUri!=null){
                uploadFile()
            }
            else{
                Toast.makeText(context,"Please select pdf first",Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun uploadFile(){
        val fileName=bind.fileName.text.toString()
        val mstorageRef=storageRefPdf.child("${System.currentTimeMillis()}/${fileName}")
        pdfUri.let {uri ->
            uri?.let { mstorageRef.putFile(it).addOnSuccessListener {

                mstorageRef.downloadUrl.addOnSuccessListener {
                    downloadUri->

                    val pdfFile=PdfFile(fileName,downloadUri.toString())
                    RealtimedbRef.push().key?.let {
                        pushkey->

                        RealtimedbRef.child(pushkey).setValue(pdfFile).addOnSuccessListener {


                            pdfUri=null
                            bind.fileName.text=resources.getString(R.string.no_file_is_selected_yet)

                            Toast.makeText(context,"Uploaded pdf successfully",Toast.LENGTH_SHORT).show()

                            if (bind.progressBar.isShown)
                                bind.progressBar.visibility=View.GONE
                        }
                            .addOnFailureListener{
                                Toast.makeText(context,it.message.toString(),Toast.LENGTH_SHORT).show()

                                if (bind.progressBar.isShown)
                                    bind.progressBar.visibility=View.GONE

                            }

                    }
                }
            }
                .addOnProgressListener {
                    uploadTask->

                    val uploadingPercent=uploadTask.bytesTransferred*100/uploadTask.totalByteCount
                    bind.progressBar.progress=uploadingPercent.toInt()
                    if (!bind.progressBar.isShown)
                        bind.progressBar.visibility=View.VISIBLE

                }
                .addOnFailureListener{
                    if (bind.progressBar.isShown)
                        bind.progressBar.visibility=View.GONE
            }

            }
        }

    }

    private fun initVars() {

        storageRef = FirebaseStorage.getInstance().reference.child("Images")
        fbfirestore=FirebaseFirestore.getInstance()
    }



    private fun registerClickImage() {
        bind.upload.setOnClickListener {
            uploadImage()

        }

        bind.show.setOnClickListener {
            findNavController().navigate(R.id.action_homeFrag_to_contentFrag)

        }


        bind.addImage.setOnClickListener {
            resultLauncherImg.launch("image/*")
        }
    }

    private fun uploadImage(){

        storageRef=storageRef.child(System.currentTimeMillis().toString())
        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener {task->

                if (task.isSuccessful){
                    storageRef.downloadUrl.addOnSuccessListener { uri->

                        val maps=HashMap<String,Any>()
                        maps["pic"]=uri.toString()

                        fbfirestore.collection("images").add(maps).addOnCompleteListener { firestoreTask->



                            if (firestoreTask.isSuccessful){
                                Toast.makeText(context,"Uploaded Successfully",Toast.LENGTH_SHORT).show()
                            }
                            else{
                                Toast.makeText(context,task.exception?.message,Toast.LENGTH_SHORT).show()
                            }
                            bind.addImage.setImageResource(R.drawable.image)
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