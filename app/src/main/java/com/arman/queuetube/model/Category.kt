package com.arman.queuetube.model

import android.os.Parcel
import android.os.Parcelable
import com.google.api.services.youtube.model.VideoCategory

class Category(var id: String?, var name: String?) : Parcelable {

    constructor(videoCategory: VideoCategory) : this(
            videoCategory.id,
            videoCategory.snippet.title)

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }

}