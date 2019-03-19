package dk.eboks.app.domain.models.formreply

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReplyForm(
    var inputs: List<FormInput> = ArrayList()
) : Parcelable

fun ReplyForm.toOutputForm(): ReplyFormOutput {
    return ReplyFormOutput(inputs.filter { it.name != null }.map {
        FormOutput(
            it.name!!,
            it.value!!
        )
    })
}
