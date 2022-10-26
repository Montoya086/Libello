package com.example.libello.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.libello.databinding.FragmentSigninBinding

class SigninFragment : Fragment(){
    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!
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
            val action = SigninFragmentDirections.actionSigninFragmentToNoteListFragment()
            this.findNavController().navigate(action)
        }
    }
}