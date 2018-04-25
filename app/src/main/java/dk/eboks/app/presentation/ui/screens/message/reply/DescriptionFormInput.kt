package dk.eboks.app.presentation.ui.screens.message.reply

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.models.formreply.FormInput

class DescriptionFormInput(formInput: FormInput, inflater: LayoutInflater, handler : Handler) : ReplyFormInput(formInput, inflater, handler)
{
    var headlineTv : TextView? = null

    init {
        isValid = true
    }

    override fun buildView(vg : ViewGroup): View {
        val v = inflater.inflate(R.layout.form_input_description, vg, false)
        headlineTv = v.findViewById(R.id.headLineTv)
        headlineTv?.text = formInput.label
        return v
    }

}