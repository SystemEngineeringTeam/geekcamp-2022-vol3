package io.github.com.harutiro.tempmanager.welcome_ui.stater

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.github.com.harutiro.tempmanager.R
import io.github.com.harutiro.tempmanager.databinding.FragmentAcountBinding
import io.github.com.harutiro.tempmanager.databinding.FragmentStaterBinding


class StaterFragment : Fragment() {

    private lateinit var binding: FragmentStaterBinding

    private lateinit var auth: FirebaseAuth

    val TAG = "AuthDebug"

    private lateinit var oneTapClient: SignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inflate the layout for this fragment
        binding = FragmentStaterBinding.inflate(layoutInflater)


        // Initialize Firebase Auth
        auth = Firebase.auth

        oneTapClient = Identity.getSignInClient(requireActivity())

        Log.d(TAG, auth.currentUser?.photoUrl.toString())

//        Glide.with(this).load(auth.currentUser?.photoUrl).into(findViewById(R.id.iconImage))

        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.default_web_client_id_auth))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build();




//        findViewById<Button>(R.id.authCheckButton).setOnClickListener {
//            val user = FirebaseAuth.getInstance().currentUser
//            if (user != null) {
//                // User is signed in
//                Log.d(TAG,user.email.toString())
//            } else {
//                // No user is signed in
//                Log.d(TAG,"NoAccount")
//
//            }
//        }

//        findViewById<Button>(R.id.authLogoutButton).setOnClickListener {
//            FirebaseAuth.getInstance().signOut()
//        }

        view.findViewById<Button>(R.id.login_button_stater_fragment)?.setOnClickListener {


            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(requireActivity()) { result ->
                    try {
                        startIntentSenderForResult(
                            result.pendingIntent.intentSender, REQ_ONE_TAP,
                            null, 0, 0, 0, null)
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e("Auth2", "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener(requireActivity()) { e ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    Log.d("Auth3", e.localizedMessage)
                }
        }
    }

    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with Firebase.
                            Log.d(TAG, "Got ID token.")

                            firebaseCertification(data)
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token!")
                        }
                    }
                } catch (e: ApiException) {

                    Log.d(TAG,e.toString())
                    // ...
                }
            }
        }
    }

    fun firebaseCertification ( data : Intent?){
        val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
        val idToken = googleCredential.googleIdToken
        when {
            idToken != null -> {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success")
                            val user = auth.currentUser
                            Log.d(TAG, user?.email.toString())

                            val db = Firebase.firestore
                            db.collection("User")
                                .whereEqualTo("EMAIL", user?.email.toString())
                                .get()
                                .addOnSuccessListener { result ->
                                    var user: MutableMap<String, Any>? = null

                                    for (document in result) {

                                        user = document.data

                                        Log.d(TAG, "${document.id} => ${document.data}")
                                        Log.d(TAG, "${document.id} => ${user["ICONIMAGE"]}")



                                    }



                                    if(result.size()>0){


                                        val dataStore: SharedPreferences = requireActivity().getSharedPreferences("DateStore", Context.MODE_PRIVATE)

                                        val editor = dataStore.edit()
                                        editor.putString("UUID", user?.get("UUID").toString())
                                        editor.putString("ID", user?.get("ID").toString())
                                        editor.putString("EMAIL", user?.get("EMAIL").toString())
                                        editor.putString("NAME", user?.get("NAME").toString())
                                        editor.putString("ICONIMAGE", user?.get("ICONIMAGE").toString())
                                        editor.apply()

                                        requireActivity().finish()

                                    }else{
                                        requireActivity().findViewById<ViewPager2>(R.id.view_pager).currentItem = 1
                                    }

                                }
                                .addOnFailureListener { exception ->
                                    Log.w(TAG, "Error getting documents.", exception)
                                }


//                                updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.exception)
//                                updateUI(null)
                        }
                    }
            }
            else -> {
                // Shouldn't happen.
                Log.d(TAG, "No ID token!")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
//            reload()
        }
    }

}