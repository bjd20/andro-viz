package com.example.visualizeapp

import android.os.Parcel
import android.os.Parcelable

data class EntryPair(val first: String, val second: String) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun describeContents(): Int {
        return 0;
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(first)
        dest.writeString(second)
    }

    companion object CREATOR : Parcelable.Creator<EntryPair> {
        override fun createFromParcel(parcel: Parcel): EntryPair {
            return EntryPair(parcel)
        }

        override fun newArray(size: Int): Array<EntryPair?> {
            return arrayOfNulls(size)
        }
    }

}
