package com.example.firebaseone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.firebaseone.databinding.FragmentSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SignInFrag : Fragment() {
    private lateinit var bind: FragmentSignInBinding
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var signInClient: GoogleSignInClient

    private val db = Firebase.firestore

    private val Address = "email"
    private val Credential = "password"
    private val time = "Time"


    override fun onAttach(context: Context) { // Or onCreate(savedInstanceState: Bundle?)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentSignInBinding.inflate(inflater, container, false)


        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fbAuth = FirebaseAuth.getInstance()
        Log.d("ko", "Part1")

        val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                    handleResults(task)

                }
            }



        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()


        bind.google.setOnClickListener {

            signInClient = GoogleSignIn.getClient(requireActivity(), gso)
            val signinIntent = signInClient.signInIntent
            launcher.launch(signinIntent) // Use the declared launcher
        }


        //custom authentication
        bind.signIn.setOnClickListener {

            val email = bind.enailLogin.text.toString()
            val password = bind.passwordLogin.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        save()
//                        findNavController().navigate(
//                            R.id.action_signInFrag_to_homeFrag,
//                            null,
//                            NavOptions.Builder().setPopUpTo(R.id.signInFrag, true).build()
//                        )
                    }
                    else {
                        Toast.makeText(
                            context,
                            "Incorrect Email id or password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } else {
                Toast.makeText(context, "fields can't be empty", Toast.LENGTH_SHORT).show()
            }
        }


        bind.createAccount.setOnClickListener {
            findNavController().navigate(
                R.id.action_signInFrag_to_signUpFrag,
                null,
                NavOptions.Builder().setPopUpTo(R.id.signUpFrag, true).build()
            )
        }

        bind.ForgotPass.setOnClickListener {
            val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
            val view = layoutInflater.inflate(R.layout.dialogbox, null)
            val userEmail = view.findViewById<EditText>(R.id.editTextTextEmailAddress)


            builder?.setView(view)
            val dialog = builder?.create()
            view.findViewById<Button>(R.id.reset).setOnClickListener {
                compareEMail(userEmail)
                dialog?.dismiss()
            }
            view.findViewById<Button>(R.id.cancel).setOnClickListener {
                dialog?.dismiss()
            }

            if (dialog?.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(1))
            }
            dialog?.show()
        }

    }


    private fun compareEMail(email: EditText) {
        if (email.text.toString().isEmpty()) {
            Toast.makeText(context, "Enter Email Address correctly", Toast.LENGTH_SHORT).show()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            return
        }
        fbAuth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener {
            if (it.isSuccessful)
                Toast.makeText(context, "Check Your Mail", Toast.LENGTH_SHORT).show()
        }

    }

    private fun signInGoogle() {
        val signinIntent = signInClient.signInIntent
        val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleResults(task)

                }


            }
        launcher.launch(signinIntent)

    }


    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result

            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(context, "try again", Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        fbAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {

                findNavController().navigate(R.id.action_signInFrag_to_homeFrag)

            } else {
                Toast.makeText(context, "Cant login", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun save() {
        val email = bind.enailLogin.text.toString().trim()
        val pw = bind.passwordLogin.text.toString().trim()
        val timestamp: Long = Date().getTime()

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDate = sdf.format(Date(timestamp))

        val map = hashMapOf(
            Address to email,
            Credential to pw,
            time to formattedDate

        )
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            db.collection("USER").document(userId).set(map).addOnSuccessListener {


                Toast.makeText(requireActivity(), "Successfullly saved", Toast.LENGTH_SHORT).show()

                findNavController().navigate(
                    R.id.action_signInFrag_to_homeFrag,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.signUpFrag, true).build()
                )

            }
                .addOnFailureListener { e ->
                    // Log.e("FirestoreError", "Error occurred: ${e.message}", e)
                    Toast.makeText(
                        requireActivity(),
                        "Error Occurred: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            // Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show()


        }

    }
}


