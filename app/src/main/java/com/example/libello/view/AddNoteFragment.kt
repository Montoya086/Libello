package com.example.libello.view

//Librerias
import android.content.pm.ActivityInfo
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

/**
*-------------------------------------------
* AddNoteFragment
*-------------------------------------------
* Descripción: Fragmento para la creacion de
* una nueva nota junto a sus caracteristicas.
*-------------------------------------------
*/
class AddNoteFragment : Fragment() {
    private val args by navArgs<AddNoteFragmentArgs>()
    private var _binding: FragmentAddNoteBinding? = null
    private lateinit var databaseUsers: DatabaseReference
    private lateinit var databaseNotes: DatabaseReference
    private lateinit var databaseData: DatabaseReference
    private lateinit var database: DatabaseReference
    private val binding get() = _binding!!

    /**
    * onCreateView
    * Define el Binding, inflater, container y savedInstanceState.
    */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
    * onViewCreated
    * Inicializa el ciclo de vida del fragment.
    * Parametros: view, savedInstance
    */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
                        binding.radioButton1.id -> temp = "MARKET"
                        // Finance
                        binding.radioButton2.id -> temp = "FINANCE"
                        // Personal
                        binding.radioButton3.id -> temp = "PERSONAL"
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