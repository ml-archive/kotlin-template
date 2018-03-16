package dk.eboks.app.domain.models.protocol

data class AppInfo(
        var version: String = "",
        var os: String = "Android",
        var osVersion: String = "",
        var device: String = ""
)