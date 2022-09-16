package io.github.com.harutiro.tempmanager

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AppLaunchChecker
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import io.github.com.harutiro.tempmanager.databinding.ActivityMainBinding
import io.github.com.harutiro.tempmanager.service.IbeaconOutputService
import org.altbeacon.beacon.*
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity(), RangeNotifier,MonitorNotifier{

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth

    val TAG = "mainActivity"

    val region = Region("unique-id-001", Identifier.parse("ba4d8ef7-51a3-110c-a55d-bca1afbba494"), Identifier.parse("56562"), null)

    var insideRegion = false

    private val IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"



    val permissions = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE
        )
    }else{
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            Log.d(TAG,user.email.toString())
        } else {
            // No user is signed in
            Log.d(TAG,"NoAccount")

            val intent = Intent(this,WelcomeActivity::class.java)
            startActivity(intent)
        }


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_profile, R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()

        if (!EasyPermissions.hasPermissions(this, *permissions)) {
            val snackbar = Snackbar.make(findViewById(R.id.cordinator_layout_main_activity),"パーミッションが許可されていいません。", Snackbar.LENGTH_SHORT)
            snackbar.view.setBackgroundResource(R.color.error)
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.purple_200))
            snackbar.setTextColor(ContextCompat.getColor(this, R.color.purple_200))
            snackbar.show()
        }

        ibeacon()

    }

    fun ibeacon(){
        //ここからビーコン関係
        if (EasyPermissions.hasPermissions(this, *permissions)) {
            val beaconManager = BeaconManager.getInstanceForApplication(this)

            // 初期化。これがないと画面を２回以上開いた時に２重でデータを受信してしまう
            beaconManager.removeAllMonitorNotifiers()
            beaconManager.removeAllRangeNotifiers()
            beaconManager.rangedRegions.forEach {region ->
                beaconManager.stopRangingBeacons(region)
                beaconManager.stopMonitoring(region)

            }


            beaconManager.beaconParsers.clear()
            beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_FORMAT))

            val builder = Notification.Builder(this)
            builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            builder.setContentTitle("入退室をチェック中")
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_MUTABLE
            )
            builder.setContentIntent(pendingIntent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "My Notification Channel ID",
                    "入退室チェック通知チャンネル", NotificationManager.IMPORTANCE_DEFAULT
                )
                channel.description = "ビーコンの読み取り時の通知チャンネル"
                val notificationManager = getSystemService(
                    NOTIFICATION_SERVICE
                ) as NotificationManager
                notificationManager.createNotificationChannel(channel)
                builder.setChannelId(channel.id)
            }
            beaconManager.enableForegroundServiceScanning(builder.build(), 456)

            beaconManager.setEnableScheduledScanJobs(false)
            beaconManager.backgroundBetweenScanPeriod = 0
            beaconManager.backgroundScanPeriod = 1100

            Log.d(TAG, "setting up background monitoring in app onCreate")
            beaconManager.addMonitorNotifier(this)


            // If we were monitoring *different* regions on the last run of this app, they will be
            // remembered.  In this case we need to disable them here
            for (region in beaconManager.monitoredRegions) {
                beaconManager.stopMonitoring(region!!)
            }

            beaconManager.startMonitoring(region)

            beaconManager.addRangeNotifier(this)

            beaconManager.startRangingBeacons(region)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        //初回起動かどうかを判断する部分
        AppLaunchChecker.onActivityCreate(this)
    }

    override fun didEnterRegion(arg0: Region?) {
        Log.d(TAG, "did enter region.")
        insideRegion = true

        createNotificationChannel()

        // Create an explicit intent for an Activity in your app
        val intent = Intent(this,TempEditAcitivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        val builder = NotificationCompat.Builder(this,"room_inside_notify")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("入退室を検知しました。")
            .setContentText("タップして体温の記入をお願いします。")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            Log.d(TAG,user.email.toString())

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(22, builder.build())
            }
        }
    }

    private fun createNotificationChannel() {
        val name = "入退室検知通知"
        val descriptionText = "入退室を検知した時に通知するチャンネルです。"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("room_inside_notify", name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


    override fun didExitRegion(region: Region?) {
        insideRegion = false

        val targetIntent = Intent(this, IbeaconOutputService::class.java)
        stopService(targetIntent)
        // do nothing here. logging happens in MonitoringActivity
    }

    override fun didDetermineStateForRegion(state: Int, region: Region?) {
        // do nothing here. logging happens in MonitoringActivity
    }

    override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>?, region: Region?) {
        // 検知したBeaconの情報
        Log.d("MainActivity", "beacons.size ${beacons?.size}")
        beacons?.let {
            for (beacon in beacons) {
                Log.d("MainActivity", "UUID: ${beacon.id1}, major: ${beacon.id2}, minor: ${beacon.id3}, RSSI: ${beacon.rssi}, TxPower: ${beacon.txPower}, Distance: ${beacon.distance}")
            }
        }
    }

}