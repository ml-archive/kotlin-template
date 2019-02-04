package dk.eboks.app.presentation.ui.mail.screens.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.folder.components.selectuser.FolderSelectUserComponentFragment
import dk.eboks.app.util.views
import dk.eboks.app.util.visible
import dk.nodes.nstack.kotlin.util.getChildrenViews
import kotlinx.android.synthetic.main.activity_mail_overview.*
import javax.inject.Inject

class MailOverviewFragment : BaseFragment(), MailOverviewContract.View {

    @Inject lateinit var presenter: MailOverviewContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mail_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        refreshSrl.setOnRefreshListener {
            presenter.refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isHidden) presenter.refresh()
    }

    override fun onPause() {
        super.onPause()
        if (!isHidden) resetToolbar()
    }

    private fun setupToolbar(userName: String?) {
        val mainTb = activity?.getToolbar() ?: return
        mainTb.title = userName
        if (BuildConfig.ENABLE_SHARES) {
            if (!mainTb.getChildrenViews().any { view -> view is ImageView }) {
                val imageView = ImageView(context)
                imageView.setImageResource(R.drawable.icon_48_small_arrow_down)
                val layoutparams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                imageView.layoutParams = layoutparams
                mainTb.addView(imageView)
            }
            mainTb.isClickable = true
            mainTb.setOnClickListener {
                activity?.openComponentDrawer(FolderSelectUserComponentFragment::class.java)
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) {
            resetToolbar()
        } else {
            presenter.refresh()
        }
    }

    private fun resetToolbar() {
        val mainTb = activity?.getToolbar() ?: return
        val imageView = mainTb.views.firstOrNull { it is ImageView } ?: return
        mainTb.removeView(imageView)
        mainTb.isClickable = false
    }

    override fun showProgress(show: Boolean) {
        if (refreshSrl.isRefreshing != show)
            refreshSrl.isRefreshing = show
    }

    override fun setUser(user: User?, userName: String?) {
        setupToolbar(userName)
        user?.let {
            folderShortcutsFragmentContainerFl.visible = (it.verified)
        }
    }
}
