package io.github.com.harutiro.tempmanager.welcome_ui.stater

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.com.harutiro.tempmanager.R
import io.github.com.harutiro.tempmanager.databinding.FragmentAcountBinding
import io.github.com.harutiro.tempmanager.databinding.FragmentStaterBinding


class StaterFragment : Fragment() {

    private lateinit var binding: FragmentStaterBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStaterBinding.inflate(layoutInflater)



        return inflater.inflate(R.layout.fragment_stater, container, false)
    }

}