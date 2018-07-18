package dk.eboks.app.presentation.ui.mail.screens.overview

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.dialogs.ConfirmDialogFragment
import dk.eboks.app.presentation.ui.folder.components.selectuser.FolderSelectUserComponentFragment
import dk.eboks.app.util.setVisible
import kotlinx.android.synthetic.main.activity_mail_overview.*
import timber.log.Timber
import javax.inject.Inject
import kotlinx.android.synthetic.main.include_toolbar.*


class MailOverviewActivity : BaseActivity(), MailOverviewContract.View {
    @Inject lateinit var presenter: MailOverviewContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_overview)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        //setupYourMail()


        refreshSrl.setOnRefreshListener {
            presenter.refresh()
        }

    }

    override fun onResume() {
        super.onResume()
        if(refreshOnResume)
        {
            refreshOnResume = false
            presenter.refresh()
        }
    }

    private fun setupTopbar(user: User?) {
        mainTb.title = user?.name
        if(BuildConfig.ENABLE_SHARES) {
            var imageView = ImageView(this)
            imageView.setImageResource(R.drawable.icon_48_small_arrow_down)
            var layoutparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            imageView.layoutParams = layoutparams
            mainTb.addView(imageView)
            mainTb.isClickable = true
            mainTb.setOnClickListener {
                openComponentDrawer(FolderSelectUserComponentFragment::class.java)
            }
        }
    }

    fun showConfirmDialog()
    {
        Timber.e("Showing confirm dialog")
        var dialog = ConfirmDialogFragment()
        dialog.show(supportFragmentManager, ConfirmDialogFragment::class.simpleName)
    }

    override fun showProgress(show: Boolean) {
        if(refreshSrl.isRefreshing != show)
            refreshSrl.isRefreshing = show
    }

    override fun setUser(user: User?) {
        setupTopbar(user)
        user?.let {
            folderShortcutsFragmentContainerFl.setVisible(it.verified)
        }
    }

    companion object {
        var refreshOnResume = false
    }
}
