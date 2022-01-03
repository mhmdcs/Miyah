package com.example.miyah

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.miyah.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    companion object {
        val TAG: String = LoginFragment::class.java.simpleName
    }

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //initialize the data binding variable
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.signUpButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
        }

        binding.forgotPassword.setOnClickListener {
            val email = binding.emailEditTextLogin.text.toString().trim()
            if (email.isEmpty()) {
                binding.emailEditTextLogin.error = "Email is required to reset password!"
                binding.emailEditTextLogin.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEditTextLogin.error = "Please provide valid email to reset password!"
                binding.emailEditTextLogin.requestFocus()
            } else {
                auth.sendPasswordResetEmail(email).addOnCompleteListener() {
                    if (it.isSuccessful) {
                        Toast.makeText(activity, "Successfully sent email to reset password", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "Could not send email to reset password", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

        binding.signInButton.setOnClickListener { view: View ->
            signingIn(view)
        }


        return binding.root
    }

    private fun signingIn(onClickView: View) {

        val email =
            binding.emailEditTextLogin.text.toString().trim() //good practice to trim whitespace
        val password = binding.passwordEditTextLogin.text.toString().trim()


        //perform validations

        if (email.isEmpty()) {
            binding.emailEditTextLogin.error = "Email is required!"
            binding.emailEditTextLogin.requestFocus()
        }

        //check if user provided a valid email address pattern (i.e. it has @ symbol or .com)
        // ! in the if-condition is a negation, this means: if it does NOT match, then do the following
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditTextLogin.error = "Please provide valid email!"
            binding.emailEditTextLogin.requestFocus()
        } else if (password.isEmpty()) {
            binding.passwordEditTextLogin.error = "Password is required!"
            binding.passwordEditTextLogin.requestFocus()
        }

        //validate that the password is over 6 characters
        //because firebase does not accept password that is less than 6 characters
        else if (password.length < 6) {
            binding.passwordEditTextLogin.error = "Minimum password length is 6 characters!"
            binding.passwordEditTextLogin.requestFocus()

        } else {
            //sign the user in via the firebase authentication
            binding.statusLoadingWheel.isVisible = true

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    //good practice to run addOnCompleteListener to check if the task completed
                    if (it.isSuccessful) {

                        val currentUser = FirebaseAuth.getInstance().uid.toString()
                        val typeDatabaseReference =
                            FirebaseDatabase.getInstance().getReference("users").child(currentUser)
                                .child("type")

                        typeDatabaseReference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                Log.i(TAG, "User type is: " + snapshot.value.toString())

                                if (snapshot.value.toString() == "Client") {
                                    onClickView.findNavController()
                                        .navigate(LoginFragmentDirections.actionLoginFragmentToClientFragment())
                                } else {
                                    onClickView.findNavController()
                                        .navigate(LoginFragmentDirections.actionLoginFragmentToProviderFragment())
                                }

                            }

                            override fun onCancelled(error: DatabaseError) {}

                        })

                        Toast.makeText(activity, "Successfully logged in", Toast.LENGTH_SHORT).show()
                        binding.emailEditTextLogin.text.clear()
                        binding.passwordEditTextLogin.text.clear()

                        binding.statusLoadingWheel.isVisible = false

                    } else {
                        Toast.makeText(activity, "User does not exist", Toast.LENGTH_SHORT).show()
                        binding.statusLoadingWheel.isVisible = false
                    }

                }

        }
    }


}