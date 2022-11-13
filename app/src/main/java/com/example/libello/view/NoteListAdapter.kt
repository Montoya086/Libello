package com.example.libello.view
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.libello.databinding.ItemNoteViewBinding
import com.example.libello.dataLayer.Note

class NoteListAdapter(private val noteList: MutableList<Note>) : RecyclerView.Adapter<NoteListAdapter.NoteListHolder>(){
    inner class NoteListHolder(val binding: ItemNoteViewBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListHolder {
        val binding = ItemNoteViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteListHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteListHolder, position: Int) {
        val note = noteList[position]
        holder.binding.textViewTitle.text = note.name
        holder.binding.textViewDescription.text = note.desc
        holder.binding.textViewDescriptionTitle.text = "Descripción de la nota "+note.name
        holder.itemView.setOnClickListener {
            val action = NoteListFragmentDirections.actionNoteListFragmentToEditNoteFragment(note.name)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount()=noteList.size

}