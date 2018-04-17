package dk.eboks.app.domain.models.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by bison on 09-02-2018.
 */

@Parcelize
data class LoginInfo(
        var type: LoginInfoType = LoginInfoType.EMAIL,
        var socialSecurity: String = "",
        var password: String = ""
) : Parcelable {
    companion object {
        val KEY = "LoginInfo"
    }
}
