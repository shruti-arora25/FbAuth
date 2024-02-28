package com.example.firebaseone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.firebaseone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navC: NavController
    private lateinit var bind: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.signInButton.setOnClickListener {
            val i=Intent(this,Signin::class.java)
            startActivity(i)
        }



        supportActionBar
    }


    override fun onSupportNavigateUp(): Boolean {
        navC = findNavController(R.id.fragmentContainerView)
        return navC.navigateUp() || super.onSupportNavigateUp()


    }
}