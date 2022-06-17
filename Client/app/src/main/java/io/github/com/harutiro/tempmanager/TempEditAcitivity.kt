package io.github.com.harutiro.tempmanager

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import io.github.com.harutiro.tempmanager.databinding.ActivityMainBinding
import io.github.com.harutiro.tempmanager.databinding.ActivityTempEditAcitivityBinding
import io.github.com.harutiro.tempmanager.service.IbeaconOutputService
import pub.devrel.easypermissions.EasyPermissions

class TempEditAcitivity : AppCompatActivity() {

    private lateinit var binding: ActivityTempEditAcitivityBinding

    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTempEditAcitivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataStore: SharedPreferences = getSharedPreferences("DateStore", Context.MODE_PRIVATE)

        Glide.with(this).load(dataStore.getString("ICONIMAGE","")).into(binding.userIconImageButtonTempEditActivity)


        binding.registerButtonTempEditActivity.setOnClickListener {
            val intent = Intent(this, IbeaconOutputService::class.java)
            intent.putExtra("UUID",dataStore.getString("UUID",""))
            intent.putExtra("TEMP",binding.mainTempEditTextTempEditActivity.text.toString())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && EasyPermissions.hasPermissions(this, *permissions)) {
                startForegroundService(intent)
            }

            finish()
        }



    }

    //戻る動作無効
    override fun onBackPressed() {}
}