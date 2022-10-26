package com.example.libello.view
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.libello.databinding.ItemNoteViewBinding

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.NoteListHolder>(){
    inner class NoteListHolder(val binding: ItemNoteViewBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListHolder {
        val binding = ItemNoteViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteListHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteListHolder, position: Int) {
        holder.binding.noteName.text="Nota 1"
        holder.binding.noteDesc.text="Esta es la descripción de la nota de prueba"
        holder.itemView.setOnClickListener {
            val action = NoteListFragmentDirections.actionNoteListFragmentToEditNoteFragment()
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount()=1 //cambiar

}