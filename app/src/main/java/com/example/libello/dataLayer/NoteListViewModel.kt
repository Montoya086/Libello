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
    fun getAllNotes(myUser: User){
        val text = ""
        val mail = myUser.getMail()!!
        database = FirebaseDatabase.getInstance().reference
        val textListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("PRUEBA18", "Entro al OnDataChange")
                val temp=mutableListOf<Note>()
                val noteReference = snapshot.child("Notes")
                val userReference = snapshot.child("Users").child(mail)
                //FOR EACH SHARED KEY, CREATES A NOTE INSTANCE
                for(key in userReference.child("SharedKeys").children){
                    val title= noteReference.child(key.value.toString()).child("Title").value.toString()
                    val desc =noteReference.child(key.value.toString()).child("Desc").value.toString()
                    val id = noteReference.child(key.value.toString()).child("ID").value.toString()
                    val creator = noteReference.child(key.value.toString()).child("Owner").value.toString()
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

    fun getFilterNotes(myUser: User, filter: String){
        val mail = myUser.getMail()!!
        database = FirebaseDatabase.getInstance().reference
        val textListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp=mutableListOf<Note>()
                val noteReference = snapshot.child("Notes")
                val userReference = snapshot.child("Users").child(mail)
                //FOR EACH SHARED KEY, CREATES A NOTE INSTANCE
                for(key in userReference.child("SharedKeys").children){
                    val tempReference = noteReference.child(key.value.toString())
                    if(tempReference.child("Type").value.toString() == filter){
                        val title= tempReference.child("Title").value.toString()
                        val desc = tempReference.child("Desc").value.toString()
                        val id = tempReference.child("ID").value.toString()
                        val creator = tempReference.child("Owner").value.toString()
                        temp.add(Note(title,desc,id,creator))
                    }
                }
                notes.value = temp
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        database.addValueEventListener(textListener)
    }
}