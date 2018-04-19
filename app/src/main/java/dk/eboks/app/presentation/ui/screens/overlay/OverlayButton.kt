package dk.eboks.app.presentation.ui.screens.overlay

import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import java.io.Serializable

/**
 * Created by thsk on 12/04/2018.
 */
class OverlayButton(type: ButtonType) : Serializable {
     var type : ButtonType
     var text : String?
     var icon : Int?

    init {
        this.type = type
        this.text = getText(type)
        this.icon = getIcon(type)
    }

    public fun getText(type: ButtonType): String {
        when (type) {
            ButtonType.MOVE -> return Translation.overlaymenu.move
            ButtonType.DELETE -> return Translation.overlaymenu.delete
            ButtonType.PRINT -> return Translation.overlaymenu.print
            ButtonType.MAIL -> return Translation.overlaymenu.mail
            ButtonType.OPEN -> return Translation.overlaymenu.openIn
            else ->{
                return ""
            }
        }

    }

    private fun getIcon(type: ButtonType): Int? {
        when (type) {
            ButtonType.MOVE -> return R.drawable.ic_folder
            ButtonType.DELETE -> return R.drawable.icon_48_delete_red
            ButtonType.PRINT -> return R.drawable.icon_48_print_red
            ButtonType.MAIL -> return R.drawable.icon_48_forward_red
            ButtonType.OPEN -> return R.drawable.icon_48_share_red
            else ->{
                return null
            }
        }
    }

}



enum class ButtonType {
    MOVE, DELETE, PRINT, MAIL,OPEN
}