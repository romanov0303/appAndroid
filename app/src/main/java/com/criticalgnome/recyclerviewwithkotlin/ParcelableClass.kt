package com.criticalgnome.recyclerviewwithkotlin

import android.os.Parcel
import android.os.Parcelable

class ParcelableClass : Parcelable {

    public lateinit var listArr: List<ParcelableString>

    constructor(list: List<ParcelableString>){
        this.listArr = list
    }

    public constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(ParcelableString.CREATOR)
    )

    override fun describeContents(): Int {
        return 0
    }


    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeTypedList(this.listArr)
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ParcelableClass> {
            override fun createFromParcel(parcel: Parcel) = ParcelableClass(parcel)
            override fun newArray(size: Int) = arrayOfNulls<ParcelableClass>(size)
        }
    }

    fun getListProperties(): List<ParcelableString> {
        return this.listArr
    }

}