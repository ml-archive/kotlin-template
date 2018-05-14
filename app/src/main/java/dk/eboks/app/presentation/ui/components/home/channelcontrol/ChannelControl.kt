package dk.eboks.app.presentation.ui.components.home.channelcontrol

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.domain.models.home.Control
import java.util.*

abstract class ChannelControl(val control : Control, val inflater : LayoutInflater, val handler: Handler) : Observable() {
    var view : View? = null
    var isValid: Boolean = false

    fun addViewGroup(vg : ViewGroup)
    {
        val view = buildView(vg)
        view.tag = this
        vg.addView(view)
        onResume()
    }

    fun removeViewGroup(vg : ViewGroup)
    {
        vg.removeView(view)
    }

    open fun onResume() {}
    open fun onPause() {}
    protected abstract fun buildView(vg : ViewGroup) : View
}