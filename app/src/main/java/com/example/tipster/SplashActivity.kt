package com.example.tipster

import android.content.Intent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.splash_activity.*


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {

            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }
        loadingScreen()
    }

    fun loadingScreen() {
        if (Splash.isAnimating == false) {
            if (FirebaseAuth.getInstance().currentUser == null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {

                val intent = Intent(this, MainPage::class.java)
                startActivity(intent)
            }
        } else {
            loadingScreen()
        }
    }
}