package dk.eboks.app.presentation.ui.screens.message.reply

import android.os.Bundle
import dk.eboks.app.presentation.base.BaseActivity
import timber.log.Timber
import javax.inject.Inject

class ReplyFormActivity : BaseActivity(), ReplyFormContract.View {
    @Inject lateinit var presenter: ReplyFormContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_reply_form)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

}
