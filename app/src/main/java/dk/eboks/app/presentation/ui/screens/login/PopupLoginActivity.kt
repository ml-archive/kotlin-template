package dk.eboks.app.presentation.ui.screens.login

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.components.start.login.LoginComponentFragment
import dk.eboks.app.util.guard
import dk.eboks.app.util.putArg
import kotlinx.android.synthetic.main.activity_popup_login.view.*
import timber.log.Timber
import javax.inject.Inject

class PopupLoginActivity : BaseActivity(), PopupLoginContract.View {
    @Inject lateinit var presenter: PopupLoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_login)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed)
                    finish()
            }
        }
        intent?.getStringExtra("verifyLoginProviderId")?.let { provider_id->
            setRootFragment(R.id.containerFl, LoginComponentFragment().putArg("verifyLoginProviderId", provider_id))
        }.guard {
            setRootFragment(R.id.containerFl, LoginComponentFragment())
        }
    }

}
