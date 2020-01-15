@file:Suppress("Annotator")

package com.example.tipster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.date_info.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class DateInfo : AppCompatActivity() {


    val UserInformationFrom = "99B" // Rememeber to change this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.date_info)



    }
}
