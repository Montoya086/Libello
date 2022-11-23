package com.example.libello.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.libello.databinding.FragmentAddNoteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.*


class AddNoteFragment : Fragment(){
    private val args by navArgs<AddNoteFragmentArgs>()
    private var _binding: FragmentAddNoteBinding? = null
    private lateinit var database_users: DatabaseReference
    private lateinit var database_notes: DatabaseReference
    private lateinit var database_data: DatabaseReference
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
        database = FirebaseDatabase.getInstance().getReference()
        database_notes = database.child("Notes")
        database_users = database.child("Users")
        database_data = database.child("Data")
        val user = args.user
        binding.createNote.setOnClickListener{
            val title = binding.editTextTitle.text.toString()
            val desc = binding.editTextDescription.text.toString()
            val noteType = binding.radioGroup.checkedRadioButtonId
            if(title.isNotEmpty() && desc.isNotEmpty() && noteType != -1) {
                var current_id = 0

                //UPDATES CURRENT ID
                database_data.child("CurrentID").get().addOnSuccessListener {
                    current_id = it.value.toString().toInt() + 1
                    database_data.child("CurrentID").setValue((current_id))
                }

                //UPDATES NOTES
                database_notes.get().addOnSuccessListener {
                    database_notes.child(current_id.toString()).push()
                    val currentID = current_id.toString()
                    database_notes.child(currentID).child("Content").setValue("")
                    database_notes.child(currentID).child("Desc").setValue(desc)
                    database_notes.child(currentID).child("Title").setValue(title)
                    database_notes.child(currentID).child("ID").setValue(current_id)
                    database_notes.child(currentID).child("Owner").setValue(user.getMail())
                    var temp = ""
                    when(noteType){
                        // Market
                        2131231082 -> temp = "MARKET"
                        // Finance
                        2131231083 -> temp = "FINANCE"
                        // Personal
                        2131231084 -> temp = "PERSONAL"
                    }
                    database_notes.child(currentID).child("Type").setValue(temp)
                }

                //UPDATES SHAREDKEYS
                database_users.child(user.getMail()!!).child("SharedKeys").get()
                    .addOnSuccessListener {
                        database_users.child(user.getMail()!!).child("SharedKeys").child(current_id.toString()).setValue(current_id.toString())
                    }

                val action = AddNoteFragmentDirections.actionAddNoteFragmentToNoteListFragment(user)
                this.findNavController().navigate(action)
            }else{
                Toast.makeText(this.context, "Asegurese de llenar todos los campos", Toast.LENGTH_SHORT).show()
            }

        }
    }
}