package dk.eboks.app.domain.managers

import dk.eboks.app.domain.models.Content
import dk.eboks.app.domain.models.Message

/**
 * Created by bison on 18-02-2018.
 */
interface DownloadManager {
    fun downloadContent(content: Content) : String?
    fun downloadAttachmentContent(message : Message, content: Content) : String?
}