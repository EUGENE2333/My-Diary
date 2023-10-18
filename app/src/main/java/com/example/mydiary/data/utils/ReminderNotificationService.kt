package com.example.mydiary.data.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.mydiary.MainActivity
import com.example.mydiary.R


class ReminderForegroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, serviceFlags: Int, startId: Int): Int {
        val title = intent?.getStringExtra("title") ?: "Reminder"
        val message = intent?.getStringExtra("message") ?: "Come and write your experiences and ideas!"

        val activityIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val activityPendingIntent = PendingIntent.getActivity(
            this,
            1,
            activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val dismissIntent = Intent(this, ReminderDismissReceiver::class.java).apply {
            action = "DISMISS_ACTION"
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(this, 0, dismissIntent, 0)

        val notification = NotificationCompat.Builder(this, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.notifications_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(activityPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(0, "Dismiss", getDismissPendingIntent(this))
            .build()

        startForeground(1, notification)

        return START_NOT_STICKY
    }

    private fun getDismissPendingIntent(context: Context): PendingIntent {
        val dismissIntent = Intent(context, ReminderDismissReceiver::class.java)
        dismissIntent.action = DISMISS_ACTION
        return PendingIntent.getBroadcast(
            context,
            0,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }



    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                REMINDER_CHANNEL_ID,
                "Reminder Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Used for reminder notifications"

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val REMINDER_CHANNEL_ID = "reminder_channel"
        const val DISMISS_ACTION = "com.example.reminder.DISMISS"

    }

   /* override fun onDestroy() {
        // Service is destroyed, restart it
        val restartIntent = Intent(this, ReminderForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(restartIntent)
        } else {
            startService(restartIntent)
        }
        super.onDestroy()
    } */
}

class ReminderDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ReminderForegroundService.DISMISS_ACTION) {
            // Stop the foreground service and remove the notification
            context.stopService(Intent(context, ReminderForegroundService::class.java))
        }
    }
}

 /* class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            // Start the foreground service on device boot
            val serviceIntent = Intent(context, ReminderForegroundService::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
        }
    }
} */



class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Reminder"
        val message = intent.getStringExtra("message") ?: "Come and write your experiences and ideas!"

        val activityIntent = Intent(context, MainActivity::class.java).apply {
           intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
             0
        )

        // Build and show the notification
        val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.notifications_icon)
            .setContentTitle("Reminder")
            .setContentText("Come and write your experiences and ideas!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(activityPendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification.build())

        // Start the foreground service
        val serviceIntent = Intent(context, ReminderForegroundService::class.java)
        serviceIntent.putExtra("title", title)
        serviceIntent.putExtra("message", message)
        ContextCompat.startForegroundService(context, serviceIntent)
    }

    companion object {
        const val REMINDER_CHANNEL_ID = "reminder_channel"
    }
}






