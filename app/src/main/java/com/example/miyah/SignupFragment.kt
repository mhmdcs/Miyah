package com.example.miyah

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.miyah.databinding.FragmentBarBinding
import com.example.miyah.databinding.FragmentSignupBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //initialize the data binding variable
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_signup,
            container,
            false)

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.signUpButton.setOnClickListener { view: View ->

            if(registerUser()){ //means if(registerUser()==true) it is implicitly true in kotlin
                view.findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToBarFragment())
            }
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun registerUser(): Boolean {
        var bool = false
        var email = binding.emailEditTextSignup.text.toString().trim()
        var name = binding.nameEditTextSignup.text.toString().trim()
        var phone = binding.phoneEditTextSignup.text.toString().trim()
        var password = binding.passwordEditTextSignup.text.toString().trim()
        var radioButtonGroup = binding.rbtnGroupSignUp.checkedRadioButtonId

        if(name.isEmpty()){
            binding.nameEditTextSignup.error = "Name is required!"
            binding.nameEditTextSignup.requestFocus()
        }

       else if(phone.isEmpty()){
            binding.phoneEditTextSignup.error = "Phone is required!"
            binding.phoneEditTextSignup.requestFocus()
        }

        else if(email.isEmpty()){
            binding.emailEditTextSignup.error = "Email is required!"
            binding.emailEditTextSignup.requestFocus()
        }

         //check if user provided a valid email address pattern (i.e. it has @ symbol or .com)
        // ! in the if-condition is a negation, this means: if it does NOT match, then do the following
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEditTextSignup.error = "Please provide valid email!"
            binding.emailEditTextSignup.requestFocus()
        }

        else if(password.isEmpty()){
            binding.passwordEditTextSignup.error = "Password is required!"
            binding.passwordEditTextSignup.requestFocus()
        }

        //validate that the password is over 6 characters
        //because firebase does not accept password that is less than 6 characters
        else if(password.length < 6){
            binding.passwordEditTextSignup.error = "Minimum password length is 6 characters!"
            binding.passwordEditTextSignup.requestFocus()

        }

        else {
            bool = true
        }

        binding.statusLoadingWheel.isVisible
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult>{

                //check if task (user registration) has been completed and successful
                override fun onComplete(task: Task<AuthResult>) {
                    if(task.isSuccessful){
                        val user = User(email, name, phone) //create an object of User

                        //send the user object to the realtime database by getting the current user ID and passing it the user object
                        FirebaseAuth.getInstance().currentUser?.let {
                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(it.uid)
                                .setValue(user).addOnCompleteListener{
                                    if(it.isSuccessful){
                                        Toast.makeText(activity,"Successful registration",Toast.LENGTH_SHORT).show()
                                        binding.statusLoadingWheel.isGone
                                        bool = true
                                    } else {
                                        Toast.makeText(activity,"Failure to register",Toast.LENGTH_SHORT).show()
                                        binding.statusLoadingWheel.isGone
                                    }
                                }
                        }
                    } else {
                        Toast.makeText(activity,"Failure to register",Toast.LENGTH_SHORT).show()
                        binding.statusLoadingWheel.isGone
                    }
                }


            })

        return bool

    }

}