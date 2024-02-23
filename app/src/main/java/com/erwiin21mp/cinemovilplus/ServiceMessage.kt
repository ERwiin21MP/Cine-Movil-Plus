package com.erwiin21mp.cinemovilplus

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat.BigTextStyle
import androidx.core.app.NotificationCompat.Builder
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import com.erwiin21mp.cinemovilplus.core.ext.logData
import com.erwiin21mp.cinemovilplus.ui.view.contentView.ContentViewActivity
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.ID
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class ServiceMessage : FirebaseMessagingService() {

    private val random = Random

    companion object {
        const val CHANNEL_NAME = "FCM notification channel"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        logData(message.notification!!.title.toString(), "FCM Title")
        logData(message.notification!!.body.toString(), "FCM Body")
        logData(message.data.toString(), "message.data")
        sendNotification(message.notification!!, message.data[ID]!!.toString())
        logData(message.data[ID]!!, "get")
    }

    private fun sendNotification(message: RemoteMessage.Notification, id: String) {
        val title = message.title
        val body = message.body

        val intent = Intent(this, ContentViewActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(ID, id)
        }

        val pendingIntent = getActivity(this, 0, intent, FLAG_UPDATE_CURRENT)
        val channelId = getString(R.string.default_notification_channel_id)

        val builder = Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setStyle(BigTextStyle().bigText(body))
            .setAutoCancel(true)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Descripcion de la notification"
            }
            manager.createNotificationChannel(channel)
        }

        manager.notify(random.nextInt(), builder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}