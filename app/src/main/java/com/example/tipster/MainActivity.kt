package com.example.tipster

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.example.tipster.Util.FireStoreUtil
import com.example.tipster.Util.New
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.names_taken.view.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val Code = References.Codes.toString()

        FirebaseApp.initializeApp(this) // Important
        val Database = FirebaseDatabaseClass()

        MyUserAssigned = false

        Database.myRef.child(References.Codes.toString())


        EnterToLogin.setOnClickListener {



            val UsernameGiven = TextUserName.text.toString()
            val CodeGiven = TextCode.text.toString()




            Database.myRef.child(Code).addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again


                    if (dataSnapshot.hasChild(UsernameGiven)) {
                        if (dataSnapshot.child(UsernameGiven).value == CodeGiven) {
                            Callnow()
                            FireabaseClass.UserName = UsernameGiven
                            val Gender =  CheckGender(TextCode.text.toString())
                            RegisterDetails(Gender,UsernameGiven)
                        }
                    }



                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value

                }

            })


        }







    }




    fun Callnow() {
        val Gender =  CheckGender(TextCode.text.toString())
        RegisterDetails(Gender, TextCode.text.toString())




            val intent = Intent(this, login_mobile::class.java)
            startActivity(intent)


    }

    val Male = true
    val Female = false



    fun RegisterDetails(Gender : Boolean , Username : String ) {







        // Set Username
        FireabaseClass.UserName = Username


        if (Gender == Male) {

            FireabaseClass.Gender = References.Males.toString()

        } else {

            FireabaseClass.Gender = References.Females.toString()
        }





    }



    fun CheckGender(CodeEntered : String) : Boolean {

        val Code = CodeEntered[CodeEntered.lastIndex]

        if (Code == 'B') {
            return Male
        } else {
            return Female
        }








    }
}







var popUped = true
class ProvideWarnings() {

    fun GiveWarnings(cont : Context  , Message : String) {




        val Dialog = LayoutInflater.from(cont).inflate(R.layout.names_taken , null)
        val self = LayoutInflater.from(cont).inflate(R.layout.names_taken , null) as ViewGroup
        val popupWindow = PopupWindow(
            Dialog, // Custom view to show in popup window
            LinearLayout.LayoutParams.MATCH_PARENT, // Width of popup window
            LinearLayout.LayoutParams.WRAP_CONTENT

        )
        Dialog.textView5.text = Message


        val slideIn = Slide()
        slideIn.slideEdge = Gravity.TOP
        popupWindow.enterTransition = slideIn

        // Slide animation for popup window exit transition
        val slideOut = Slide()
        slideOut.slideEdge = Gravity.RIGHT
        popupWindow.exitTransition = slideOut

        if (popUped == false) {
            TransitionManager.beginDelayedTransition(self)
            popupWindow.showAtLocation(
                self, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )
            popUped = true
        }


        Dialog.Ok.setOnClickListener {
            popUped = false
            popupWindow.dismiss()
        }}
}
