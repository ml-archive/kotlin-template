package dk.eboks.app.presentation.ui.screens.profile.myinfo

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.components.profile.myinfo.MyInfoComponentFragment
import timber.log.Timber
import javax.inject.Inject

class MyInfoActivity : BaseActivity(), MyInfoContract.View {
    @Inject lateinit var presenter: MyInfoContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_profile_myinfo)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setRootFragment(R.id.contentFl, MyInfoComponentFragment())
    }

}
