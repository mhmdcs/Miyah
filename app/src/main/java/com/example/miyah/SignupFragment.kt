package com.example.miyah

import android.os.Bundle
import android.util.Log
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
import com.example.miyah.databinding.FragmentSignupBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class SignupFragment : Fragment() {

    companion object {
        val TAG: String = SignupFragment::class.java.simpleName
    }

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
            registerUser(view)
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun registerUser(onClickView: View) {
        val email = binding.emailEditTextSignup.text.toString().trim() //good practice to trim whitespace
        val name = binding.nameEditTextSignup.text.toString().trim()
        val phone = binding.phoneEditTextSignup.text.toString().trim()
        val password = binding.passwordEditTextSignup.text.toString().trim()
        val location = ""
        val espDistance = 0
        val requestStatus = ""

        //perform validations

        if(name.isEmpty()){
            binding.nameEditTextSignup.error = "Name is required!"
            binding.nameEditTextSignup.requestFocus()
        }

       else if(phone.isEmpty()){
            binding.phoneEditTextSignup.error = "Phone is required!"
            binding.phoneEditTextSignup.requestFocus()
        }

        else if(phone.length<9){
            binding.phoneEditTextSignup.error = "Phone must be over 9 numbers long!"
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
        //write user data to the realtime database

        binding.statusLoadingWheel.isVisible = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult>{

                //check if task (user registration) has been completed and successful
                override fun onComplete(task: Task<AuthResult>) {
                    if(task.isSuccessful){
                        val user = User(email, name, phone, userType(),location,espDistance,requestStatus) //create an object of User

                        //send the user object to the realtime database by getting the current user ID and passing it the user object
                        FirebaseAuth.getInstance().currentUser?.let {
                            FirebaseDatabase.getInstance().getReference("users")
                                .child(it.uid)
                                .setValue(user).addOnCompleteListener{
                                    //good practice to run addOnCompleteListener to check if the operation succeeded or failed

                                    if(it.isSuccessful){

                                        var currentUser = FirebaseAuth.getInstance().uid.toString()
                                        var typeDatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUser).child("type")

                                        typeDatabaseReference.addValueEventListener(object:
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                Log.i(LoginFragment.TAG,snapshot.value.toString())

                                                if(snapshot.value.toString()=="Client"){
                                                    onClickView.findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToClientFragment())
                                                } else {
                                                    onClickView.findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToProviderFragment())
                                                }

                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                            }

                                        })

                                        Toast.makeText(activity,"Successful registration",Toast.LENGTH_SHORT).show()
                                        binding.statusLoadingWheel.isVisible = false
                                    } else {
                                        Toast.makeText(activity,"Failure to register",Toast.LENGTH_SHORT).show()
                                        binding.statusLoadingWheel.isVisible = false
                                    }
                                }
                        }
                    } else {
                        Toast.makeText(activity,"Failure to register",Toast.LENGTH_SHORT).show()
                        binding.statusLoadingWheel.isGone
                    }
                }

            })
        }

    }

    private fun userType(): String {
        val radioGroup = binding.radioGroupSignUp
        val radioClient = binding.radioClient
        val radioProvider = binding.radioServiceProvider
        if (radioGroup.checkedRadioButtonId ==radioClient.id){
            return radioClient.text.toString()
        }
        else return radioProvider.text.toString()
    }

}