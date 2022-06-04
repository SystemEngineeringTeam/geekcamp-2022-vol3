package io.github.com.harutiro.tempmanager.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.github.com.harutiro.tempmanager.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dataStore:SharedPreferences = requireActivity().getSharedPreferences("DateStore", Context.MODE_PRIVATE)

        binding.userIdEditText.setText(dataStore.getString("ID","NoDate"))
        binding.userNameEditText.setText(dataStore.getString("NAME","NoDate"))

        Glide.with(context).load(dataStore.getString("ICONIMAGE","")).into(binding.userIconImageButtonProfileFragment)

        //TODO：Profileの編集をできるようにする。

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}