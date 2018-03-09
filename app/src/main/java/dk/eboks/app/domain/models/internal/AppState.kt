package dk.eboks.app.domain.models.internal

import dk.eboks.app.domain.models.Folder
import dk.eboks.app.domain.models.Message
import dk.eboks.app.domain.models.channel.ChannelState

/**
 * Created by bison on 09-02-2018.
 */
data class AppState (
    var currentFolder : Folder? = null,
    var currentMessage : Message? = null,
    var currentViewerFileName : String? = null,
    @Transient val openingState: MessageOpeningState = MessageOpeningState(),
    @Transient val channelState: ChannelState = ChannelState()
)
