package com.me.ml.webview.bean

import android.content.Intent
import android.os.Parcel
import android.os.Parcelable

class ActivityResultBean(
    val mResultCode: Int,
    val mRequestCode: Int,
    val mData: Intent?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        if (parcel.readInt() == 0) null else Intent.CREATOR.createFromParcel(parcel)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(mResultCode)
        parcel.writeInt(mRequestCode)
        parcel.writeParcelable(mData, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ActivityResultBean> {
        override fun createFromParcel(parcel: Parcel): ActivityResultBean {
            return ActivityResultBean(parcel)
        }

        override fun newArray(size: Int): Array<ActivityResultBean?> {
            return arrayOfNulls(size)
        }
    }
}
