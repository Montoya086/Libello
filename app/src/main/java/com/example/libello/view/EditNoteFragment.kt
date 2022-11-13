package com.example.libello.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.libello.databinding.FragmentEditNoteBinding
import com.google.firebase.database.*

class EditNoteFragment : Fragment() {
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private var text: String = "Mondongo"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TO READ DATA
        database = FirebaseDatabase.getInstance().getReference("Notes")

        val textListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                text = snapshot.child("2").child("Content").value.toString()
                binding.editTextTextMultiLine.setText(text)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        database.addValueEventListener(textListener)

        // TO SAVE NEW DATA
        binding.floatingActionButton.setOnClickListener{
            text = binding.editTextTextMultiLine.text.toString()
            database.child("2").child("Content").setValue(text)
        }
    }
}