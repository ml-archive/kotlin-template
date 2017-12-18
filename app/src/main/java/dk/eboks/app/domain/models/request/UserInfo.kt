package dk.eboks.app.domain.models.request

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

/**
 * Created by bison on 07/12/17.
 */
@Root(name = "User", strict = false)
data class UserInfo
(
    @field:Attribute var identity: String = "",
    @field:Attribute var identityType: String = "P",
    @field:Attribute var nationality: String = "",
    @field:Attribute var pincode: String = "",
    var activationCode : String = "",
    var loginDateTime : String = ""
)