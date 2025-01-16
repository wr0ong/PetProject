package com.hfad.pet3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.hfad.pet3.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        database = Firebase.database.reference
        auth = Firebase.auth

        binding.fragmentRegisterRegisterButton.setOnClickListener {
                createAccount(
                    binding.fragmentRegisterNewEmail.text.toString(),
                    binding.fragmentRegisterNewPassword.text.toString()
                )
        }
        return view
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(
                        context,
                        SUCCESS_REGISTRATION,
                        Toast.LENGTH_SHORT,
                    ).show()
                    val user = auth.currentUser
                    val nameOfUser = email.toString().replaceAfter("@", "").dropLast(1)
                    writeNewUser(user?.uid.toString(), nameOfUser, user?.email.toString())
                    view?.findNavController()
                        ?.navigate(R.id.action_registerFragment_to_usersFragment)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        FAILURE_REGISTRATION,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun validateForm(): Boolean {
        var valid = false

        valid =
            !((binding.fragmentRegisterNewEmail.toString() == "") || (binding.fragmentRegisterNewPassword.toString() == ""))
        return valid
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun writeNewUser(userId: String, name: String, email: String) {
        val user = User(name, email)

        database.child("users").child(userId).setValue(user)
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}

const val FAILURE_REGISTRATION = "Registration is failed! Try again!"
const val SUCCESS_REGISTRATION = "Registration is success!"
