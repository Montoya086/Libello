package com.example.libello.view

//Librerias
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.libello.R
import com.example.libello.databinding.FragmentLoginBinding
import com.example.libello.network.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
*-------------------------------------------
* LoginFragment
*-------------------------------------------
* Descripción: Fragmento para el ingreso
* de usuarios ya registrados en el sistema.
*-------------------------------------------
*/
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    /**
    * onCreateView
    * Define el Binding, inflater, container y savedInstanceState.
    */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    /**
    * onViewCreated
    * Inicializa el ciclo de vida del fragment.
    * Parametros: view, savedInstance
    */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        //MAIL AUTH
        binding.loginButton.setOnClickListener {
            // Reads the data of the editText
            val mail: String = binding.editTextTextEmailAddress.text.toString()
            val splitMail: String = mail.split(".")[0]
            val password: String = binding.editTextTextPassword.text.toString()

            // Checks if theres data input
            if (mail.isNotEmpty() && password.isNotEmpty()) {
                database = FirebaseDatabase.getInstance().getReference("Users")
                database.child(splitMail).get().addOnSuccessListener {
                    // Checks if the user exists and has valid credentials
                    firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val userVerification = firebaseAuth.currentUser?.isEmailVerified
                            if (userVerification == true) {
                                val action =
                                    LoginFragmentDirections.actionLoginFragmentToNoteListFragment(
                                        User(splitMail)
                                    )
                                binding.editTextTextPassword.text.clear()
                                binding.editTextTextEmailAddress.text.clear()
                                firebaseAuth.signOut()
                                this.findNavController().navigate(action)
                            } else {
                                Toast.makeText(
                                    this.context,
                                    R.string.toastUserNotVerified,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this.context,
                                it.exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this.context, R.string.toastAskCredentials, Toast.LENGTH_SHORT).show()
            }
        }

        //to login fragment
        binding.signinButton.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSigninFragment()
            this.findNavController().navigate(action)
        }

        //Google auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("231816380784-h6vk0o0kpk0jkatrrifsv0b5fp4icfoh.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this.requireContext(), gso)
        binding.loginWithGoogleButton.setOnClickListener {
            signIn()
            googleSignInClient.signOut()
        }
    }

    /**
    * onViewCreated
    * Inicializa el ciclo de vida del fragment.
    * Parametros: view, savedInstance
    */
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    /**
    * onActivityResult
    * Resultado del ingreso del usuario.
    * Autenticacion de credenciales.
    * Parametros: requestCode, resultCode, data
    */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { it ->
                            if (it.isSuccessful) {
                                val splitMail = account.email.toString().split(".")[0]
                                database = FirebaseDatabase.getInstance().getReference("Users")
                                database.child(splitMail).get().addOnSuccessListener {
                                    if (it.exists()) {
                                        val action =
                                            LoginFragmentDirections.actionLoginFragmentToNoteListFragment(
                                                User(splitMail)
                                            )
                                        this.findNavController().navigate(action)
                                    } else {
                                        database.child(splitMail).push()
                                        database.child(splitMail).child("Mail")
                                            .setValue(account.email)
                                        //database.child(splitMail).child("Password").setValue(password)
                                        val action =
                                            LoginFragmentDirections.actionLoginFragmentToNoteListFragment(
                                                User(splitMail)
                                            )
                                        this.findNavController().navigate(action)
                                    }
                                }
                            }
                        }
                }
            } catch (e: ApiException) {
                Toast.makeText(
                    this.context,
                    R.string.toastErrorOccurred.toString() + e,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val RC_SIGN_IN = 1001
    }
}