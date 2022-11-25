package com.example.libello.view

//Librerias
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

/**
*-------------------------------------------
* EditNoteFragment
*-------------------------------------------
* Descripción: Fragmento para la edicion de
* las caracteristicas de una nota ya creada.
*
* La nota se actualiza automaticamente en
* todas las cuentas compartidas
*-------------------------------------------
*/
class EditNoteFragment : Fragment() {
    private val args by navArgs<EditNoteFragmentArgs>()
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseNotes: DatabaseReference
    private lateinit var databaseUsers: DatabaseReference

    /**
    * onCreateView
    * Define el Binding, inflater, container y savedInstanceState.
    */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
    * onViewCreated
    * Inicializa el ciclo de vida del fragment.
    * Parametros: view, savedInstance
    */
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
                Toast.makeText(activity, R.string.toastSavedChanges, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(activity, R.string.toastErrorOccurred.toString() + e, Toast.LENGTH_SHORT)
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
                    setTitle(R.string.shareNote)
                    //POSITIVE
                    setPositiveButton(R.string.shareNotePositiveButton) { dialog, which ->
                        val splitMail = editTex.text.toString().split(".")[0]
                        databaseUsers.child(splitMail).get().addOnSuccessListener {
                            if (it.exists() && splitMail != "") {
                                databaseUsers.child(splitMail).child("SharedKeys").child(id)
                                    .setValue(id)
                                Toast.makeText(
                                    activity,
                                    R.string.shareNoteUserAdded,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    activity,
                                    R.string.shareNoteUserNotFound,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    //NEGATIVE
                    setNegativeButton(R.string.shareNoteNegativeButton) { dialog, which ->

                    }
                    setView(inflater)
                    show()
                }
                //USER NOT FOUND
            } else {
                Toast.makeText(
                    activity,
                    R.string.shareNotePermissionToShareDenied,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}