package dk.eboks.app.domain.models.protocol

data class UserInfo
(
        var identity: String = "",
        var identityType: String = "P",
        var nationality: String = "",
        var pincode: String = "",
        var activationCode: String = "",
        var loginDateTime: String = ""
)