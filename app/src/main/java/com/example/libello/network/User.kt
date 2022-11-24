package com.example.libello.network

import android.os.Parcel
import android.os.Parcelable

// Parcelable object for Fragment Navigation
class User(private val mail: String?) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString())

    fun getMail(): String? {
        return mail
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mail)
    }

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