package dk.eboks.app.presentation.ui.screens.senders.list

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.components.senders.list.SenderAllListComponentFragment
import kotlinx.android.synthetic.main.activity_senders_list.*
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
