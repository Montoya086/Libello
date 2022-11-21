package com.example.libello.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.libello.databinding.FragmentSigninBinding
import com.example.libello.network.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SigninFragment : Fragment(){
    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSigninBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener{
            val action = SigninFragmentDirections.actionSigninFragmentToLoginFragment()
            this.findNavController().navigate(action)
        }
        binding.signinButton.setOnClickListener {
            // Reads the data of the editText
            val mail : String = binding.editTextTextEmailAddress.text.toString()
            val splitMail : String = mail.split(".")[0]
            val password: String = binding.editTextTextPassword.text.toString()

            // Checks if theres data input
            if(mail.isNotEmpty() && password.isNotEmpty()) {
                database = FirebaseDatabase.getInstance().getReference("Users")
                database.child(splitMail).get().addOnSuccessListener {
                    // Checks if the user exists and has valid credentials
                    if(!it.exists()){
                        database.child(splitMail).push()
                        database.child(splitMail).child("Mail").setValue(mail)
                        database.child(splitMail).child("Password").setValue(password)
                        val action = SigninFragmentDirections.actionSigninFragmentToNoteListFragment(User(splitMail, password))
                        this.findNavController().navigate(action)
                    }else{
                        Toast.makeText(this.context, "Este correo ya esta en uso", Toast.LENGTH_SHORT).show()
                    }
                }

            }else{
                Toast.makeText(this.context, "Ingrese sus credenciales", Toast.LENGTH_SHORT).show()
            }
        }
    }
}