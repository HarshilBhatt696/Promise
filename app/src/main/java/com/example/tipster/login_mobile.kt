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
import com.example.tipster.Service.MyFirebaseInstanceIDService
import com.example.tipster.Util.FireStoreUtil
import com.example.tipster.Util.New
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse

import com.google.android.gms.common.SignInButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.*
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import kotlinx.android.synthetic.main.log_in_mobile_number.*

import kotlinx.android.synthetic.main.names_taken.view.*
import java.lang.reflect.Array
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class login_mobile : AppCompatActivity() {

    lateinit var Providere : List<AuthUI.IdpConfig>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in_mobile_number)

        FirebaseApp.initializeApp(this)



        Providere = Arrays.asList<AuthUI.IdpConfig>(



            AuthUI.IdpConfig.GoogleBuilder().build() ,
            AuthUI.IdpConfig.PhoneBuilder().build()





        )

        button2.setOnClickListener {
            SignInMethod()
        }









    }

    private fun SignInMethod() {
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Providere)
                .setTheme(R.style.AppTheme)
                .build() , 7117

        )
    }

    fun AssignUserName() {
        val UsersDisplayName = FireabaseClass.UserName
        val user = FirebaseAuth.getInstance().currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(UsersDisplayName.toString()).build()
        user?.updateProfile(profileUpdates)

        FireStoreUtil.initCurrentUserIfFirstTime { // TODO add register token


            val registrationToken = FirebaseInstanceId.getInstance().token!!
            MyFirebaseInstanceIDService.addTokenToFirestore(registrationToken)





                    val intent = Intent(this, UserInfor::class.java)
                    startActivity(intent)

            }


        }






    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 7117) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // FROM HERE WE START


                // Toast.makeText(this , User!!.email , Toast.LENGTH_SHORT).show()

                AssignUserName() //
                FireabaseClass.AllotUser() // USER Alloted to real time data


                        val intent = Intent(this, UserInfor::class.java)
                        startActivity(intent)







            } else if (resultCode == Activity.RESULT_CANCELED) {
                    if (response == null) return

                    when (response.error?.errorCode) {
                        ErrorCodes.NO_NETWORK ->
                            Toast.makeText(this , "Error : Connect to a Wifi" , Toast.LENGTH_SHORT).show()
                        ErrorCodes.UNKNOWN_ERROR ->
                            Toast.makeText(this , "Error :Uknown Error" , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }


