package com.example.tipster

import android.app.Activity
import android.content.Intent
import android.content.RestrictionEntry
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RestrictTo
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipster.Flide.GlideApp
import com.example.tipster.Model.ImageMessage
import com.example.tipster.Model.MessageType
import com.example.tipster.Model.TextMessage
import com.example.tipster.RecyclerView.Item.PersonClass
import com.example.tipster.Util.FireStoreUtil
import com.example.tipster.Util.FireStoreUtil.getOrCreateChatChannel
import com.example.tipster.Util.StorageUtil
import com.firebase.ui.auth.data.model.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.date_info.view.*
import kotlinx.android.synthetic.main.date_info.view.Cancel
import kotlinx.android.synthetic.main.date_info.view.UserDescribe
import kotlinx.android.synthetic.main.date_info.view.UserImage
import kotlinx.android.synthetic.main.date_info.view.UserNames
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.profile_acitivity.view.*
import java.io.ByteArrayOutputStream
import java.util.*

private const val RC_SELECT_IMAGE = 2
class ChatActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    private lateinit var currentChannelId: String
    private lateinit var currentUser: com.example.tipster.Model.User
    private lateinit var otherUserId: String

    private lateinit var messagesListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = USER_NAME

        FireStoreUtil.getCurrentUser {
            currentUser = it
        }

        otherUserId = (USER_ID)


        getOrCreateChatChannel(otherUserId) {channelId ->
            currentChannelId = channelId

            messagesListenerRegistration =
                FireStoreUtil.addChatMessagesListener(channelId, this, this::updateRecyclerView)


            SendItems.setOnClickListener {
                val messageToSends= TextMessage(EditTextMessages.text.toString(), Calendar.getInstance().time , FirebaseAuth.getInstance().currentUser!!.displayName!! , otherUserId, currentUser.name
                )
                EditTextMessages.setText("")
                FireStoreUtil.sendMessage(messageToSends , channelId)
            }

            Image_Clicker.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
            }

        }





    }

    private fun updateRecyclerView(message: List<Item>) {


        fun init() {
            recyclerForMessages.apply {
                layoutManager = LinearLayoutManager(this@ChatActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messagesSection = Section(message)
                    this.add(messagesSection)
                }
            }

        }
        fun update() = messagesSection.update(message)

        if (shouldInitRecyclerView) {
            init()
        }
        else {
            update()
        }

        recyclerForMessages.scrollToPosition(recyclerForMessages.adapter!!.itemCount - 1)



    }


    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_activity, menu)



        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.Grids -> {

            }
            R.id.Each_user -> {
                SetBottomProfileActivty()

            }
        }


        return super.onOptionsItemSelected(item)
    }


    fun SetBottomProfileActivty() {
        val Contenty = BottomSheetDialog(this)
        val Dialog = LayoutInflater.from(this).inflate(R.layout.profile_acitivity , null)
        Contenty.setContentView(Dialog)

        Contenty.show()

        Dialog.Cancel.setOnClickListener {
            Contenty.dismiss()
        }

        val Values = itemValue as PersonClass
        Dialog.UserNames.text = Values.Person.name
        Dialog.UserDescribe.text = Values.Person.Bio

        if (Values.Person.profilePicturePath != null) {
            GlideApp.with(this)
                .load(StorageUtil.pathToReference(Values.Person.profilePicturePath))
                .placeholder(R.drawable.rect_oval_white)
                .into(Dialog.UserImage)
        }

        Dialog.Block.setOnClickListener {
            FireStoreUtil.getCurrentUser { user ->
                FireStoreUtil.Block(Values.Person.name , user.name , Values.Person.NotToBeViwedAnywhere , user.declineOrBlocked , user.numberOfPeopleDating , Values.Person.numberOfPeopleDating)
                Contenty.dismiss()
                val intent = Intent(this, Messaging::class.java)
                startActivity(intent)
            }

        }





    }


    // Upload image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null) {
            val selectedImagePath = data.data

            val selectedImageBmp = MediaStore.Images.Media.getBitmap(contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()

            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            val selectedImageBytes = outputStream.toByteArray()

            StorageUtil.uploadMessageImage(selectedImageBytes) { imagePath ->
                val messageToSend =
                    ImageMessage(imagePath, Calendar.getInstance().time,
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        otherUserId, currentUser.name)
                FireStoreUtil.sendMessage(messageToSend, currentChannelId)
            }
        }
    }




}
