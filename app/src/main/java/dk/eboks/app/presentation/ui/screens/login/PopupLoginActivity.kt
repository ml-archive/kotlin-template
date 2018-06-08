package dk.eboks.app.presentation.ui.screens.login

import android.os.Bundle
import dk.eboks.app.presentation.base.BaseActivity
import timber.log.Timber
import javax.inject.Inject

class PopupLoginActivity : BaseActivity(), PopupLoginContract.View {
    @Inject lateinit var presenter: PopupLoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_pasta)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

}
