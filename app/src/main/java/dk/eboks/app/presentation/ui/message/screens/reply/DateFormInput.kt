package dk.eboks.app.presentation.ui.message.screens.reply

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import com.google.android.material.textfield.TextInputLayout
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.formreply.FormInput
import dk.eboks.app.mail.presentation.ui.message.reply.ReplyFormInput
import dk.nodes.nstack.kotlin.NStack
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class DateFormInput(
    formInput: FormInput,
    inflater: LayoutInflater,
    handler: Handler,
    val isDateTime: Boolean = false
) : ReplyFormInput(formInput, inflater, handler) {
    var textTil: TextInputLayout? = null
    var textEt: EditText? = null
    var parsedDate: Date? = null

    val dateYearFormat: SimpleDateFormat by lazy {
        try {
            SimpleDateFormat("d MMMM YYYY", NStack.language)
        } catch (t: Throwable) {
            SimpleDateFormat()
        }
    }

    val serverDateFormat: SimpleDateFormat by lazy {
        try {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", NStack.language)
        } catch (t: Throwable) {
            SimpleDateFormat()
        }
    }

    val dateTimeFormat: SimpleDateFormat by lazy {
        try {
            SimpleDateFormat("d. MMM YYYY hh:mm", NStack.language)
        } catch (t: Throwable) {
            SimpleDateFormat()
        }
    }

    val clickListener = View.OnClickListener {
        showDatePicker()
    }

    override fun buildView(vg: ViewGroup): View {
        val v = inflater.inflate(R.layout.form_input_date, vg, false)
        textTil = v.findViewById(R.id.textTil)
        textEt = v.findViewById(R.id.textEt)

        textTil?.hint = if (!formInput.required) formInput.label else "${formInput.label}*"

        val dateformat = if (isDateTime) dateTimeFormat else dateYearFormat

        formInput.value?.let {
            try {
                parsedDate = dateformat.parse(it)
                textEt?.setText(dateformat.format(parsedDate))
            } catch (t: Throwable) {
                parsedDate = Date()
                textEt?.setText(dateformat.format(parsedDate))
            }
        }
        // textEt?.isEnabled = false
        v?.setOnClickListener(clickListener)
        textEt?.setOnClickListener(clickListener)
        textTil?.setOnClickListener(clickListener)

        validate(silent = true)
        return v
    }

    fun showDatePicker() {
        val cal = GregorianCalendar()
        parsedDate?.let { cal.time = it }

        val dlg = DatePickerDialog(
            inflater.context,
            R.style.DatePickerTheme,
            object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(picker: DatePicker?, year: Int, month: Int, day: Int) {
                    cal.set(year, month, day)
                    parsedDate = cal.time
                    if (!isDateTime) {
                        try {
                            textEt?.setText(dateYearFormat.format(parsedDate))
                        } catch (t: Throwable) {
                            parsedDate = null
                        }
                    }
                }
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        val title =
            if (isDateTime) Translation.reply.dateTimePickerCaption else Translation.reply.datePickerCaption
        dlg.setTitle(title)
        dlg.setOnDismissListener { it ->
            if (isDateTime)
                handler.post {
                    showTimePicker()
                }
            it.dismiss()
        }
        dlg.setOnCancelListener { it ->

            it.dismiss()
        }
        dlg.show()
    }

    fun showTimePicker() {
        val cal = GregorianCalendar()
        parsedDate?.let { cal.time = it }

        val dlg = TimePickerDialog(
            inflater.context,
            R.style.DatePickerTheme,
            object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(p0: TimePicker?, hour: Int, min: Int) {
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, min)
                    parsedDate = cal.time
                    try {
                        textEt?.setText(dateTimeFormat.format(parsedDate))
                    } catch (t: Throwable) {
                        parsedDate = null
                    }
                }
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        )

        val title =
            if (isDateTime) Translation.reply.dateTimePickerCaption else Translation.reply.datePickerCaption
        dlg.setTitle(title)
        dlg.setOnDismissListener { it ->
            it.dismiss()
        }
        dlg.setOnCancelListener { it ->
            it.dismiss()
        }
        dlg.show()
    }

    override fun validate(silent: Boolean) {
        // Timber.e("Validating $formInput")
        isValid = false
        val text: String = textEt?.text.toString().trim()
        if (formInput.required && text.isBlank()) {
            textTil?.error = if (silent) null else Translation.reply.required
            return
        }

        try {
            formInput.value = serverDateFormat.format(parsedDate)
        } catch (t: Throwable) {
            Timber.e(t)
            return
        }

        textTil?.error = null
        isValid = true
        return
    }
}