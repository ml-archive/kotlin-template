package dk.eboks.app.presentation.ui.screens.message.reply

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.domain.models.formreply.FormInput

abstract class ReplyFormInput(val formInput : FormInput, val inflater : LayoutInflater) {
    var view : View? = null

    fun addViewGroup(vg : ViewGroup)
    {
        vg.addView(buildView(vg))
    }

    fun removeViewGroup(vg : ViewGroup)
    {
        vg.removeView(view)
    }


    protected abstract fun buildView(vg : ViewGroup) : View
}