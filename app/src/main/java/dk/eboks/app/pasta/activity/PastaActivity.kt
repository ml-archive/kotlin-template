package dk.eboks.app.pasta.activity

import android.os.Bundle
import dk.eboks.app.presentation.base.BaseActivity
import timber.log.Timber
import javax.inject.Inject

class PastaActivity : BaseActivity(), PastaContract.View {
    @Inject lateinit var presenter: PastaContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_pasta)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun showError(msg: String) {
        Timber.e(msg) // errorhandling lol
    }
}
