package io.github.com.harutiro.tempmanager.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.content.SharedPreferences
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import io.github.com.harutiro.tempmanager.MainActivity
import io.github.com.harutiro.tempmanager.R
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter

class IbeaconOutputService : Service() {

    companion object {
        const val CHANNEL_ID = "1111"
    }
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    lateinit var beaconTransmitter: BeaconTransmitter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("Service","onStartCommand called")

        //1．通知領域タップで戻ってくる先のActivity
        val openIntent = Intent(this, MainActivity::class.java).let {
            PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }

        //2．通知チャネル登録
        val channelId = CHANNEL_ID
        val channelName = "TestService Channel"
        val channel = NotificationChannel(
            channelId, channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        //4．通知の作成（ここでPendingIntentを通知領域に渡す）
        val notification = NotificationCompat.Builder(this, CHANNEL_ID )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("ビーコンの送信中")
            .setContentText("体温は${intent?.getStringExtra("TEMP")}°で送信しています。")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(openIntent)
            .build()

        //5．フォアグラウンド開始。
        startForeground(2222, notification)


        //ビーコンのパーサーの作成、ibeaconの取得をするときに必要
        val beaconParser = BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
        beaconTransmitter = BeaconTransmitter(applicationContext, beaconParser)

        //テキストボックスの情報を取得
        val uuid = intent?.getStringExtra("UUID") ?:""
        val major = "56562"
        val minor = intent?.getStringExtra("TEMP")?.toDouble()?.times(10)?.toInt().toString() ?:"0"

        //beaconのビルダーで、どんなデータを送信するか作成する。
        val beacon = Beacon.Builder()
            .setId1(uuid)
            .setId2(major)
            .setId3(minor)
            .setManufacturer(0x004C)
            .build()

        //実際にbeaconを開始する部分
        beaconTransmitter.startAdvertising(beacon, object : AdvertiseCallback() {

            //正しく動作したとき
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                super.onStartSuccess(settingsInEffect)
                Log.d("debag","OK")

            }

            //失敗したとき
            override fun onStartFailure(errorCode: Int) {
                Log.d("debag","NO")
            }
        })





        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        beaconTransmitter.stopAdvertising()

    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)


    }
}