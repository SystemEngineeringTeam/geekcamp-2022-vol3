package io.github.com.harutiro.tempmanager.welcome_ui.acount.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.github.com.harutiro.tempmanager.R
import io.github.com.harutiro.tempmanager.databinding.ActivityMainBinding
import io.github.com.harutiro.tempmanager.databinding.FragmentAcountBinding
import io.github.com.harutiro.tempmanager.databinding.FragmentStaterBinding
import java.util.*

class AcountFragment : Fragment() {

    private lateinit var binding: FragmentAcountBinding

    private lateinit var auth: FirebaseAuth

    val TAG = "account"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAcountBinding.inflate(inflater, container, false)
        //authのデータを受け取る
        auth = Firebase.auth
        //datebaseのインスタンスの作成
        val db = Firebase.firestore


        //デフォルトの名前を入力
        binding.mainNameEditTextAcountFragment.setText(auth.currentUser?.displayName)

        //写真のはめ込み
        Glide.with(context).load(auth.currentUser?.photoUrl).into(binding.userIconImageButtonAcountFragment)


        binding.fixFavAcountFragment.setOnClickListener{
            val user = hashMapOf(
                "UUID" to UUID.randomUUID().toString(),
                "ID" to binding.mainStudentNumberEditTextAcountFragment.text.toString(),
                "EMAIL" to auth.currentUser?.email.toString(),
                "NAME" to binding.mainNameEditTextAcountFragment.text.toString(),
                "ICONIMAGE" to auth.currentUser?.photoUrl.toString(),
            )

            db.collection("User")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    requireActivity().finish()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

            //TODO ここでUserデータをシャプリに保存



        }

        return binding.root
    }

}