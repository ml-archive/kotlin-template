package dk.eboks.app.domain.models.request

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root

/**
 * Created by bison on 07/12/17.
 */
@Root(name = "Logon", strict = false)
@NamespaceList(
    Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
    Namespace(reference = "urn:eboks:mobile:1.0.0")
)
data class LoginRequest(
    @field:Element(name = "App", required = false)
    var App : AppInfo? = null,
    @field:Element(name = "User", required = false) var user : UserInfo? = null
)
