package dk.eboks.app.presentation.ui.screens.channels.content.storebox

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceipt
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.components.channels.content.storebox.ChannelContentStoreboxComponentFragment
import dk.eboks.app.presentation.ui.components.channels.content.storebox.ChannelContentStoreboxDetailComponentFragment
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

class StoreboxContentActivity : BaseActivity(), StoreboxContentContract.View {
    @Inject
    lateinit var presenter: StoreboxContentContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_storebox_content)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        updateTranslation()
        setRootFragment(R.id.content, ChannelContentStoreboxComponentFragment())

        supportFragmentManager.addOnBackStackChangedListener {
            Timber.e("bs changed entryCount ${supportFragmentManager.backStackEntryCount}")
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed)
                    finish()
            }
        }
    }

    fun showDetailFragment(receiptID: String) {
        Timber.d("showDetailFragment: %s", receiptID)
        val bundle = Bundle()
        bundle.putString(StoreboxReceipt.KEY_ID, receiptID)
        val fragment = ChannelContentStoreboxDetailComponentFragment()
        fragment.arguments = bundle

        addFragmentOnTop(R.id.content, fragment, true)
    }


    private fun updateTranslation() {
        mainTb.title = Translation.storeboxlist.title
    }
}
