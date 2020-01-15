package com.example.tipster.Model


import android.os.Message
import java.util.*


data class ImageMessage(val imagePath: String,
                        override val time: Date,
                        override val senderId: String,
                        override val recipientId: String,
                        override val senderName: String,
                        override val type: String = MessageType.IMAGE)
    : Mesage {
    constructor() : this("", Date(0), "", "", "")
}