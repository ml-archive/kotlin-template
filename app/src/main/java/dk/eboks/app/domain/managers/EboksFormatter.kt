package dk.eboks.app.domain.managers

import dk.eboks.app.domain.models.channel.storebox.StoreboxReceiptItem
import dk.eboks.app.domain.models.home.Item
import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message
import java.util.Date

/**
 * Created by bison on 19/02/18.
 */
interface EboksFormatter
{
    fun formatDate(target : Message) : String
    fun formatDateRelative(target : Message) : String
    fun formatDateRelative(target : StoreboxReceiptItem) : String
    fun formatDateRelative(target : Item) : String
    fun formatSize(target : Content) : String
    fun formatSize(target: Int): String
    fun formatCpr(cpr: String) : String

    fun formatDateToDay(date : Date) : String
    fun formatDateToTime(date : Date) : String

    // monaaaay
    fun formatPrice(item : Item) : String
}