package com.example.libello.temp

import android.util.Log
import com.example.libello.network.User
import com.google.firebase.database.*

class NoteList(myUser: User) {
    private lateinit var database: DatabaseReference
    private val mail = myUser.getMail()!!
    private var text: String = "Mondongo"

    fun getNotes(): List<Note>{
        val notes = mutableListOf<Note>()
        database = FirebaseDatabase.getInstance().getReference("Notes")

        val textListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("PRUEBA18", "Entro al OnDataChange")
                text = snapshot.child("2").child("Content").value.toString()
                Log.i("PRUEBA20", text)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        notes.add(Note(text, text))
        Log.i("PRUEBA28", text)
        database.addValueEventListener(textListener)
        Log.i("PRUEBA30", text)
        return notes.toList()
    }
}