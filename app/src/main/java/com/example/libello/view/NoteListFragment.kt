package com.example.libello.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libello.R
import com.example.libello.databinding.FragmentNoteListBinding
import com.example.libello.temp.NoteList
import com.google.firebase.database.DatabaseReference

class NoteListFragment : Fragment(){
    private val args by navArgs<NoteListFragmentArgs>()
    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addNote.setOnClickListener{
            val action = NoteListFragmentDirections.actionNoteListFragmentToAddNoteFragment(args.user!!)
            this.findNavController().navigate(action)
        }
        val noteList = NoteList(args.user!!).getNotes()
        val recyclerView = view.findViewById<RecyclerView>(R.id.noteListRv)
        recyclerView.layoutManager = LinearLayoutManager(view.context);
        recyclerView.adapter = NoteListAdapter(noteList)
        recyclerView.setHasFixedSize(true)
    }
}