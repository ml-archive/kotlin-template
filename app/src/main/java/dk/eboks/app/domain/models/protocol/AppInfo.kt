package dk.eboks.app.domain.models.protocol

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

/**
 * Created by bison on 07/12/17.
 */
@Root(name = "App", strict = false)
data class AppInfo(
    @field:Attribute var version: String = "",
    @field:Attribute var os: String = "Android",
    @field:Attribute var osVersion: String = "",
    @field:Attribute var device: String = ""
)