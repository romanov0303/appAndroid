package com.criticalgnome.recyclerviewwithkotlin

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

class ImagesParcelable : Parcelable {

    public var list: MutableList<ListUri>? = null

    constructor(images: MutableList<ListUri>) {
        this.list = images
    }

    private constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(ListUri.CREATOR)
    )

    public fun getListParams(): MutableList<ListUri>? {
        return this.list
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeList(this.list)
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ImagesParcelable> {
            override fun createFromParcel(parcel: Parcel) = ImagesParcelable(parcel)
            override fun newArray(size: Int) = arrayOfNulls<ImagesParcelable>(size)
        }
    }

}