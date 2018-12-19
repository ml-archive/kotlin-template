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
import java.util.regex.Pattern

class NumberFormInput(formInput: FormInput, inflater: LayoutInflater, handler: Handler) : ReplyFormInput(formInput, inflater, handler), TextWatcher
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
        val v = inflater.inflate(R.layout.form_input_number, vg, false)
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
        val doubleval = text.toDoubleOrNull()
        if(doubleval == null)
        {
            textTil?.error = if(silent) null else Translation.reply.nanError
            return
        }
        formInput.minValue?.let { min ->
            if(doubleval < min)
            {
                textTil?.error = if(silent) null else Translation.reply.minValueError.replace("[VALUE]", "$min")
                return
            }
        }
        formInput.maxValue?.let { max ->
            if(doubleval > max)
            {
                textTil?.error = if(silent) null else Translation.reply.maxValueError.replace("[VALUE]", "$max")
                return
            }
        }
        formInput.value = "$doubleval"
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