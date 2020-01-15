package com.example.tipster


import android.R.attr.*
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock



import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.iid.FirebaseInstanceId

import kotlinx.android.synthetic.main.activity_main.*

import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import android.sax.StartElementListener
import androidx.core.app.ActivityCompat
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.widget.*


import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.*
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.names_taken.view.*
import java.lang.reflect.Array
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList



import com.google.firebase.database.FirebaseDatabase
import java.sql.Ref


val FireabaseClass = FirebaseDatabaseClass()
class FirebaseDatabaseClass() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference()
    val Users = arrayListOf<String>()
    var UserName = ""
    var Gender = ""

//    fun AssignDescription() {
//
//        for (n in UserInfo.Interest) {
//
//            myRef.child(References.UserDetail.toString()).child(Gender).child(UserName).child("About").child(n.key).setValue(UserInfo.Interest[n.key])
//
//        }
//
//
//    }

    fun AllotUser() {
     //   myRef.child(References.UserDetail.toString()).child(Gender).child(UserName).setValue(1) // 1 means per device user


    }


    fun AssignInterest() {

        val Interest = Interests.Category
        val InterestGathered = Interests.CategoryGatheredTypes


        for (n in Interest.indices) {

            myRef.child(References.Likes.toString()).child(Interest[n]).child(InterestGathered[n]).child(Gender).child(UserName).setValue("1")

        }




    }

    fun SetMatchMakingOff(MyUserName : String) {

        FireabaseClass.myRef.child(MyUserName).setValue(false)


    }










}



