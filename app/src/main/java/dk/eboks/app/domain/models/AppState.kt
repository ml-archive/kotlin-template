package dk.eboks.app.domain.models

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.login.*
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.MessageOpeningState


/**
 * Created by bison on 09-02-2018.
 */
data class AppState (
        var currentFolder : Folder? = null,
        var currentMessage : Message? = null,
        var currentViewerFileName : String? = null,
        var currentUser : User? = null,
        var currentSettings: UserSettings? = null,
        var impersoniateUser: SharedUser? = null,
        @Transient val openingState: MessageOpeningState = MessageOpeningState(),
        @Transient var verificationState: VerificationState? = null,
        val loginState: LoginState = LoginState(),
        @Transient var selectedFolders : List<Folder>? = null
)
