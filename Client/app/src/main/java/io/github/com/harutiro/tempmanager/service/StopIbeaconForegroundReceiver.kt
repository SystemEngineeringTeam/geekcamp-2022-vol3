package io.github.com.harutiro.tempmanager.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class StopIbeaconForegroundReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val targetIntent = Intent(context, IbeaconOutputService::class.java)
        context.stopService(targetIntent)
    }
}