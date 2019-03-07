package dk.eboks.app.presentation.ui.senders.screens.list

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.senders.components.list.SenderAllListComponentFragment
import dk.eboks.app.senders.presentation.ui.screens.list.SenderAllListContract
import javax.inject.Inject

class SenderAllListActivity : BaseActivity(), SenderAllListContract.View {
    @Inject lateinit var presenter: SenderAllListContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_senders_list)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setRootFragment(R.id.sendersListContainerLl, SenderAllListComponentFragment())

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed)
                    finish()
            }
        }
    }

    override fun getNavigationMenuAction(): Int {
        return R.id.actionMail
    }
}
