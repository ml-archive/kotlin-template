package dk.eboks.app.presentation.ui.message.screens.reply

import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.formreply.FormInput
import dk.eboks.app.mail.presentation.ui.message.screens.reply.ReplyFormInput

class TextFormInput(
    formInput: FormInput,
    inflater: LayoutInflater,
    handler: Handler,
    val multiline: Boolean = false
) : ReplyFormInput(formInput, inflater, handler), TextWatcher {
    private var textTil: TextInputLayout? = null
    private var textEt: EditText? = null

    override fun buildView(vg: ViewGroup): View {
        val resid = if (multiline) R.layout.form_input_text_multiline else R.layout.form_input_text
        val v = inflater.inflate(resid, vg, false)
        textTil = v.findViewById(R.id.textTil)
        textEt = v.findViewById(R.id.textEt)

        textTil?.hint = if (!formInput.required) formInput.label else "${formInput.label}*"
        formInput.value?.let {
            textEt?.setText(it)
        }
        textEt?.isEnabled = !formInput.readonly
        validate(silent = true)
        return v
    }

    override fun validate(silent: Boolean) {
        // Timber.e("Validating $formInput")
        isValid = false
        val text: String = textEt?.text.toString().trim()
        if (formInput.required && text.isBlank()) {
            textTil?.error = if (silent) null else Translation.reply.required
            return
        }
        formInput.minLength?.let { min ->
            if (text.length < min) {
                textTil?.error = if (silent) null else Translation.reply.minLengthError.replace(
                    "[CHARS]",
                    "$min"
                )
                return
            }
        }
        formInput.maxLength?.let { max ->
            if (text.length > max) {
                textTil?.error = if (silent) null else Translation.reply.maxLengthError.replace(
                    "[CHARS]",
                    "$max"
                )
                return
            }
        }
        formInput.validate?.toRegex()?.let {
            if (it.matches(text)) {
                textTil?.error = if (silent) null else formInput.error
                return
            }
        }

        formInput.value = text

        textTil?.error = null
        isValid = true
        return
    }

    override fun onResume() {
        textEt?.addTextChangedListener(this)
    }

    override fun onPause() {
        textEt?.removeTextChangedListener(this)
    }

    private val validateDelayedRunnable = Runnable {
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