package com.example.libello.view

import android.content.ContentValues.TAG
import android.content.Intent
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SigninFragment : Fragment(){
    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var  googleSignInClient: GoogleSignInClient

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
        firebaseAuth = FirebaseAuth.getInstance()

        //to Singup fragment
        binding.loginButton.setOnClickListener{
            val action = SigninFragmentDirections.actionSigninFragmentToLoginFragment()
            this.findNavController().navigate(action)
        }

        //MAIL AUTH
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
                        firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener {
                            if(it.isSuccessful){
                                database.child(splitMail).push()
                                database.child(splitMail).child("Mail").setValue(mail)
                                //database.child(splitMail).child("Password").setValue(password)
                                val action = SigninFragmentDirections.actionSigninFragmentToNoteListFragment(User(splitMail))
                                this.findNavController().navigate(action)
                            }else{
                                Toast.makeText(this.context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        Toast.makeText(this.context, "Este correo ya esta en uso", Toast.LENGTH_SHORT).show()
                    }
                }

            }else{
                Toast.makeText(this.context, "Ingrese sus credenciales", Toast.LENGTH_SHORT).show()
            }
        }

        //Google auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("231816380784-h6vk0o0kpk0jkatrrifsv0b5fp4icfoh.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this.requireContext(),gso)
        binding.loginWithGoogleButton.setOnClickListener {
            signIn()
            googleSignInClient.signOut()
        }
    }

    private fun signIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                var splitMail = account.email.toString().split(".")[0]
                                database = FirebaseDatabase.getInstance().getReference("Users")
                                database.child(splitMail).get().addOnSuccessListener {
                                    if(it.exists()){
                                        val action = SigninFragmentDirections.actionSigninFragmentToNoteListFragment(User(splitMail))
                                        this.findNavController().navigate(action)
                                    }else{
                                        database.child(splitMail).push()
                                        database.child(splitMail).child("Mail").setValue(account.email)
                                        //database.child(splitMail).child("Password").setValue(password)
                                        val action = SigninFragmentDirections.actionSigninFragmentToNoteListFragment(User(splitMail))
                                        this.findNavController().navigate(action)
                                    }
                                }
                            }
                        }
                }
            }catch (e:ApiException){
                Toast.makeText(this.context, "Ha ocurrido un error de tipo "+e, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object{
        const val  RC_SIGN_IN = 1001
    }
}