package dk.eboks.app.presentation.ui.screens.channels.content.storebox

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.components.channels.content.ChannelContentStoreboxComponentFragment
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

class StoreboxContentActivity : BaseActivity(), StoreboxContentContract.View {
    @Inject lateinit var presenter: StoreboxContentContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_storebox_content)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        updateTranslation()
        val fragment = ChannelContentStoreboxComponentFragment()
        fragment?.let{
            supportFragmentManager.beginTransaction().add(R.id.content, it, ChannelContentStoreboxComponentFragment::class.java.simpleName).commit()
        }
    }


    private fun updateTranslation() {
        mainTb.title = Translation.storeboxlist.title
    }
}
