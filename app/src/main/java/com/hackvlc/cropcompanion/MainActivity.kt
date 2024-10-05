package com.hackvlc.cropcompanion

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.hackvlc.cropcompanion.service.CommService

class MainActivity : ComponentActivity() {

    private var serviceMessenger: Messenger? = null

    private var responseMessenger = Messenger(ResponseHandler())

    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            serviceMessenger = Messenger(binder)
            val msg = CommService.bindAlertNotification(responseMessenger)
            serviceMessenger?.send(msg)
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            val msg = CommService.unbindAlertNotification(responseMessenger)
            serviceMessenger?.send(msg)
            serviceMessenger = null
            mBound = false
        }
    }

    inner class ResponseHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                CommService.COMMAND_WATER_ID -> {
                    notify( "Water command back received")
                }

                CommService.COMMAND_LIGHT_ID -> {
                    notify( "Light command back received")
                }

                CommService.COMMAND_SOLAR_WINDOW_ID -> {
                    notify( "Window command back received")
                }

                CommService.ALERT_NOTIFICATION_REGISTERED_ID -> {
                    notify( "register alert received")
                }

                CommService.ALERT_NOTIFICATION_UNREGISTERED_ID -> {
                    notify("unregister alert received")
                }

                CommService.HEAVY_RAINING_ALERT -> {
                    notify("Heavy rain alert received")
                }

                CommService.STRONG_WIND_ALERT -> {
                    notify("Strong wind alert received")
                }
            }
        }

        private fun notify(txt: String) {
            Toast.makeText(this@MainActivity, txt, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        findViewById<Button>(R.id.water_command_button).setOnClickListener {
            sendWaterCommand(0)
        }

        findViewById<Button>(R.id.light_command_button).setOnClickListener {
            sendLightCommand(0)
        }

        findViewById<Button>(R.id.window_command_button).setOnClickListener {
            sendWindowCommand(0)
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, CommService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }

    /**
     * Sends a command to control the water flow.
     * This function sends a signal to the water system to adjust the opening level,
     * controlling the amount of water flow.
     * Params:
     * opening - An integer representing the desired opening level for the water flow.
     * This value should be within the valid range of the water system 0 - 100 where 0 is closed and
     * 100 is completely open.
     */

    private fun sendWaterCommand(opening: Int) {
        serviceMessenger?.send(CommService.createWaterCommand(opening, responseMessenger))
    }

    private fun sendLightCommand(opening: Int) {
        serviceMessenger?.send(CommService.createLightCommand(opening, responseMessenger))
    }

    private fun sendWindowCommand(opening: Int) {
        serviceMessenger?.send(CommService.createWindowCommand(opening, responseMessenger))
    }
}