package com.example.firebaseone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseone.databinding.ActivitySigninBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date



class Signin : AppCompatActivity() {


    private lateinit var bind: ActivitySigninBinding
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var signInClient: GoogleSignInClient



    private val db = Firebase.firestore

    private val Address = "email"
    private val Credential = "password"
    private val time="Time"

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            //in getSignedInAccount passes the parameter of data of type intent

            val signInCred = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            //type task<GOOGLE SignIn ACC>
                updateUI(signInCred.result)    //using .result will only contain account

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(bind.root)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))

            .requestEmail()
            .build()

        signInClient = GoogleSignIn.getClient(this, gso)

        fbAuth = FirebaseAuth.getInstance()

        bind.googleActivity.setOnClickListener {

            signInWithGoogle()
        }


        bind.SIGNIN.setOnClickListener {
            save()
        }
    }


    private fun signInWithGoogle() {

            val intent = signInClient.signInIntent
            startForResult.launch(intent)


    }
    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        fbAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {


//                fbAuth.currentUser

                val i = Intent(this, MainActivity2::class.java)
                startActivity(i)


            } else {
                Toast.makeText(this, "Cant login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun save() {
        val email = bind.EMAIL.text.toString().trim()
        val pw = bind.PASSWORD.text.toString().trim()
        val timestamp: Long = Date().getTime()

        val map = hashMapOf(
            Address to email,
            Credential to pw,
            time to timestamp

        )
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            db.collection("USER").document(userId).set(map).addOnSuccessListener {


                Toast.makeText(this, "Successfullly saved", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, MainActivity2::class.java))

            }
                .addOnFailureListener { e ->
                   // Log.e("FirestoreError", "Error occurred: ${e.message}", e)
                    Toast.makeText(this, "Error Occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }


        } else {

            Toast.makeText(this, "USER Error Occurred", Toast.LENGTH_SHORT).show()

        }
    }
}