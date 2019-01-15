package dk.eboks.app.presentation.ui.channels.screens.content.storebox

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import dk.eboks.app.R
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCardLinkInteractorImpl
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.base.BaseWebFragment
import dk.eboks.app.util.putArg
import kotlinx.android.synthetic.main.fragment_base_web.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber

class StoreboxAddCardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_storebox_add_card)
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed)
                    finish()
            }
        }
        intent?.getParcelableExtra<Link>(Link::class.java.simpleName)?.let { link ->
            setRootFragment(R.id.containerFl, StoreboxAddCardFragment().putArg(Link::class.java.simpleName, link) as BaseFragment)
        }

    }

    class StoreboxAddCardFragment : BaseWebFragment() {
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            arguments?.getParcelable<Link>(Link::class.java.simpleName)?.url?.let(webView::loadUrl)
            setupTopBar()
        }

        // shamelessly ripped from chnt
        private fun setupTopBar() {
            mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
            mainTb.setNavigationOnClickListener {
                activity?.onBackPressed()
            }
            mainTb.title = Translation.channelsettingsstoreboxadditions.addCardTitle
        }

        override fun onOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            Timber.e("URL override: $url")
            url?.let {
                if(url.contains(GetStoreboxCardLinkInteractorImpl.SUCCESS_CALLBACK)) {
                    activity?.finish()
                }
                if(url.contains(GetStoreboxCardLinkInteractorImpl.ERROR_CALLBACK))
                {
                    val ve = ViewError()
                    ve.shouldCloseView = true
                    showErrorDialog(ve)
                }
            }
            return false
        }

        override fun onLoadFinished(view: WebView?, url: String?) {

        }

    }
}
