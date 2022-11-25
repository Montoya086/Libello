package com.example.libello.network

//Librerias
import android.os.Parcel
import android.os.Parcelable

/**
*-------------------------------------------
* User
*-------------------------------------------
* Descripción: Usuario.
* Caracteristicas y funciones.
*-------------------------------------------
*/
// Parcelable object for Fragment Navigation
class User(private val mail: String?) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString())

    /*
    * getMail
    * Obtencion del correo electronico del usuario.
    * Return: mail
    */
    fun getMail(): String? {
        return mail
    }

    /*
    * writeToParcel
    * Definicion Parcelable.
    * Parametros: parcel, flags
    */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mail)
    }

    /*
    * describeContents
    * Definicion contenedores.
    * Return: 0
    */
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


}