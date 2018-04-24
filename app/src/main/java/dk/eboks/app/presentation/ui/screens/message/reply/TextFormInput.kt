package dk.eboks.app.presentation.ui.screens.message.reply

import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import dk.eboks.app.R
import dk.eboks.app.domain.models.formreply.FormInput

class TextFormInput(formInput: FormInput, inflater: LayoutInflater) : ReplyFormInput(formInput, inflater)
{
    var textTil : TextInputLayout? = null
    var textEt : EditText? = null

    override fun buildView(vg : ViewGroup): View {
        val v = inflater.inflate(R.layout.form_input_text, vg, false)
        textTil = v.findViewById(R.id.textTil)
        textEt = v.findViewById(R.id.textEt)

        textTil?.hint = if(!formInput.required) formInput.label else "${formInput.label}*"
        formInput.value?.let {
            textEt?.setText(it)
        }
        textEt?.isEnabled = !formInput.readonly
        setupValidation()
        return v
    }

    fun setupValidation()
    {
        val text : String = textEt?.text.toString().trim()
        textEt?.setOnFocusChangeListener { view, b ->
            if(!b) // on focus lost
            {
                if(formInput.required && text.isNullOrBlank())
                {
                    textEt?.error = "_Please fill out"
                    return@setOnFocusChangeListener
                }
                formInput.minLength?.let { min ->
                    if(text.length < min)
                    {
                        textEt?.error = "_Please enter at least $min characters"
                    }
                }
                formInput.maxLength?.let { max ->
                    if(text.length > max)
                    {
                        textEt?.error = "_Please enter at less than $max characters"
                    }
                }

            }
        }
    }
}