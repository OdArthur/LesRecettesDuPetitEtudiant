package com.example.lesrecettesdupetitetudiant

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

class Notifications : Application() {

    override fun onCreate() {
        super.onCreate()

        val intentDay = Intent(this, MyNotificationReceiver::class.java)
        intentDay.putExtra("message", "Il est 11 heures ! Prêt à préparer un bon plat ?")
        val intentNight = Intent(this, MyNotificationReceiver::class.java)
        intentNight.putExtra("message", "Il est 19 heures ! Prêt à préparer un bon plat ?")
        val pendingIntentDay = PendingIntent.getBroadcast(this, 0, intentDay, PendingIntent.FLAG_IMMUTABLE)
        val pendingIntentNight = PendingIntent.getBroadcast(this, 0, intentNight, PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 11)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntentDay
        )

        calendar.set(Calendar.HOUR_OF_DAY, 19)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntentNight
        )
    }
}