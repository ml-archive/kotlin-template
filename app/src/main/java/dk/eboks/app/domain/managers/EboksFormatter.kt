package dk.eboks.app.domain.managers

import dk.eboks.app.domain.models.channel.StoreboxReceipt
import dk.eboks.app.domain.models.home.Item
import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message
import java.util.*

/**
 * Created by bison on 19/02/18.
 */
interface EboksFormatter
{
    fun formatDate(target : Message) : String
    fun formatDateRelative(target : Message) : String
    fun formatDateRelative(target : StoreboxReceipt) : String
    fun formatDateRelative(target : Item) : String
    fun formatSize(target : Content) : String
    fun formatCpr(cpr: String) : String
}