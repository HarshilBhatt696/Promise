package com.example.tipster.Model

import android.app.Notification
import androidx.core.app.NotificationCompat
import java.util.*

data class TextMessage(val text: String,
                       override val time: Date,
                       override val senderId: String,
                       override val recipientId: String,
                       override val senderName: String,
                       override val type: String = MessageType.TEXT)
    : Mesage {
    constructor() : this("", Date(0), "", "", "")
}