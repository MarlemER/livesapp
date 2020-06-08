package com.aptivist.livesapp.model

import android.os.Parcel
import android.os.Parcelable

data class UserData (
    var uid: String?,
    var userUser: String?,
    var emailUser: String?,
    var passwordUser: String?,
    var isAuthenticated: Boolean?,
    var isNew: Boolean?,
    var isCreated: Boolean?,
    var photoUser:String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(userUser)
        parcel.writeString(emailUser)
        parcel.writeString(passwordUser)
        parcel.writeValue(isAuthenticated)
        parcel.writeValue(isNew)
        parcel.writeValue(isCreated)
        parcel.writeString(photoUser)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserData> {
        override fun createFromParcel(parcel: Parcel): UserData {
            return UserData(parcel)
        }

        override fun newArray(size: Int): Array<UserData?> {
            return arrayOfNulls(size)
        }
    }
}


