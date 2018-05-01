package dk.eboks.app.presentation.ui.screens.message.reply

import android.os.Bundle
import android.view.View
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.formreply.FormInput
import dk.eboks.app.domain.models.formreply.FormInputType
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.util.guard
import dk.eboks.app.util.views
import dk.nodes.nstack.kotlin.util.OnLanguageChangedListener
import kotlinx.android.synthetic.main.activity_reply_form.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ReplyFormActivity : BaseActivity(), ReplyFormContract.View, OnLanguageChangedListener {
    @Inject lateinit var presenter: ReplyFormContract.Presenter

    // observer without rx, how is teh possible?
    var inputObserver: Observer = Observer { observable, newval ->
        Timber.e("Input observer firing! $observable")
        submitBtn.isEnabled = allInputsValidate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_reply_form)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopBar(Translation.reply.title)

        // deserialize or message and hand it to the presenter
        intent?.extras?.getSerializable(Message::class.java.simpleName)?.let { msg ->
            presenter.setup(msg as Message)
        }.guard {
            finish()    // finish if we didn't get a message
        }

    }

    private fun setupTopBar(txt : String) {
        mainTb.setNavigationIcon(R.drawable.ic_red_close)
        mainTb.title = txt
        mainTb.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        // iterate through form inputs and let them register listeners
        for(v in formInputLl.views)
        {
            if(v.tag is ReplyFormInput)
            {
                val input = v.tag as ReplyFormInput
                input.onResume()
            }
        }
    }

    override fun onPause() {
        // iterate through form inputs and let them deregister listeners
        for(v in formInputLl.views)
        {
            if(v.tag is ReplyFormInput)
            {
                val input = v.tag as ReplyFormInput
                input.onPause()
            }
        }
        super.onPause()
    }

    private fun allInputsValidate() : Boolean
    {
        if(formInputLl.childCount == 0)
            return false
        for(v in formInputLl.views)
        {
            if(v.tag is ReplyFormInput)
            {
                val input = v.tag as ReplyFormInput
                if(!input.isValid)
                    return false
            }
        }
        return true // alles sehr gut
    }

    override fun onLanguageChanged(locale: Locale) {
        mainTb.title = Translation.reply.title
    }

    override fun showProgress(show: Boolean) {
        progressFl.visibility = if(show) View.VISIBLE else View.INVISIBLE
    }

    override fun clearForm() {
        formInputLl.removeAllViews()
    }

    override fun showFormInput(input: FormInput) {
        //Timber.e("showing form input $input")
        when(input.type)
        {
            FormInputType.DESCRIPTION -> {
                val fi = DescriptionFormInput(input, inflator, mainHandler)
                fi.addObserver(inputObserver)
                fi.addViewGroup(formInputLl)
            }
            FormInputType.LINK -> {
                val fi = LinkFormInput(input, inflator, mainHandler)
                fi.addObserver(inputObserver)
                fi.addViewGroup(formInputLl)
            }
            FormInputType.TEXT -> {
                val fi = TextFormInput(input, inflator, mainHandler)
                fi.addObserver(inputObserver)
                fi.addViewGroup(formInputLl)
            }
            FormInputType.TEXTAREA -> {
                val fi = TextFormInput(input, inflator, mainHandler, multiline = true)
                fi.addObserver(inputObserver)
                fi.addViewGroup(formInputLl)
            }
            FormInputType.NUMBER -> {
                val fi = NumberFormInput(input, inflator, mainHandler)
                fi.addObserver(inputObserver)
                fi.addViewGroup(formInputLl)
            }
            FormInputType.DROPDOWN -> {
                val fi = DropdownFormInput(input, inflator, mainHandler)
                fi.addObserver(inputObserver)
                fi.addViewGroup(formInputLl)
            }
            FormInputType.DATE -> {
                val fi = DateFormInput(input, inflator, mainHandler)
                fi.addObserver(inputObserver)
                fi.addViewGroup(formInputLl)
            }
            FormInputType.DATETIME -> {
                val fi = DateFormInput(input, inflator, mainHandler, isDateTime = true)
                fi.addObserver(inputObserver)
                fi.addViewGroup(formInputLl)
            }
            FormInputType.RADIOBOX -> {
                val fi = RadioBoxFormInput(input, inflator, mainHandler)
                fi.addObserver(inputObserver)
                fi.addViewGroup(formInputLl)
            }
            FormInputType.LIST -> {
                val fi = CheckBoxFormInput(input, inflator, mainHandler)
                fi.addObserver(inputObserver)
                fi.addViewGroup(formInputLl)
            }
            else -> {}
        }
    }
}
