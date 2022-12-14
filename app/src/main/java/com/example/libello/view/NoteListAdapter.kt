package com.example.libello.view

//Librerias
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.libello.R
import com.example.libello.dataLayer.Note
import com.example.libello.databinding.ItemNoteViewBinding
import com.example.libello.network.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
*-------------------------------------------
* NoteListAdapter
*-------------------------------------------
* Descripción: Adapter para la lista de Notas
* mostradas al usuario en la pagina de inicio,
* junto a toda su información.
*-------------------------------------------
*/
class NoteListAdapter(
    private val noteList: MutableList<Note>,
    private val c: Context,
    private val user: User,
    private val desc_string: String
) : RecyclerView.Adapter<NoteListAdapter.NoteListHolder>() {
    private lateinit var database: DatabaseReference

    inner class NoteListHolder(val binding: ItemNoteViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
    * onCreateViewHolder
    * Infla la vista con la informacion de las notas.
    * Parametros: parent, viewGroup
    */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListHolder {
        val binding =
            ItemNoteViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteListHolder(binding)
    }

    /**
    * onBindViewHolder
    * Visualizacion y acceso de las notas.
    * Parametros: holder, position
    */
    override fun onBindViewHolder(holder: NoteListHolder, position: Int) {
        val note = noteList[position]
        database = FirebaseDatabase.getInstance().reference
        holder.binding.textViewTitle.text = note.name
        holder.binding.textViewDescription.text = note.desc
        holder.binding.textViewDescriptionTitle.text = desc_string
        holder.itemView.setOnClickListener {
            val action = NoteListFragmentDirections.actionNoteListFragmentToEditNoteFragment(
                note.id, noteList[position].creator, user
            )
            holder.itemView.findNavController().navigate(action)
        }
        //Recycler view on hold:
        holder.itemView.setOnLongClickListener {
            val popupMenu = PopupMenu(c, holder.itemView)
            popupMenu.inflate(R.menu.context_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.deleteElement -> {
                        //Delete the permission to edit the note
                        database.child("Users").child(user.getMail()!!).child("SharedKeys").get()
                            .addOnSuccessListener {
                                for (key in it.children) {
                                    if (it.child(key.value.toString()).value.toString() == note.id) {
                                        database.child("Users").child(user.getMail()!!)
                                            .child("SharedKeys").child(key.value.toString())
                                            .removeValue()
                                    }
                                }
                            }
                        //Delete the note itself (Only if the owner delete it)

                        database.child("Notes").child(note.id).get().addOnSuccessListener {
                            if (user.getMail() == it.child("Owner").value.toString()) {
                                database.child("Notes").child(note.id).removeValue()
                                database.child("Users").get().addOnSuccessListener {
                                    for (user in it.children) {
                                        database.child("Users").child(
                                            user.child("Mail").value.toString().split(".")[0]
                                        ).child("SharedKeys").child(note.id).removeValue()
                                    }
                                }

                            }
                        }
                        true
                    }
                    else -> true
                }
            }
            popupMenu.show()
            true
        }
    }

    /**
    * onBindViewHolder
    * Devuelve el tamanio de la lista, el numero de notas.
    */
    override fun getItemCount() = noteList.size
}