package dk.eboks.app.presentation.ui.screens.message.reply

import android.os.Bundle
import android.view.View
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.formreply.FormInput
import dk.eboks.app.domain.models.formreply.FormInputType
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseActivity
import dk.nodes.nstack.kotlin.util.OnLanguageChangedListener
import kotlinx.android.synthetic.main.activity_reply_form.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ReplyFormActivity : BaseActivity(), ReplyFormContract.View, OnLanguageChangedListener {
    @Inject lateinit var presenter: ReplyFormContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_reply_form)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopBar(Translation.reply.title)

        intent?.extras?.getSerializable(Message::class.java.simpleName)?.let { msg ->
            presenter.setup(msg as Message)
        }
    }

    private fun setupTopBar(txt : String) {
        mainTb.setNavigationIcon(R.drawable.ic_red_close)
        mainTb.title = txt
        mainTb.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onLanguageChanged(locale: Locale) {
        mainTb.title = Translation.reply.title
    }

    override fun showProgress(show: Boolean) {
        progressFl.visibility = if(show) View.VISIBLE else View.INVISIBLE
    }

    override fun showFormInput(input: FormInput) {
        //Timber.e("showing form input $input")
        when(input.type)
        {
            FormInputType.DESCRIPTION -> {
                val fi = DescriptionFormInput(input, inflator)
                fi.addViewGroup(formInputLl)
            }
            FormInputType.LINK -> {
                val fi = LinkFormInput(input, inflator)
                fi.addViewGroup(formInputLl)
            }
            FormInputType.TEXT -> {
                val fi = TextFormInput(input, inflator)
                fi.addViewGroup(formInputLl)
            }
            else -> {}
        }
    }
}
