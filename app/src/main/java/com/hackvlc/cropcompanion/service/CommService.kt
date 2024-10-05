package com.hackvlc.cropcompanion.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class CommService : Service() {

    companion object {

        fun bindAlertNotification(msgBackMessenger: Messenger): Message {
            val msg = Message.obtain(null, ALERT_NOTIFICATION_REGISTERED_ID)
            msg.replyTo = msgBackMessenger
            return msg
        }

        fun unbindAlertNotification(msgBackMessenger: Messenger): Message {
            val msg = Message.obtain(null, ALERT_NOTIFICATION_UNREGISTERED_ID)
            msg.replyTo = msgBackMessenger
            return msg
        }

        fun createWaterCommand(opening: Int, msgBackMessenger: Messenger): Message {
            val msg = Message.obtain(null, COMMAND_WATER_ID, opening)
            msg.replyTo = msgBackMessenger
            return msg
        }

        fun createLightCommand(opening: Int, msgBackMessenger: Messenger): Message {
            val msg = Message.obtain(null, COMMAND_LIGHT_ID, opening)
            msg.replyTo = msgBackMessenger
            return msg
        }

        fun createWindowCommand(opening: Int, msgBackMessenger: Messenger): Message {
            val msg = Message.obtain(null, COMMAND_SOLAR_WINDOW_ID, opening)
            msg.replyTo = msgBackMessenger
            return msg
        }

        const val ALERT_NOTIFICATION_REGISTERED_ID = 0

        const val ALERT_NOTIFICATION_UNREGISTERED_ID = 1

        const val COMMAND_SOLAR_WINDOW_ID = 2

        const val COMMAND_LIGHT_ID = 3

        const val COMMAND_WATER_ID = 4

        const val HEAVY_RAINING_ALERT = 5

        const val STRONG_WIND_ALERT = 6
    }


    private lateinit var mMessenger: Messenger

    internal class CommandHandler : Handler(Looper.getMainLooper()) {

        private val scheduler = Executors.newSingleThreadScheduledExecutor()

       inner class AlarmNotification(private val messenger: Messenger) : Runnable {

            override fun run() {
                Log.i("AlarmNotification", "alarm trigger")
                // call api for notification
                val alert = 1
                when (alert) {
                    1 -> {
                        heavyRainingAlertNotification(messenger)
                    }
                    2->{
                        strongWindAlertNotification(messenger)
                    }
                }

            }
        }

        override fun handleMessage(msg: Message) {
            when (msg.what) {

                ALERT_NOTIFICATION_REGISTERED_ID -> {
                    registerNotification(msg)
                }

                ALERT_NOTIFICATION_UNREGISTERED_ID -> {
                    unregisterNotification(msg)
                }

                COMMAND_SOLAR_WINDOW_ID -> {
                    commandWindow(msg)
                }

                COMMAND_LIGHT_ID -> {
                    commandLight(msg)
                }

                COMMAND_WATER_ID -> {
                    commandWatering(msg)
                }

                else -> super.handleMessage(msg)
            }
        }

        private fun registerNotification(msg: Message) {
            scheduler.scheduleWithFixedDelay(AlarmNotification(msg.replyTo), 0, 30, TimeUnit.SECONDS)
            confirmBackMessage(msg)
        }

        private fun unregisterNotification(msg: Message) {
            scheduler.shutdownNow()
            confirmBackMessage(msg)
        }

        private fun commandWatering(msg: Message) {
            val opening = msg.obj as Int
            //TODO api call for watering
            confirmBackMessage(msg)
        }

        private fun commandLight(msg: Message) {
            val opening = msg.obj as Int
            //TODO api call for light
            confirmBackMessage(msg)
        }

        private fun commandWindow(msg: Message) {
            val opening = msg.obj as Int
            // TODO api call for window
            confirmBackMessage(msg)
        }

        private fun heavyRainingAlertNotification(messenger: Messenger) {
            messenger.send(Message.obtain(null, HEAVY_RAINING_ALERT))
        }

        private fun strongWindAlertNotification(messenger: Messenger) {
            messenger.send(Message.obtain(null, STRONG_WIND_ALERT))
        }

        private fun confirmBackMessage(msg: Message) {
            msg.replyTo?.send(Message.obtain(null, msg.what))
        }
    }


    override fun onBind(intent: Intent): IBinder? {
        mMessenger = Messenger(CommandHandler())
        return mMessenger.binder
    }
}