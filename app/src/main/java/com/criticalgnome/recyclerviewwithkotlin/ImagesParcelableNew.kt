package com.criticalgnome.recyclerviewwithkotlin

import android.os.Parcel
import android.os.Parcelable

class ImagesParcelableNew : Parcelable {

    public var images = mutableListOf<String>()

    constructor(list: MutableList<String>) {
        this.images = list
    }

    protected constructor(parcel: Parcel) :this(
        parcel.createStringArrayList()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeStringList(this.images)
    }

    public fun getListParams(): MutableList<String>{
        return this.images
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ImagesParcelableNew> {
            override fun createFromParcel(parcel: Parcel) = ImagesParcelableNew(parcel)
            override fun newArray(size: Int) = arrayOfNulls<ImagesParcelableNew>(size)
        }
    }

}
