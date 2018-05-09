package dk.eboks.app.domain.models.channel.ekey

import android.os.Parcel
import android.os.Parcelable


sealed class Ekey(
        open val name : String,
        open val note: String?
) : Parcelable

data class Login(var username: String, var password: String, override val name: String, override val note: String?) : Parcelable, Ekey(name, note) {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        //        super.writeToParcel(parcel, flags)
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(name)
        parcel.writeString(note)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Login> {
        override fun createFromParcel(parcel: Parcel): Login {
            return Login(parcel)
        }

        override fun newArray(size: Int): Array<Login?> {
            return arrayOfNulls(size)
        }
    }
}

data class Note(override val name: String, override val note: String?) : Ekey(name, note) {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        super.writeToParcel(parcel, flags)
        parcel.writeString(name)
        parcel.writeString(note)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}

data class Pin(var pin: String, override val name: String, override val note: String?) : Parcelable,Ekey(name, note) {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
//        super.writeToParcel(parcel, flags)
        dest!!.writeString(pin)
        dest.writeString(name)
        dest.writeString(note)
    }

    companion object CREATOR : Parcelable.Creator<Pin> {
        override fun createFromParcel(parcel: Parcel): Pin {
            return Pin(parcel)
        }

        override fun newArray(size: Int): Array<Pin?> {
            return arrayOfNulls(size)
        }
    }
}

