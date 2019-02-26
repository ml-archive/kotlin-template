package dk.eboks.app.domain.config

/**
 * Created by joso on 11/08/15.
 */
data class Environments(
    var apiUrl: String = "",
    var authUrl: String = "",
    var kspUrl: String = "",
    var shortAuthId: String = "",
    var shortAuthSecret: String = "",
    var shortAuthCustomId: String = "",
    var shortAuthCustomSecret: String = "",
    var longAuthId: String = "",
    var longAuthSecret: String = "",
    var longAuthCustomId: String = "",
    var longAuthCustomSecret: String = ""
)
