package dk.eboks.app.domain.managers

import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message

/**
 * Created by bison on 18-02-2018.
 */
interface DownloadManager {
    fun downloadContent(message: Message, content: Content): String?
    fun downloadAttachmentContent(message: Message, content: Content): String?
    fun downloadReceiptContent(receiptId: String): String?
}