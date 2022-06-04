package io.github.com.harutiro.tempmanager

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import io.github.com.harutiro.tempmanager.databinding.ActivityMainBinding
import io.github.com.harutiro.tempmanager.service.IbeaconOutputService
import org.altbeacon.beacon.*

class MainActivity : AppCompatActivity(), RangeNotifier,MonitorNotifier {

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth

    val TAG = "mainActivity"

    val region = Region("unique-id-001", Identifier.parse("ba4d8ef7-51a3-110c-a55d-bca1afbba494"), Identifier.parse("56562"), null)

    var insideRegion = false

    private val IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            Log.d(TAG,user.email.toString())

            val dataStore: SharedPreferences = getSharedPreferences("DateStore", Context.MODE_PRIVATE)


            val intent = Intent(this, IbeaconOutputService::class.java)
            intent.putExtra("UUID",dataStore.getString("UUID",""))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            }

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



        val beaconManager = BeaconManager.getInstanceForApplication(this)

        beaconManager.beaconParsers.clear()
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_FORMAT))


        // Uncomment the code below to use a foreground service to scan for beacons. This unlocks
        // the ability to continually scan for long periods of time in the background on Andorid 8+
        // in exchange for showing an icon at the top of the screen and a always-on notification to
        // communicate to users that your app is using resources in the background.
        //
        val builder = Notification.Builder(this)
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setContentTitle("Scanning for Beacons")
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_MUTABLE
        )
        builder.setContentIntent(pendingIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "My Notification Channel ID",
                "My Notification Name", NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "My Notification Channel Description"
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

    override fun didEnterRegion(arg0: Region?) {
        Log.d(TAG, "did enter region.")
        insideRegion = true
        // Send a notification to the user whenever a Beacon
        // matching a Region (defined above) are first seen.
        Log.d(TAG, "Sending notification.")
    }

    override fun didExitRegion(region: Region?) {
        insideRegion = false
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