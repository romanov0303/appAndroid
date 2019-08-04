package com.criticalgnome.recyclerviewwithkotlin

import android.os.Parcel
import android.os.Parcelable

class ParcelableString : Parcelable {

    public var groupName: String = ""
    public var rating: String = ""

    constructor(key: String, value: String) {
        this.groupName = key
        this.rating = value
    }

    private constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()
    )

    override fun describeContents(): Int {
        return 0
    }

    public fun getGroup(): String {
        return this.groupName
    }

    public fun getRatingVal(): String {
        return this.rating
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.groupName)
        dest?.writeString(this.rating)
    }



    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ParcelableString> {
            override fun createFromParcel(parcel: Parcel) = ParcelableString(parcel)
            override fun newArray(size: Int) = arrayOfNulls<ParcelableString>(size)
        }
    }
}