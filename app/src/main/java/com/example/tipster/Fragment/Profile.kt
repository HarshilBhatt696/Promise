package com.example.tipster.Fragment


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
//import com.example.tipster.Glide.GlideApp


import com.example.tipster.MainActivity

import com.example.tipster.R
import com.example.tipster.Util.FireStoreUtil
import com.example.tipster.Util.StorageUtil
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.view.*


import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream

//import com.example.tipster.FireMessageGlideModule




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Profile : androidx.fragment.app.Fragment() {


    private val RC_SELECT_IMAGE = 2
    private lateinit var selectedImageBytes: ByteArray
    private var pictureJustChanged = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?) : View? {



        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.apply {

            if (FirebaseAuth.getInstance().currentUser == null) {
                startActivity(Intent(this.context , MainActivity::class.java))
            }

            imageView.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
            }

            Confirm.setOnClickListener {
                if (::selectedImageBytes.isInitialized) {
                    StorageUtil.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                      // FireStoreUtil.updateCurrentUser(editText2.text.toString() , imagePath)
                        // TODO : UNCOMMENT THIS AND REMOVE WHATS BELOW

                        FireStoreUtil.UpdateUser(Bio.text.toString() , imagePath)

                    }
                } else {
                    FireStoreUtil.UpdateUser(BasicName.text.toString() , null) // TODO : WHEN PROFILE LOADED FILL IT WITH BIO ALREADY THERE CHECK VIDEO PROFILE EXAMPLE
                }



            }
            SignOut.setOnClickListener {



                AuthUI.getInstance()
                    .signOut(this@Profile.context!!)
                startActivity(Intent(this.context , MainActivity::class.java))




            }







            return view
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null) {
            val selectedImagePath = data.data
            val selectedImageBmp = MediaStore.Images.Media
                .getBitmap(activity?.contentResolver, selectedImagePath)


            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            selectedImageBytes = outputStream.toByteArray()



//            Glide.with(this)
//                .load(selectedImageBytes)
//                 .into(imageView)


            pictureJustChanged = true

        }
    }

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this.context , MainActivity::class.java))
        }


        FireStoreUtil.getCurrentUser { user ->
            if (this@Profile.isVisible) {
                Bio.setText(user.gender)
                BasicName.setText(user.name)




                if (user.profilePicturePath != null) {
//                    GlideApp.with(this)
//                        .load(StorageUtil.pathToReference(user.profilePicturePath))
//                        .placeholder(R.drawable.ic_alarm_add_black_24dp)
//                        .into(imageView)

//                    Glide.with(this)
//                        .load(StorageUtil.pathToReference(user.profilePicturePath))
//                     //   .apply(RequestOptions().placeholder(R.drawable.ic_alarm_add_black_24dp))
//                        .into(imageView)

                }







            }

        }


        }
    }









