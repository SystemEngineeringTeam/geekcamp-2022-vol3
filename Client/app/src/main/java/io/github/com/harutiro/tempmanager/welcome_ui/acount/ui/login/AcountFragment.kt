package io.github.com.harutiro.tempmanager.welcome_ui.acount.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import io.github.com.harutiro.tempmanager.R
import io.github.com.harutiro.tempmanager.databinding.ActivityMainBinding
import io.github.com.harutiro.tempmanager.databinding.FragmentAcountBinding
import io.github.com.harutiro.tempmanager.databinding.FragmentStaterBinding

class AcountFragment : Fragment() {

    private lateinit var binding: FragmentAcountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAcountBinding.inflate(layoutInflater)



        return inflater.inflate(R.layout.fragment_acount, container, false)
    }
}