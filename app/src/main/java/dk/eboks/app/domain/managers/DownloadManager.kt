package dk.eboks.app.domain.managers

import dk.eboks.app.domain.models.Content

/**
 * Created by bison on 18-02-2018.
 */
interface DownloadManager {
    fun downloadContent(content: Content) : String?
}