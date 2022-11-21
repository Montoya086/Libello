package com.example.libello.view
import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.PopupMenu
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.libello.R
import com.example.libello.databinding.ItemNoteViewBinding
import com.example.libello.dataLayer.Note
import com.example.libello.network.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NoteListAdapter(private val noteList: MutableList<Note>, val c: Context, val user:User) : RecyclerView.Adapter<NoteListAdapter.NoteListHolder>(){
    private lateinit var database: DatabaseReference
    inner class NoteListHolder(val binding: ItemNoteViewBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListHolder {
        val binding = ItemNoteViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteListHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteListHolder, position: Int) {
        val note = noteList[position]
        database = FirebaseDatabase.getInstance().getReference()
        holder.binding.textViewTitle.text = note.name
        holder.binding.textViewDescription.text = note.desc
        holder.binding.textViewDescriptionTitle.text = "Descripción de la nota \""+note.name+"\":"
        holder.itemView.setOnClickListener {
            val action = NoteListFragmentDirections.actionNoteListFragmentToEditNoteFragment(note.id)
            holder.itemView.findNavController().navigate(action)
        }
        holder.itemView.setOnLongClickListener{
            val popupMenu = PopupMenu(c,holder.itemView)
            popupMenu.inflate(R.menu.context_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId){
                    R.id.deleteElement->{
                        database.child("Users").child(user.getMail()!!).child("SharedKeys").get().addOnSuccessListener {
                            for (key in it.children) {
                                if(it.child(key.value.toString()).value.toString()==note.id) {
                                    database.child("Users").child(user.getMail()!!).child("SharedKeys").child(key.value.toString()).removeValue()
                                }
                            }
                        }
                        database.child("Notes").child(note.id).removeValue()
                        true
                    }
                    else->true
                }
            }
            popupMenu.show()
            true
        }
    }

    override fun getItemCount()=noteList.size

}