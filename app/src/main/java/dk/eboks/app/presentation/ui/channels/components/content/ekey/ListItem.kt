package dk.eboks.app.presentation.ui.channels.components.content.ekey

import dk.eboks.app.domain.models.channel.ekey.BaseEkey
import dk.eboks.app.domain.models.channel.ekey.Ekey
import java.io.Serializable

/**
 * Created by Christian on 5/8/2018.
 * @author   Christian
 * @since    5/8/2018.
 */
abstract class ListItem: Serializable

data class Header(val text: String): ListItem()
data class EkeyItem(val data: BaseEkey): ListItem()