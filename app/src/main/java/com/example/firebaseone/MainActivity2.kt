package com.example.firebaseone

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebaseone.databinding.ActivityMain2Binding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth

class MainActivity2 : AppCompatActivity() {
    private lateinit var bind:ActivityMain2Binding
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var signInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(bind.root)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()


        signInClient = GoogleSignIn.getClient(this, gso)
        fbAuth = FirebaseAuth.getInstance()

        bind.signout.setOnClickListener {
            signInClient.signOut().addOnCompleteListener {
                Toast.makeText(this, "Sign Out", Toast.LENGTH_SHORT).show()
                signOutF()
            }
        }
    }


    private fun signOutF() {
//
        fbAuth.signOut()
        startActivity(Intent(this,Signin::class.java))




    }
}