package com.example.libello.dataLayer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.libello.network.User
import com.google.firebase.database.*

class NoteListViewModel(): ViewModel() {
    lateinit var database: DatabaseReference
    val notes:MutableLiveData<MutableList<Note>> by lazy{
        MutableLiveData<MutableList<Note>>()
    }
    fun getNotes(myUser: User){
        var text = ""
        val mail = myUser.getMail()!!
        database = FirebaseDatabase.getInstance().getReference()
        val textListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("PRUEBA18", "Entro al OnDataChange")
                val temp=mutableListOf<Note>()
                var noteReference = snapshot.child("Notes")
                var userReference = snapshot.child("Users").child(mail)
                val nKeys = userReference.child("SharedKeys").childrenCount
                for(i in 0 until nKeys){
                    var key = userReference.child("SharedKeys").child(i.toString()).value.toString()
                    //text = noteReference.child(key).child("Content").value.toString()
                    //Log.i("PRUEBA20", text)
                    var title= noteReference.child(key).child("Title").value.toString()
                    var desc =noteReference.child(key).child("Desc").value.toString()
                    var id = noteReference.child(key).child("ID").value.toString()
                    temp.add(Note(title,desc,id))
                }
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