package com.example.visualizeapp

import android.os.Parcel
import android.os.Parcelable
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry

data class PieChartSetup(
    val data:ArrayList<EntryPair>,
    val heads: EntryPair,
    override val desc: String?
) :ChartSetup {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(EntryPair.CREATOR) ?: arrayListOf(),
        parcel.readTypedObject(EntryPair.CREATOR)  ?: EntryPair("", ""),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(data)
        parcel.writeParcelable(heads, flags)
        parcel.writeString(desc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PieChartSetup> {
        override fun createFromParcel(parcel: Parcel): PieChartSetup {
            return PieChartSetup(parcel)
        }

        override fun newArray(size: Int): Array<PieChartSetup?> {
            return arrayOfNulls(size)
        }
    }
}
