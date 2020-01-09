package com.example.tipster

import android.os.Bundle
import android.provider.ContactsContract
//import android.support.design.widget.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.bumptech.glide.Glide

import com.example.tipster.Fragment.Profile
import com.example.tipster.Fragment.Request_dates
import com.example.tipster.Fragment.match_makin
import com.example.tipster.Util.FireStoreUtil
import com.example.tipster.Util.StorageUtil
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.main_page_layout.*

class MainPage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page_layout)

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
        FireStoreUtil.getCurrentUser { user ->


                FireabaseClass.UserName = user.name
                FireabaseClass.Gender = user.gender


            }



    }
}
