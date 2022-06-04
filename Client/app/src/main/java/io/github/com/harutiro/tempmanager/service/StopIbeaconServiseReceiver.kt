package io.github.com.harutiro.tempmanager.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class StopIbeaconServiseReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val targetIntent = Intent(context, IbeaconInputService::class.java)
        context.stopService(targetIntent)


    }
}