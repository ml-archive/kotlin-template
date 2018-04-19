package dk.eboks.app.domain.models.formreply

import java.io.Serializable

data class ReplyForm (
    var inputs : List<FormInput> = ArrayList()
) : Serializable
