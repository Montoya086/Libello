package com.example.libello.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.libello.R
import com.example.libello.databinding.FragmentEditNoteBinding
import com.google.firebase.database.*

class EditNoteFragment : Fragment() {
    private val args by navArgs<EditNoteFragmentArgs>()
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseNotes: DatabaseReference
    private lateinit var databaseUsers: DatabaseReference
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
        databaseNotes = FirebaseDatabase.getInstance().getReference("Notes")
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users")
        val id = args.noteID
        val owner = args.noteOwner
        val user = args.user
        val textListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempRef = snapshot.child(id)
                binding.editTextTextMultiLine.setText(
                    tempRef.child("Content").value.toString()
                )
                binding.nameText.setText(tempRef.child("Title").value.toString())
                binding.descText.setText(tempRef.child("Desc").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        databaseNotes.addValueEventListener(textListener)

        // TO SAVE NEW DATA
        binding.floatingActionButton.setOnClickListener {
            try {
                val tempRef = databaseNotes.child(id)
                tempRef.child("Content")
                    .setValue(binding.editTextTextMultiLine.text.toString())
                tempRef.child("Title").setValue(binding.nameText.text.toString())
                tempRef.child("Desc").setValue(binding.descText.text.toString())
                Toast.makeText(activity, "Cambios guardados", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(activity, "Ha ocurrido un error de tipo " + e, Toast.LENGTH_SHORT)
                    .show()
            }

        }

        // TO SHARE NOTE
        binding.floatingActionButtonAdd.setOnClickListener {
            //ONLY THE OWNER CAN SHARE
            Log.i("OWNER", owner)
            Log.i("MAIL", user.getMail()!!)
            if (owner == user.getMail()) {
                val builder = AlertDialog.Builder(this.context)
                val inflater = layoutInflater.inflate(R.layout.share_mail_layout, null)
                val editTex = inflater.findViewById<EditText>(R.id.mailEditText)
                with(builder) {
                    setTitle("Ingrese el correo al cual desea compartir")
                    //POSITIVE
                    setPositiveButton("Compartir") { dialog, which ->
                        val splitmail = editTex.text.toString().split(".")[0]
                        databaseUsers.child(splitmail).get().addOnSuccessListener {
                            if (it.exists() && splitmail != "") {
                                databaseUsers.child(splitmail).child("SharedKeys").child(id)
                                    .setValue(id)
                                Toast.makeText(
                                    activity,
                                    "El usuario ha sido agregado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Usuario no encontrado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    //NEGATIVE
                    setNegativeButton("Cancelar") { dialog, which ->

                    }
                    setView(inflater)
                    show()
                }
                //USER NOT FOUND
            } else {
                Toast.makeText(
                    activity,
                    "No tienes permiso de compartir esta nota",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}