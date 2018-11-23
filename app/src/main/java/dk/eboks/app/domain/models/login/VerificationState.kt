package dk.eboks.app.domain.models.login

/**
 * Created by bison on 09-02-2018.
 */
data class VerificationState(
        var loginProviderId : String,
        var userBeingVerified: User? = null,
        var kspToken : String = "",
        var oldAccessToken : String? = null,
        var allowMigrateUserId : String? = null,
        var shouldMergeProfiles : Boolean = false,
        var signupVerification : Boolean = false

)
