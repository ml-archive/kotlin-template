package dk.eboks.app.domain.models

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.login.LoginState
import dk.eboks.app.domain.models.message.MessageOpeningState
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.domain.models.login.UserSettings
import dk.eboks.app.domain.models.login.VerificationState


/**
 * Created by bison on 09-02-2018.
 */
data class AppState (
        var currentFolder : Folder? = null,
        var currentMessage : Message? = null,
        var currentViewerFileName : String? = null,
        var currentUser : User? = null,
        var currentSettings: UserSettings? = null,
        @Transient val openingState: MessageOpeningState = MessageOpeningState(),
        @Transient var verificationState: VerificationState? = null,
        val loginState: LoginState = LoginState()

)
