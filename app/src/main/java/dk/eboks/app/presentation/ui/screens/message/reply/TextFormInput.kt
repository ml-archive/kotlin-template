package dk.eboks.app.presentation.ui.screens.message.reply

import android.os.Handler
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import dk.eboks.app.R
import dk.eboks.app.domain.models.formreply.FormInput
import timber.log.Timber

class TextFormInput(formInput: FormInput, inflater: LayoutInflater, handler: Handler) : ReplyFormInput(formInput, inflater, handler), TextWatcher
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
        return v
    }

    override fun validate() {
        //Timber.e("Validating $formInput")
        isValid = false
        val text : String = textEt?.text.toString().trim()
        if(formInput.required && text.isNullOrBlank())
        {
            textTil?.error = "_Please fill out"
            return
        }
        formInput.minLength?.let { min ->
            if(text.length < min)
            {
                textTil?.error = "_Please enter at least $min characters"
                return
            }
        }
        formInput.maxLength?.let { max ->
            if(text.length > max)
            {
                textTil?.error = "_Please enter at less than $max characters"
                return
            }
        }
        formInput.validate?.let {

        }
        textTil?.error = null
        isValid = true
        return
    }

    override fun onResume()
    {
        textEt?.addTextChangedListener(this)
    }

    override fun onPause() {
        textEt?.removeTextChangedListener(this)
    }

    override fun afterTextChanged(p0: Editable?) {
        handler.postDelayed({
            validate()
        }, 1200)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}