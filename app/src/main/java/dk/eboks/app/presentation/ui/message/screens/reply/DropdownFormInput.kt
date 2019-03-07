package dk.eboks.app.presentation.ui.message.screens.reply

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.formreply.FormInput
import dk.eboks.app.mail.presentation.ui.message.screens.reply.ReplyFormInput
import java.util.regex.Pattern

class DropdownFormInput(formInput: FormInput, inflater: LayoutInflater, handler: Handler) :
    ReplyFormInput(formInput, inflater, handler), AdapterView.OnItemSelectedListener {
    private var dropdownSpr: Spinner? = null
    private var labelTv: TextView? = null
    private var errorTv: TextView? = null
    private var options: MutableList<String> = ArrayList()

    // lazy compile the pattern only if we get one
    private val pattern: Pattern? by lazy {
        formInput.validate?.let {
            // Timber.e("Compiling $it")
            Pattern.compile(it)
        }
    }

    override fun buildView(vg: ViewGroup): View {
        val v = inflater.inflate(R.layout.form_input_dropdown, vg, false)
        dropdownSpr = v.findViewById(R.id.dropdownSpr)
        labelTv = v.findViewById(R.id.labelTv)
        errorTv = v.findViewById(R.id.errorTv)

        dropdownSpr?.isEnabled = !formInput.readonly
        labelTv?.text = if (!formInput.required) formInput.label else "${formInput.label}*"
        options.add(Translation.reply.select)
        var i = 0
        var preselect = 0
        formInput.options?.let { opts ->
            for (option in opts) {
                options.add(option.value)
                // preselect an option from the server
                formInput.value?.let { value ->
                    if (option.value == value) {
                        preselect = i + 1
                    }
                }
                i++
            }
        }

        // val adapter = ArrayAdapter.createFromResource(vg.context, R.array.countries, android.R.layout.simple_spinner_item)
        val adapter =
            ArrayAdapter<String>(vg.context, android.R.layout.simple_spinner_item, options)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        dropdownSpr?.adapter = adapter
        dropdownSpr?.isSelected = false // otherwise listener will be called on initialization
        dropdownSpr?.setSelection(
            preselect,
            true
        ) // otherwise listener will be called on initialization
        dropdownSpr?.onItemSelectedListener = this

        validate(silent = true)
        return v
    }

    private fun setError(error: String?) {
        errorTv?.let {
            if (error != null) {
                it.text = error
                it.visibility = View.VISIBLE
            } else {
                it.text = ""
                it.visibility = View.INVISIBLE
            }
        }
    }

    override fun validate(silent: Boolean) {
        // Timber.e("Validating $formInput")
        isValid = false
        if (formInput.required) {
            if (formInput.required && dropdownSpr?.selectedItemPosition == 0) {
                if (!silent)
                    setError(Translation.reply.required)
                return
            }
        }

        formInput.value =
            dropdownSpr?.adapter?.getItem(dropdownSpr?.selectedItemPosition ?: 0) as String
        setError(null)
        isValid = true
        return
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        validate()
        setChanged()
        notifyObservers()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        validate()
        setChanged()
        notifyObservers()
    }
}