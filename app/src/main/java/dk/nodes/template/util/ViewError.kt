package dk.nodes.template.util

import java.io.Serializable

data class ViewError(
    var title: String,
    var message: String,
    var code: Int = 400
) : Serializable
