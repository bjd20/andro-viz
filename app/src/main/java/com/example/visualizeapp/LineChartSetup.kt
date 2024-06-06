package com.example.visualizeapp

import android.os.Parcel
import android.os.Parcelable
import com.github.mikephil.charting.data.Entry

data class LineChartSetup(
    val data:ArrayList<EntryPair>,
    val heads: EntryPair,
    override val desc: String?
) : ChartSetup {
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

    companion object CREATOR : Parcelable.Creator<LineChartSetup> {
        override fun createFromParcel(parcel: Parcel): LineChartSetup {
            return LineChartSetup(parcel)
        }

        override fun newArray(size: Int): Array<LineChartSetup?> {
            return arrayOfNulls(size)
        }
    }
}
