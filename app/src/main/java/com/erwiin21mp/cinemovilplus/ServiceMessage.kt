package com.erwiin21mp.cinemovilplus

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ServiceMessage : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let {
            sendNotification(message)
        }
    }

    private fun sendNotification(message: RemoteMessage) {

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}