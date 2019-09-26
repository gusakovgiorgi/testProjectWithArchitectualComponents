package com.gusakov.frogogo.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import java.util.*

class User(
    val id: Long,
    var firstName: String,
    var lastName: String,
    var email: String,
    var avatarUri: Uri,
    val created: Date,
    var updated: Date,
    var url: Uri
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(Uri::class.java.classLoader)!!,
        Date(parcel.readLong()),
        Date(parcel.readLong()),
        parcel.readParcelable(Uri::class.java.classLoader)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(email)
        parcel.writeParcelable(avatarUri, flags)
        parcel.writeLong(created.time)
        parcel.writeLong(updated.time)
        parcel.writeParcelable(url, flags)
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