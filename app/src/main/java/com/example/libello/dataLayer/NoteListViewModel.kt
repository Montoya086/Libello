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
                //FOR EACH SHARED KEY, CREATES A NOTE INSTANCE
                for(key in userReference.child("SharedKeys").children){
                    var title= noteReference.child(key.value.toString()).child("Title").value.toString()
                    var desc =noteReference.child(key.value.toString()).child("Desc").value.toString()
                    var id = noteReference.child(key.value.toString()).child("ID").value.toString()
                    var creator = noteReference.child(key.value.toString()).child("Owner").value.toString()
                    temp.add(Note(title,desc,id,creator))
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