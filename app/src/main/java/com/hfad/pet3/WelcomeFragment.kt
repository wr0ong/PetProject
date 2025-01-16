package com.hfad.pet3

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.hfad.pet3.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = Firebase.auth

        binding.fragmentWelcomeButtonEnter.setOnClickListener {

                    signIn(
                        binding.fragmentWelcomeUserEmail.text.toString(),
                        binding.fragmentWelcomeUserPassword.text.toString()
                    )



            }

        binding.fragmentWelcomeRegisterButton.setOnClickListener (
            Navigation.createNavigateOnClickListener(
                R.id.action_welcomeFragment_to_registerFragment
            )
        )

        binding.fragmentWelcomeForgotPassword.setOnClickListener {
            auth.sendPasswordResetEmail(binding.fragmentWelcomeUserEmail.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                        Toast.makeText(
                            context,
                            SUCCESS_SEND_PASSWORD,
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                    else { Toast.makeText(
                        context,
                        FAILURE_SEND_PASSWORD,
                        Toast.LENGTH_SHORT,
                    ).show() }
                }
        }
        return view
    }


    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(
                        context,
                        SUCCESS_AUTHENTICATION,
                        Toast.LENGTH_SHORT,
                    ).show()
                    view?.findNavController()
                        ?.navigate(R.id.action_welcomeFragment_to_usersFragment)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        FAILURE_AUTHENTICATION,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = binding.fragmentWelcomeUserEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.fragmentWelcomeUserEmail.error = "Required."
            valid = false
        } else {
            binding.fragmentWelcomeUserEmail.error = null
        }

        val password = binding.fragmentWelcomeUserPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.fragmentWelcomeUserPassword.error = "Required."
            valid = false
        } else {
            binding.fragmentWelcomeUserPassword.error = null
        }

        return valid
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}

const val SUCCESS_AUTHENTICATION = "Authentication is success!"
const val FAILURE_AUTHENTICATION = "Authentication is failed. Try again"
const val SUCCESS_SEND_PASSWORD = "Verification email sent to"
const val FAILURE_SEND_PASSWORD = "Failed to send verification email. Try to enter email or to register"

