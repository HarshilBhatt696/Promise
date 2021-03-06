package com.example.tipster

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tipster.Flide.GlideApp
import com.example.tipster.Util.FireStoreUtil
import com.example.tipster.Util.StorageUtil
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.set_image.*
import java.io.ByteArrayOutputStream

class SetImage : AppCompatActivity() {

    private val RC_SELECT_IMAGE = 2
    private lateinit var selectedImageBytes: ByteArray
    private var pictureJustChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_image)

        SetImage.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
            }
            startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null
        ) {
            val selectedImagePath = data.data
            val selectedImageBmp = MediaStore.Images.Media
                .getBitmap(this.contentResolver, selectedImagePath)


            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            selectedImageBytes = outputStream.toByteArray()






            pictureJustChanged = true


            if (::selectedImageBytes.isInitialized) {
                StorageUtil.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                    FireStoreUtil.UpdateUser("" , imagePath)

                    startActivity(Intent(this , MainPage::class.java))

                }

            }
        } else {
            Toast.makeText(this , "Make Sure to upload a photo" , Toast.LENGTH_SHORT).show()
        }


    }
}