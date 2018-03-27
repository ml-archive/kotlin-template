package dk.eboks.app.presentation.ui.screens.home

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import timber.log.Timber
import javax.inject.Inject

class HomeActivity : BaseActivity(), HomeContract.View {
    @Inject lateinit var presenter: HomeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_home)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

    }


    override fun getNavigationMenuAction(): Int {
        return R.id.actionHome
    }
}
