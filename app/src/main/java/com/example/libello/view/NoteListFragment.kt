package com.example.libello.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libello.R
import com.example.libello.dataLayer.NoteListViewModel
import com.example.libello.databinding.FragmentNoteListBinding


class NoteListFragment : Fragment() {
    private val args by navArgs<NoteListFragmentArgs>()
    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteListViewModel: NoteListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.AllFilter) {
            noteListViewModel.getAllNotes(args.user!!)
        }
        if (id == R.id.FinanceFilter) {
            noteListViewModel.getFilterNotes(args.user!!, "FINANCE")
        }
        if (id == R.id.MarketFiler) {
            noteListViewModel.getFilterNotes(args.user!!, "MARKET")
        }
        if (id == R.id.PersonalFilter) {
            noteListViewModel.getFilterNotes(args.user!!, "PERSONAL")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteListViewModel = ViewModelProvider(this).get(NoteListViewModel::class.java)
        binding.addNote.setOnClickListener {
            val action =
                NoteListFragmentDirections.actionNoteListFragmentToAddNoteFragment(args.user!!)
            this.findNavController().navigate(action)
        }
        noteListViewModel.getAllNotes(args.user!!)
        val recyclerView = view.findViewById<RecyclerView>(R.id.noteListRv)
        //UPDATES RECYCLER VIEW
        noteListViewModel.notes.observe(viewLifecycleOwner, Observer {
            Log.i("RECYCLER", it.size.toString())
            recyclerView.layoutManager = LinearLayoutManager(view.context)
            recyclerView.adapter = NoteListAdapter(it, this.requireContext(), args.user!!, getString(R.string.noteListAdapterDesc))
            recyclerView.setHasFixedSize(true)
        })

    }
}