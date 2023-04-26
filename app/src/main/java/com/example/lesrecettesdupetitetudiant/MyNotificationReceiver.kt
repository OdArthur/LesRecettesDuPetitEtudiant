package com.example.lesrecettesdupetitetudiant

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class MyNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val message = intent.getStringExtra("message")!!

        val notification = NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("On prépare à manger ?")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()


        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }
}