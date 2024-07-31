package com.uphar.smartbaroda

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat


class NotificationService(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "water_notification"
            val channelName = "Water Notification"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showBasicNotification() {
        val notification = NotificationCompat.Builder(context, "water_notification")
            .setContentTitle("Water Reminder")
            .setContentText("Time to drink a glass of water")
            .setSmallIcon(R.drawable.donut)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(5, notification)
    }

    fun showExpandableNotification(notificationsPopup :String) {
        val notification = NotificationCompat.Builder(context, "smart_baroda_notification")
            .setContentTitle("Best Ai Suggestions")
            .setContentText(notificationsPopup)
            .setSmallIcon(R.drawable.bob_logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(
                        context.bitmapFromResource(R.drawable.bob_logo)
                    )
            )
            .build()

        notificationManager.notify(8, notification)
    }

    private fun Context.bitmapFromResource(@DrawableRes resId: Int) =
        BitmapFactory.decodeResource(resources, resId)
}