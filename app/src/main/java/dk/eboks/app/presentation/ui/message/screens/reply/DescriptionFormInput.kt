package dk.eboks.app.presentation.ui.message.screens.reply

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.models.formreply.FormInput
import dk.eboks.app.mail.presentation.ui.message.screens.reply.ReplyFormInput

class DescriptionFormInput(formInput: FormInput, inflater: LayoutInflater, handler: Handler) :
    ReplyFormInput(formInput, inflater, handler) {
    private var labelTv: TextView? = null
    private var descTv: TextView? = null

    init {
        isValid = true
    }

    /*
        If we dont get a value show the smaller label-only viewholder (for the sections in the design)
     */
    override fun buildView(vg: ViewGroup): View {
        val resid = if (formInput.value != null)
            R.layout.form_input_description
        else
            R.layout.form_input_description_label_only

        val v = inflater.inflate(resid, vg, false)
        labelTv = v.findViewById(R.id.labelTv)
        labelTv?.text = formInput.label

        // if we have no description, hide the widget
        formInput.value?.let { value ->
            descTv = v.findViewById(R.id.descriptionTv)
            descTv?.text = value
        }
        return v
    }
}