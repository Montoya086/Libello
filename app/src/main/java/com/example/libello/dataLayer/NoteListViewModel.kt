package com.example.libello.dataLayer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.libello.network.User
import com.google.firebase.database.*

class NoteListViewModel(): ViewModel() {

    val notes:MutableLiveData<MutableList<Note>> by lazy{
        MutableLiveData<MutableList<Note>>()
    }
    fun getNotes(myUser: User){
        lateinit var database: DatabaseReference
        var text = ""
        val mail = myUser.getMail()!!
        database = FirebaseDatabase.getInstance().getReference("Notes")
        val textListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp=mutableListOf<Note>()
                Log.i("PRUEBA18", "Entro al OnDataChange")
                text = snapshot.child("2").child("Content").value.toString()
                Log.i("PRUEBA20", text)
                temp.add(Note(snapshot.child("2").child("Title").value.toString(), snapshot.child("2").child("Desc").value.toString()))
                notes.value =temp
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        Log.i("PRUEBA28", text)
        database.addValueEventListener(textListener)
        Log.i("PRUEBA30", text)
    }
}