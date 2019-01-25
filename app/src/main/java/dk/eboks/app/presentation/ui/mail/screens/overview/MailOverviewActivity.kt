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
import dk.eboks.app.util.visible
import dk.nodes.nstack.kotlin.util.getChildrenViews
import kotlinx.android.synthetic.main.activity_mail_overview.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

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

    private fun setupTopbar(userName: String?) {
        mainTb.title = userName
        if(BuildConfig.ENABLE_SHARES) {
            if(!mainTb.getChildrenViews().any { view -> view is ImageView }) {
                val imageView = ImageView(this)
                imageView.setImageResource(R.drawable.icon_48_small_arrow_down)
                val layoutparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                imageView.layoutParams = layoutparams
                mainTb.addView(imageView)
            }
            mainTb.isClickable = true
            mainTb.setOnClickListener {
                refreshOnResume = true
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

    override fun setUser(user: User?, userName: String?) {
        setupTopbar(userName)
        user?.let {
            folderShortcutsFragmentContainerFl.visible = (it.verified)
        }
    }

    companion object {
        var refreshOnResume = false
    }
}
