package com.criticalgnome.recyclerviewwithkotlin

import android.os.Parcel
import android.os.Parcelable

data class MainItem(
        var firstName: String,
        var value: String
) : Parcelable {

    private constructor(parcel: Parcel): this(
            parcel.readString(),
            parcel.readString()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(firstName)
        dest?.writeString(value)
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<MainItem> {
            override fun createFromParcel(parcel: Parcel) = MainItem(parcel)
            override fun newArray(size: Int) = arrayOfNulls<MainItem>(size)
        }
    }
}