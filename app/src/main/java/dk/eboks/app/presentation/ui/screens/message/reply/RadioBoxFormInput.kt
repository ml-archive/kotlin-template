package dk.eboks.app.presentation.ui.screens.message.reply

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.formreply.FormInput
import android.util.TypedValue
import android.widget.TextView
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.util.views


class RadioBoxFormInput(formInput: FormInput, inflater: LayoutInflater, handler: Handler) : ReplyFormInput(formInput, inflater, handler)
{
    var radioGroup : RadioGroup? = null
    var labelTv : TextView? = null
    var errorTv : TextView? = null
    var selectedItem : String? = null

    init {
        isValid = true
    }

    override fun buildView(vg : ViewGroup): View {
        val v = inflater.inflate(R.layout.form_input_radiobox, vg, false)
        radioGroup = v.findViewById(R.id.radioGroup)
        labelTv = v.findViewById(R.id.labelTv)
        errorTv = v.findViewById(R.id.errorTv)

        labelTv?.text = if(!formInput.required) formInput.label else "${formInput.label}*"
        formInput.options?.let {
            for(option in it)
            {
                val rb = RadioButton(vg.context)
                val params = RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT)
                rb.layoutParams = params
                val typedValue = TypedValue()
                vg.context.theme.resolveAttribute(android.R.attr.listChoiceIndicatorSingle, typedValue, true)
                rb.setCompoundDrawablesWithIntrinsicBounds(0,0,typedValue.resourceId,0)
                rb.buttonDrawable = null
                rb.text = option.value
                rb.tag = option.name
                radioGroup?.addView(rb)
            }
        }

        radioGroup?.setOnCheckedChangeListener({radioGroup, i ->
            for(rb in radioGroup.views)
            {
                if((rb as RadioButton).isChecked)
                    selectedItem = rb.tag as String
            }
        })
        return v
    }

    override fun validate(silent : Boolean) {
        //Timber.e("Validating $formInput")
        isValid = false
        if(formInput.required && selectedItem == null)
        {
            if(!silent)
                setError(Translation.reply.required)
            return
        }

        setError(null)
        isValid = true
        return
    }

    private fun setError(error : String?)
    {
        errorTv?.let {
            if(error != null) {
                it.text = error
                it.visibility = View.VISIBLE
            }
            else
            {
                it.text = ""
                it.visibility = View.INVISIBLE
            }
        }
    }

}