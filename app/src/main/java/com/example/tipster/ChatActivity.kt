package com.example.tipster

import android.content.RestrictionEntry
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RestrictTo
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipster.Model.MessageType
import com.example.tipster.Model.TextMessage
import com.example.tipster.Util.FireStoreUtil
import com.example.tipster.Util.FireStoreUtil.getOrCreateChatChannel
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

class ChatActivity : AppCompatActivity() {

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




}
