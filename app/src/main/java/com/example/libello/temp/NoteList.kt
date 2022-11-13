package com.example.libello.temp

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NoteList() {
    fun getNotes(): List<Note>{

        return listOf(
            Note("Nota 1", "Descripción de la nota 1"),
            Note("Nota 2", "Descripción de la nota 2"),
            Note("Nota 3", "Descripción de la nota 3"),
            Note("Nota 4", "Descripción de la nota 4"),
            Note("Nota 5", "Descripción de la nota 5"),
            Note("Nota 6", "Descripción de la nota 6"),
            Note("Nota 7", "Descripción de la nota 7"),
            Note("Nota 8", "Descripción de la nota 8"),
            Note("Nota 9", "Descripción de la nota 9"),
            Note("Nota 10", "Descripción de la nota 10"),
            Note("Nota 11", "Descripción de la nota 11"),
            Note("Nota 12", "Descripción de la nota 12"),
            Note("Nota 13", "Descripción de la nota 13"),
            Note("Nota 14", "Descripción de la nota 14"),
            Note("Nota 15", "Descripción de la nota 15"),
            Note("Nota 16", "Descripción de la nota 16"),
            Note("Nota 17", "Descripción de la nota 17"),
            Note("Nota 18", "Descripción de la nota 18"),
            Note("Nota 19", "Descripción de la nota 19"),
        )
    }
}