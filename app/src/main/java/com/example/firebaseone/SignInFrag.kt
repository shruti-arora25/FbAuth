package com.example.firebaseone

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.firebaseone.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth


class SignInFrag : Fragment() {
    private lateinit var bind:FragmentSignInBinding
    private lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind= FragmentSignInBinding.inflate(inflater,container,false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fbAuth=FirebaseAuth.getInstance()

        bind.signIn.setOnClickListener {

            val email = bind.enailLogin.text.toString()
            val password = bind.passwordLogin.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        findNavController().navigate(R.id.action_signInFrag_to_homeFrag,null,NavOptions.Builder().setPopUpTo(R.id.signInFrag,true).build())

                    } else {
                        Toast.makeText(context, "Incorrect Email id or password", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(context, "fields cant be empty", Toast.LENGTH_SHORT).show()
            }
        }


        bind.createAccount.setOnClickListener {
            findNavController().navigate(R.id.action_signInFrag_to_signUpFrag,null,NavOptions.Builder().setPopUpTo(R.id.signUpFrag,true).build())
        }

        bind.ForgotPass.setOnClickListener {
            val builder= context?.let { it1 -> AlertDialog.Builder(it1) }
            val view=layoutInflater.inflate(R.layout.dialogbox,null)
            val userEmail=view.findViewById<EditText>(R.id.editTextTextEmailAddress)


            builder?.setView(view)
            val dialog=builder?.create()
            view.findViewById<Button>(R.id.reset).setOnClickListener {
                compareEMail(userEmail)
                dialog?.dismiss()
            }
            view.findViewById<Button>(R.id.cancel).setOnClickListener {
                dialog?.dismiss()
            }

            if (dialog?.window!=null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(1))
            }
            dialog?.show()
        }

    }

    private fun compareEMail(email:EditText){
        if (email.text.toString().isEmpty()){
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }
        fbAuth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener {
            if (it.isSuccessful)
                Toast.makeText(context,"Check Your Mail",Toast.LENGTH_SHORT).show()
        }

    }


    }
