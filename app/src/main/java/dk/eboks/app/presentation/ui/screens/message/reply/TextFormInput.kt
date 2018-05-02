package dk.eboks.app.presentation.ui.screens.message.reply

import android.os.Handler
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.formreply.FormInput
import dk.eboks.app.util.guard
import timber.log.Timber
import java.util.regex.Matcher
import java.util.regex.Pattern

class TextFormInput(formInput: FormInput, inflater: LayoutInflater, handler: Handler, val multiline : Boolean = false) : ReplyFormInput(formInput, inflater, handler), TextWatcher
{
    var textTil : TextInputLayout? = null
    var textEt : EditText? = null

    // lazy compile the pattern only if we get one
    private val pattern: Pattern? by lazy {
        formInput.validate?.let {
            //Timber.e("Compiling $it")
            Pattern.compile(it)
        }
    }

    override fun buildView(vg : ViewGroup): View {
        val resid = if(multiline) R.layout.form_input_text_multiline else R.layout.form_input_text
        val v = inflater.inflate(resid, vg, false)
        textTil = v.findViewById(R.id.textTil)
        textEt = v.findViewById(R.id.textEt)

        textTil?.hint = if(!formInput.required) formInput.label else "${formInput.label}*"
        formInput.value?.let {
            textEt?.setText(it)
        }
        textEt?.isEnabled = !formInput.readonly
        validate(silent = true)
        return v
    }

    override fun validate(silent : Boolean) {
        //Timber.e("Validating $formInput")
        isValid = false
        val text : String = textEt?.text.toString().trim()
        if(formInput.required && text.isNullOrBlank())
        {
            textTil?.error = if(silent) null else Translation.reply.required
            return
        }
        formInput.minLength?.let { min ->
            if(text.length < min)
            {
                textTil?.error = if(silent) null else Translation.reply.minLengthError.replace("[CHARS]", "$min")
                return
            }
        }
        formInput.maxLength?.let { max ->
            if(text.length > max)
            {
                textTil?.error = if(silent) null else Translation.reply.maxLengthError.replace("[CHARS]", "$max")
                return
            }
        }
        formInput.validate?.let {
            val matcher = pattern?.matcher(text)
            if(matcher?.matches() == false)
            {
                textTil?.error = if(silent) null else formInput.error
                return
            }
        }

        formInput.value = text

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

    val validateDelayedRunnable = Runnable {
        validate()
        setChanged()
        notifyObservers()
    }

    override fun afterTextChanged(p0: Editable?) {
        handler.removeCallbacks(validateDelayedRunnable)
        handler.postDelayed(validateDelayedRunnable, BuildConfig.INPUT_VALIDATION_DELAY)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}