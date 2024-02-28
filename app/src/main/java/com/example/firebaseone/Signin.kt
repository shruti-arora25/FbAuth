package com.example.firebaseone

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseone.databinding.ActivitySigninBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

const val REQUEST_CODE = 0


class Signin : AppCompatActivity() {

    private lateinit var bind: ActivitySigninBinding
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var signInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(bind.root)

        fbAuth = FirebaseAuth.getInstance()

        bind.googleActivity.setOnClickListener {


            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build()
            signInClient = GoogleSignIn.getClient(this, gso)
            signInClient.signInIntent.also {
                startActivityForResult(it, REQUEST_CODE)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account.let {
                updateUI(account)
            }

        }
    }


    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        fbAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val i = Intent(this, MainActivity2::class.java)
                startActivity(i)
                finish()

            } else {

                Toast.makeText(this, "Cant login", Toast.LENGTH_SHORT).show()
            }
        }

    }
}