//package io.github.com.harutiro.tempmanager.service
//
//import android.app.Application
//import android.content.Intent
//import android.util.Log
//import android.widget.Toast
//import io.github.com.harutiro.tempmanager.MainActivity
//import org.altbeacon.beacon.*
//
//
//class BeaconApplication : Application() , MonitorNotifier, RangeNotifier {
//
//    val TAG = "Application"
//
//    // iBeaconのデータを認識するためのParserフォーマット
//    private val IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
//
//    private lateinit var beaconManager: BeaconManager
//
//    override fun onCreate() {
//        super.onCreate()
//
//        val region = Region("unique-id-001", null, null, null)
//
//        beaconManager = BeaconManager.getInstanceForApplication(this)
//        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_FORMAT))
//
//        beaconManager.addMonitorNotifier(this)
//        beaconManager.addRangeNotifier(this)
//        beaconManager.startMonitoring(region)
//        beaconManager.startRangingBeacons(region)
//
//
//
//    }
//
//    override fun didEnterRegion(region: Region?) {
//        // 領域に入場した
//        Log.d(TAG, "Enter Region")
//        val intent = Intent(this, MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//
//        // Important:  make sure to add android:launchMode="singleInstance" in the manifest
//        // to keep multiple copies of this activity from getting created if the user has
//        // already manually launched the app.
//
//        // Important:  make sure to add android:launchMode="singleInstance" in the manifest
//        // to keep multiple copies of this activity from getting created if the user has
//        // already manually launched the app.
//        this.startActivity(intent)
//    }
//
//    override fun didExitRegion(region: Region?) {
//        // 領域から退場した
//        Log.d(TAG, "Exit Region")
//    }
//
//    override fun didDetermineStateForRegion(i: Int, region: Region?) {
//        // 入退場状態が変更された
//        Log.d(TAG, "Determine State: $i")
//    }
//
//    override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>?, region: Region?) {
//        Log.d("MainActivity", "beacons.size ${beacons?.size}")
//        beacons?.let {
//            for (beacon in beacons) {
//                Log.d("MainActivity", "UUID: ${beacon.id1}, major: ${beacon.id2}, minor: ${beacon.id3}, RSSI: ${beacon.rssi}, TxPower: ${beacon.txPower}, Distance: ${beacon.distance}")
//                Toast.makeText(this , beacon.id1.toString(), Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//}