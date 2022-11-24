package com.example.libello.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.libello.R
import com.example.libello.databinding.FragmentAddNoteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddNoteFragment : Fragment() {
    private val args by navArgs<AddNoteFragmentArgs>()
    private var _binding: FragmentAddNoteBinding? = null
    private lateinit var databaseUsers: DatabaseReference
    private lateinit var databaseNotes: DatabaseReference
    private lateinit var databaseData: DatabaseReference
    private lateinit var database: DatabaseReference
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        databaseNotes = database.child("Notes")
        databaseUsers = database.child("Users")
        databaseData = database.child("Data")
        val user = args.user
        binding.createNote.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val desc = binding.editTextDescription.text.toString()
            val noteType = binding.radioGroup.checkedRadioButtonId
            // Checks if data was input
            if (title.isNotEmpty() && desc.isNotEmpty() && noteType != -1) {
                var currentId = 0

                //UPDATES CURRENT ID
                databaseData.child("CurrentID").get().addOnSuccessListener {
                    currentId = it.value.toString().toInt() + 1
                    databaseData.child("CurrentID").setValue((currentId))
                }

                //UPDATES NOTES
                databaseNotes.get().addOnSuccessListener {
                    databaseNotes.child(currentId.toString()).push()
                    val currentID = currentId.toString()
                    databaseNotes.child(currentID).child("Content").setValue("")
                    databaseNotes.child(currentID).child("Desc").setValue(desc)
                    databaseNotes.child(currentID).child("Title").setValue(title)
                    databaseNotes.child(currentID).child("ID").setValue(currentId)
                    databaseNotes.child(currentID).child("Owner").setValue(user.getMail())
                    var temp = ""
                    when (noteType) {
                        // Market
                        2131231082 -> temp = "MARKET"
                        // Finance
                        2131231083 -> temp = "FINANCE"
                        // Personal
                        2131231084 -> temp = "PERSONAL"
                    }
                    databaseNotes.child(currentID).child("Type").setValue(temp)
                }

                //UPDATES SHAREDKEYS
                databaseUsers.child(user.getMail()!!).child("SharedKeys").get()
                    .addOnSuccessListener {
                        databaseUsers.child(user.getMail()!!).child("SharedKeys")
                            .child(currentId.toString()).setValue(currentId.toString())
                    }

                val action = AddNoteFragmentDirections.actionAddNoteFragmentToNoteListFragment(user)
                this.findNavController().navigate(action)
            } else {
                Toast.makeText(
                    this.context,
                    R.string.toastFillNoteData,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}