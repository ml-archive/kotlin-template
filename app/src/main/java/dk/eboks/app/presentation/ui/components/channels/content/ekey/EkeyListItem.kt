package dk.eboks.app.presentation.ui.components.channels.content.ekey

import dk.eboks.app.domain.models.channel.ekey.Ekey

sealed class EkeyListItem {
    data class Data(val data: Ekey):EkeyListItem()
    data class Header(val type: String): EkeyListItem()
}