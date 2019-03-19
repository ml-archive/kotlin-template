package dk.eboks.app.presentation.ui.message.screens.reply

import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.formreply.FormInput
import dk.eboks.app.domain.models.formreply.FormInputOption
import dk.eboks.app.mail.presentation.ui.message.screens.reply.ReplyFormInput

class CheckBoxFormInput(formInput: FormInput, inflater: LayoutInflater, handler: Handler) :
    ReplyFormInput(formInput, inflater, handler) {
    private var checkBoxesLl: LinearLayout? = null
    private var labelTv: TextView? = null
    private var errorTv: TextView? = null
    private var selectedOptions: MutableList<FormInputOption> = ArrayList()

    init {
        isValid = true
    }

    override fun buildView(vg: ViewGroup): View {
        val v = inflater.inflate(R.layout.form_input_checkbox, vg, false)
        checkBoxesLl = v.findViewById(R.id.checkboxesLl)
        labelTv = v.findViewById(R.id.labelTv)
        errorTv = v.findViewById(R.id.errorTv)

        labelTv?.text = if (!formInput.required) formInput.label else "${formInput.label}*"
        formInput.options?.let {
            for (option in it) {
                val cb = CheckBox(vg.context)
                val params = LinearLayout.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                )
                cb.layoutParams = params
                val typedValue = TypedValue()
                vg.context.theme.resolveAttribute(
                    android.R.attr.listChoiceIndicatorMultiple,
                    typedValue,
                    true
                )
                cb.setCompoundDrawablesWithIntrinsicBounds(0, 0, typedValue.resourceId, 0)
                cb.setButtonDrawable(R.drawable.checkbox_background)
                cb.text = option.value
                cb.tag = option
                checkBoxesLl?.addView(cb)
                // preselect an option from the server

                formInput.value?.split(",")?.forEach { part ->
                    if (part == option.value) {
                        selectedOptions.add(option)
                        cb.isChecked = true
                    }
                }

                cb.setOnCheckedChangeListener { compoundButton, b ->
                    val opt: FormInputOption? = compoundButton.tag as FormInputOption
                    opt?.let {
                        if (b) {
                            selectedOptions.add(opt)
                        } else {
                            selectedOptions.remove(opt)
                        }
                        validate()
                        setChanged()
                        notifyObservers()
                    }
                }
            }
        }

        /*
        checkBoxesLl?.setOnCheckedChangeListener({radioGroup, i ->
            for(cb in radioGroup.views)
            {
                if((cb as RadioButton).isChecked)
                    selectedOptions.add(cb.tag as FormInputOption)
            }
        })
        */
        validate(silent = true)
        return v
    }

    override fun validate(silent: Boolean) {
        // Timber.e("Validating $formInput")
        isValid = false
        if (formInput.required && selectedOptions.isEmpty()) {
            if (!silent)
                setError(Translation.reply.required)
            return
        }

        formInput.value = selectedOptions.map { it.value }.joinToString(separator = ",") { it }
        setError(null)
        isValid = true
        return
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
}