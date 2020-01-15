package com.example.tipster

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
//import android.support.design.widget.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.bumptech.glide.Glide

import com.example.tipster.Fragment.Profile
import com.example.tipster.Fragment.Request_dates
import com.example.tipster.Fragment.match_makin
import com.example.tipster.Model.User
import com.example.tipster.Util.FireStoreUtil
import com.example.tipster.Util.StorageUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.date_info.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.main_page_layout.*


lateinit var MyUser : User
var MyUserAssigned = false

class MainPage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page_layout)



        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        ProvideValues()

        replaceFragment(Profile())

        nav_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_Message -> {
                    replaceFragment(Messaging())
                    true
                }
                R.id.navigation_Profile -> {
                    replaceFragment(Profile())
                    true


                }
                R.id.navigation_MatchMake -> {
                    replaceFragment(match_makin())
                    true
                }
                R.id.navigation_request -> {
                    replaceFragment(Request_dates())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_layout, fragment)
            .commit()
    }

    fun ProvideValues() {

        if (!MyUserAssigned) {
            FireStoreUtil.getCurrentUser { user ->

                MyUser = user
                FireabaseClass.UserName = user.name
                FireabaseClass.Gender = user.gender
                MyUserAssigned = true // Values are assigned

            }


        }

    }


}
