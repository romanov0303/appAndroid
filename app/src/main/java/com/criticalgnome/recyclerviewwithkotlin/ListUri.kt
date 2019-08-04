package com.criticalgnome.recyclerviewwithkotlin

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import java.net.URI

class ListUri : Parcelable{

    public var uriString: String = ""

    constructor(uri: String) {
        this.uriString = uri
    }

    private constructor(parcel: Parcel) : this(
        parcel.readString()
    )


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.uriString)
    }

    public fun getUri(): String {
        return this.uriString
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ListUri> {
            override fun createFromParcel(parcel: Parcel) = ListUri(parcel)
            override fun newArray(size: Int) = arrayOfNulls<ListUri>(size)
        }
    }
}