package dk.eboks.app.presentation.ui.screens.message.reply

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.formreply.FormInput
import java.util.regex.Pattern

class DropdownFormInput(formInput: FormInput, inflater: LayoutInflater, handler: Handler) : ReplyFormInput(formInput, inflater, handler)
{
    var dropdownSpr : Spinner? = null
    var labelTv : TextView? = null
    var options : MutableList<String> = ArrayList()

    // lazy compile the pattern only if we get one
    private val pattern: Pattern? by lazy {
        formInput.validate?.let {
            //Timber.e("Compiling $it")
            Pattern.compile(it)
        }
    }

    override fun buildView(vg : ViewGroup): View {
        val v = inflater.inflate(R.layout.form_input_dropdown, vg, false)
        dropdownSpr = v.findViewById(R.id.dropdownSpr)
        labelTv = v.findViewById(R.id.labelTv)

        dropdownSpr?.isEnabled = !formInput.readonly
        labelTv?.text = formInput.label
        options.add(Translation.reply.select)
        formInput.options?.let { opts ->
            for(option in opts)
            {
                options.add(option.value)
            }
        }

        //val adapter = ArrayAdapter.createFromResource(vg.context, R.array.countries, android.R.layout.simple_spinner_item)
        val adapter = ArrayAdapter<String>(vg.context,  android.R.layout.simple_spinner_item, options)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        dropdownSpr?.adapter = adapter
        dropdownSpr?.setSelection(0)

        validate(silent = true)
        return v
    }

    override fun validate(silent : Boolean) {
        //Timber.e("Validating $formInput")
        isValid = false
        if(formInput.required)
        {

            return
        }

        isValid = true
        return
    }

    override fun onResume()
    {

    }

    override fun onPause() {

    }

    val validateDelayedRunnable = Runnable {
        validate()
        setChanged()
        notifyObservers()
    }
}