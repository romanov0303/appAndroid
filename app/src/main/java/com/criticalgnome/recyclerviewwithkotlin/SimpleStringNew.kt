package com.criticalgnome.recyclerviewwithkotlin

import android.os.Parcel
import android.os.Parcelable

class SimpleStringNew : Parcelable {

    public var stringW: String? = "default"

    constructor(string: String){
        this.stringW = string
    }

    protected constructor(parcel: Parcel) :this(parcel.readString())

    override fun describeContents(): Int {
        return 0
    }

    public fun returnT(): String? {
        return this.stringW
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(this.stringW)
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<SimpleStringNew> {
            override fun createFromParcel(parcel: Parcel) = SimpleStringNew(parcel)
            override fun newArray(size: Int) = arrayOfNulls<SimpleStringNew>(size)
        }
    }
}
