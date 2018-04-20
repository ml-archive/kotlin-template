package dk.eboks.app.presentation.ui.screens.message.reply

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.models.formreply.FormInput

class DescriptionFormInput(formInput: FormInput, inflater: LayoutInflater) : ReplyFormInput(formInput, inflater)
{
    var headlineTv : TextView? = null

    override fun buildView(vg : ViewGroup): View {
        val v = inflater.inflate(R.layout.form_input_description, vg, false)
        headlineTv = v.findViewById(R.id.headLineTv)
        headlineTv?.text = formInput.label
        return v
    }
}