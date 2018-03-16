package dk.eboks.app.domain.models.protocol

data class LoginRequest(
        var App: AppInfo? = null,
        var user: UserInfo? = null
)
