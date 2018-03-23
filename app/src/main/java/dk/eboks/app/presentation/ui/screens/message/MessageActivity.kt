package dk.eboks.app.presentation.ui.screens.message

import android.os.Bundle
import android.util.Log
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

class MessageActivity : BaseActivity(), MessageContract.View {
    @Inject lateinit var presenter: MessageContract.Presenter
    @Inject
    lateinit var formatter: EboksFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setContentView(R.layout.activity_message)
        setupTopBar()

    }

    private fun setupTopBar()
    {
        mainTb.setNavigationIcon(R.drawable.red_navigationbar)
        mainTb.title = Translation.message.title
        mainTb.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun showTitle(message: Message) {
        mainTb.subtitle = formatter.formatDate(message)
    }

    override fun getNavigationMenuAction(): Int { return R.id.actionMail }
}
