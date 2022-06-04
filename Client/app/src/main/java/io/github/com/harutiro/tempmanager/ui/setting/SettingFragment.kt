package io.github.com.harutiro.tempmanager.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import io.github.com.harutiro.tempmanager.WelcomeActivity
import io.github.com.harutiro.tempmanager.databinding.FragmentNotificationsBinding

class SettingFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.welcomeTestButton.setOnClickListener {
            val intent = Intent(context, WelcomeActivity::class.java)
            startActivity(intent)

        }

        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(context,WelcomeActivity::class.java)
            startActivity(intent)

        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}