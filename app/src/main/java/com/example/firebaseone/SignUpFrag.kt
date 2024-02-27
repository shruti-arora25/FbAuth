package com.example.firebaseone

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.firebaseone.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth


class SignUpFrag : Fragment() {
    private lateinit var bind: FragmentSignUpBinding
    private lateinit var fbAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bind = FragmentSignUpBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        fbAuth = FirebaseAuth.getInstance()



        bind.register.setOnClickListener {
            val email = bind.emailSignUp.text.toString()
            val password = bind.passwordSignUp.text.toString()
            val cpassword = bind.confrmpasswordSignUp.text.toString()


            if (email.isNotEmpty() && password.isNotEmpty() && cpassword.isNotEmpty()) {
                if (password == cpassword) {

                    fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

                        if (it.isSuccessful) {
                            findNavController().navigate(R.id.action_signUpFrag_to_signInFrag,null,NavOptions.Builder().setPopUpTo(R.id.signUpFrag,true).build())

                        } else {
                            Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }

                    }

                } else {
                    Toast.makeText(context, "Password is not same", Toast.LENGTH_SHORT).show()

                }
            }

            else {
                Toast.makeText(context, "Field cant be empty!", Toast.LENGTH_SHORT).show()
            }

        }

                bind.AlreadyAccLogin.setOnClickListener {
                    findNavController().navigate(R.id.action_signUpFrag_to_signInFrag,null,NavOptions.Builder().setPopUpTo(R.id.signUpFrag,true).build())

                }






            }

        }
