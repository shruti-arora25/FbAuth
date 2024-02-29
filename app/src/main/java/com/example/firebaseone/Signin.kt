package com.example.firebaseone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseone.databinding.ActivitySigninBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

const val REQUEST_CODE = 0


class Signin : AppCompatActivity() {


    private lateinit var bind: ActivitySigninBinding
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var signInClient: GoogleSignInClient

    private val db = Firebase.firestore

    private val Address = "email"
    private val Credential = "password"
    private val time="Time"


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

        bind.SIGNIN.setOnClickListener {
            save()

        }


        bind.googleActivity.setOnClickListener {
            signInWithGoogle()

        }
    }

    private fun signInWithGoogle() {
        Log.d("TAG---->", "signInIntent")
        val si: Intent = signInClient.signInIntent
        startActivityForResult(si, REQUEST_CODE)

    }

    @Deprecated("Deprecated in Java")

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {

            Log.d("TAG", "log")
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.d("TAG---->", "TASK $task")
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
                Log.d("TAG---->", "account $account")
            }
        } catch (e: ApiException) {
            e.printStackTrace()

            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        fbAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("TAG---->", "fbAuth $it")

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
           // Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show()


        } else {

            Toast.makeText(this, "USER Error Occurred", Toast.LENGTH_SHORT).show()

        }
    }
}