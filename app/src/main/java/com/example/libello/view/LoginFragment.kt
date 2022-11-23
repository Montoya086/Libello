package com.example.libello.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.libello.databinding.FragmentLoginBinding
import com.example.libello.network.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

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
        firebaseAuth = FirebaseAuth.getInstance()
        //MAIL AUTH
        binding.loginButton.setOnClickListener{
            // Reads the data of the editText
            val mail: String = binding.editTextTextEmailAddress.text.toString()
            val split_mail : String = mail.split(".")[0]
            val password: String = binding.editTextTextPassword.text.toString()

            // Checks if theres data input
            if(mail.isNotEmpty() && password.isNotEmpty()) {
                database = FirebaseDatabase.getInstance().getReference("Users")
                database.child(mail).get().addOnSuccessListener {
                    // Checks if the user exists and has valid credentials
                    if(it.exists() && it.child("Password").value.toString() == password){
                        firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener {
                            if(it.isSuccessful){
                                val action = LoginFragmentDirections.actionLoginFragmentToNoteListFragment(User(split_mail, password))
                                binding.editTextTextPassword.text.clear()
                                binding.editTextTextEmailAddress.text.clear()
                                this.findNavController().navigate(action)
                            }else{
                                Toast.makeText(this.context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        Toast.makeText(this.context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }

            }else{
                Toast.makeText(this.context, "Ingrese sus credenciales", Toast.LENGTH_SHORT).show()
            }
        }

        //to login fragment
        binding.signinButton.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSigninFragment()
            this.findNavController().navigate(action)
        }
    }
}