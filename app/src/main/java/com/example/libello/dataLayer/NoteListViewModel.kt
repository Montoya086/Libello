package com.example.libello.dataLayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.libello.network.User
import com.google.firebase.database.*

class NoteListViewModel : ViewModel() {
    lateinit var database: DatabaseReference
    val notes: MutableLiveData<MutableList<Note>> by lazy {
        MutableLiveData<MutableList<Note>>()
    }

    /**
     *  Gets all the notes for the specified user
     */
    fun getAllNotes(myUser: User) {
        val mail = myUser.getMail()!!
        database = FirebaseDatabase.getInstance().reference
        val textListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = mutableListOf<Note>()
                val noteReference = snapshot.child("Notes")
                val userReference = snapshot.child("Users").child(mail)
                //FOR EACH SHARED KEY, CREATES A NOTE INSTANCE
                for (key in userReference.child("SharedKeys").children) {
                    val tempReference = noteReference.child(key.value.toString())
                    val title =
                        tempReference.child("Title").value.toString()
                    val desc =
                        tempReference.child("Desc").value.toString()
                    val id = tempReference.child("ID").value.toString()
                    val creator =
                        tempReference.child("Owner").value.toString()
                    temp.add(Note(title, desc, id, creator))
                }
                notes.value = temp
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        database.addValueEventListener(textListener)
    }

    /**
     * Gets all the filtered notes for the specified user
     */
    fun getFilterNotes(myUser: User, filter: String) {
        val mail = myUser.getMail()!!
        database = FirebaseDatabase.getInstance().reference
        val textListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = mutableListOf<Note>()
                val noteReference = snapshot.child("Notes")
                val userReference = snapshot.child("Users").child(mail)
                //FOR EACH SHARED KEY, CREATES A NOTE INSTANCE
                for (key in userReference.child("SharedKeys").children) {
                    val tempReference = noteReference.child(key.value.toString())
                    if (tempReference.child("Type").value.toString() == filter) {
                        val title = tempReference.child("Title").value.toString()
                        val desc = tempReference.child("Desc").value.toString()
                        val id = tempReference.child("ID").value.toString()
                        val creator = tempReference.child("Owner").value.toString()
                        temp.add(Note(title, desc, id, creator))
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