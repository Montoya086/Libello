package com.example.libello.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.libello.databinding.FragmentLoginBinding
import com.example.libello.network.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener{
            // Reads the data of the editText
            val mail : String = binding.editTextTextEmailAddress.text.toString().split(".")[0]
            val password: String = binding.editTextTextPassword.text.toString()

            // Checks if theres data input
            if(mail.isNotEmpty() && password.isNotEmpty()) {
                database = FirebaseDatabase.getInstance().getReference("Users")
                database.child(mail).get().addOnSuccessListener {
                    // Checks if the user exists and has valid credentials
                    if(it.exists() && it.child("Password").value.toString() == password){
                        val action = LoginFragmentDirections.actionLoginFragmentToNoteListFragment(User(mail, password))
                        this.findNavController().navigate(action)
                    }else{
                        Toast.makeText(this.context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }

            }else{
                Toast.makeText(this.context, "Ingrese sus credenciales", Toast.LENGTH_SHORT).show()
            }
        }
        binding.signinButton.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSigninFragment()
            this.findNavController().navigate(action)
        }
    }
}