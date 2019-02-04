package dk.eboks.app.presentation.ui.channels.components.content.ekey

import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.channels.screens.content.ekey.EkeyContentActivity

abstract class BaseEkeyFragment : BaseFragment() {
    fun getEkeyBaseActivity(): EkeyContentActivity? {
        if (activity is EkeyContentActivity)
            return activity as EkeyContentActivity
        return null
    }
}