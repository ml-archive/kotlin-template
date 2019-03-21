package dk.eboks.app.presentation.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation

class AppToolbar @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, deffStyle: Int = R.attr.toolbarStyle) : Toolbar(context, attributeSet, deffStyle) {

    override fun setNavigationIcon(resId: Int) {
        super.setNavigationIcon(resId)
        navigationContentDescription = Translation.accessibility.navigateBackButton
    }
}