package io.github.com.harutiro.tempmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.com.harutiro.tempmanager.databinding.ActivityMainBinding
import io.github.com.harutiro.tempmanager.databinding.ActivityTempEditAcitivityBinding

class TempEditAcitivity : AppCompatActivity() {

    private lateinit var binding: ActivityTempEditAcitivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTempEditAcitivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}