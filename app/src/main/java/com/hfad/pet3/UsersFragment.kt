package com.hfad.pet3

import android.content.ContentValues.TAG
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.hfad.pet3.databinding.FragmentUsersBinding

class UsersFragment : Fragment() {
    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        val view = binding.root

        binding.emailVerification.setOnClickListener {
            sendEmailVerification()
        }

        binding.signOut.setOnClickListener {
            signOut()
            view.findNavController()
                .navigate(R.id.action_usersFragment_to_welcomeFragment)
        }

        return view
    }

    private fun sendEmailVerification() {

        binding.emailVerification.isEnabled = false

        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(requireActivity()) { task ->
                binding.emailVerification.isEnabled = true

                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "$SUCCESS_SEND_VERIFICATION ${user.email} ",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(
                        context,
                        FAILURE_SEND_VERIFICATION,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun signOut(){
        auth.signOut()
        Toast.makeText(
            context,
            SIGNED_OUT,
            Toast.LENGTH_SHORT,
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

const val SUCCESS_SEND_VERIFICATION = "Verification email sent to"
const val FAILURE_SEND_VERIFICATION = "Failed to send verification email"
const val SIGNED_OUT = "You signed out"