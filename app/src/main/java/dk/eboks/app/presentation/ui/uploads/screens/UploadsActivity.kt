package dk.eboks.app.presentation.ui.uploads.screens

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.mail.components.maillist.MailListComponentFragment
import dk.eboks.app.presentation.ui.uploads.components.UploadOverviewComponentFragment
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_uploads.*

import javax.inject.Inject

class UploadsActivity : BaseActivity(), UploadsContract.View,
    MailListComponentFragment.MailListComponentListener {
    @Inject
    lateinit var presenter: UploadsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_uploads)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setRootFragment(R.id.contentFl, UploadOverviewComponentFragment())

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed)
                    finish()
            }
        }
    }

    override fun getNavigationMenuAction(): Int {
        return R.id.actionUploads
    }

    override fun onEditModeActive(active: Boolean) {
        navBarContainer.isVisible = !active
    }
}
