package dk.eboks.app.presentation.ui.overlay.screens

import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import java.io.Serializable

/**
 * Created by thsk on 12/04/2018.
 */
class OverlayButton(val type: ButtonType) : Serializable {
    val text: String? = getText(type)
    val icon: Int? = getIcon(type)

    fun getText(type: ButtonType): String {
        return when (type) {
            ButtonType.MOVE -> Translation.overlaymenu.move
            ButtonType.DELETE -> Translation.overlaymenu.delete
            ButtonType.PRINT -> Translation.overlaymenu.print
            ButtonType.MAIL -> Translation.overlaymenu.mail
            ButtonType.OPEN -> Translation.overlaymenu.openIn
            ButtonType.ARCHIVE -> Translation.overlaymenu.archive
            ButtonType.READ -> Translation.overlaymenu.markAsRead
            ButtonType.UNREAD -> Translation.overlaymenu.markAsUnread
            ButtonType.GALLERY -> Translation.overlaymenu.chooseFromLibrary
            ButtonType.CAMERA -> Translation.overlaymenu.takePhoto
        }

    }

    private fun getIcon(type: ButtonType): Int? {
        return when (type) {
            ButtonType.MOVE -> R.drawable.ic_folder
            ButtonType.DELETE -> R.drawable.icon_48_delete_red
            ButtonType.PRINT -> R.drawable.icon_48_print_red
            ButtonType.MAIL -> R.drawable.icon_48_forward_red
            ButtonType.OPEN -> R.drawable.icon_48_share_red
            ButtonType.ARCHIVE -> R.drawable.icon_48_archive_red
            ButtonType.READ -> R.drawable.icon_48_mail_open_red
            ButtonType.UNREAD -> R.drawable.icon_48_mail_red
            else -> {
                null
            }
        }
    }

}


enum class ButtonType {
    MOVE, DELETE, PRINT, MAIL, OPEN, READ, UNREAD, ARCHIVE, CAMERA, GALLERY
}