package dk.eboks.app.presentation.ui.channels.screens.content

import android.content.Intent
import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.channels.components.content.storebox.content.ChannelContentStoreboxComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.web.ChannelContentComponentFragment
import dk.eboks.app.presentation.ui.channels.components.opening.ChannelOpeningComponentFragment
import dk.eboks.app.presentation.ui.channels.screens.content.ekey.EkeyContentActivity
import dk.eboks.app.util.putArg
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

class ChannelContentActivity : BaseActivity(), ChannelContentContract.View {
    @Inject lateinit var presenter: ChannelContentContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_channel_content)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        val opendirectly = intent.getBooleanExtra("openDirectly", false)
        intent.getParcelableExtra<Channel>(Channel::class.java.simpleName)?.let { channel ->
            setupToolbar(channel)
            if (!opendirectly) {
                supportFragmentManager.beginTransaction().add(
                    R.id.content,
                    ChannelOpeningComponentFragment().putArg(
                        Channel::class.java.simpleName,
                        channel
                    ),
                    ChannelOpeningComponentFragment::class.java.simpleName
                ).commit()
            } else {
                presenter.open(channel)
            }
        }
    }

    private fun setupToolbar(channel: Channel) {
        mainTb.title = channel.name
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun openChannelContent(channel: Channel) {
        val fragment =
            ChannelContentComponentFragment().putArg(Channel::class.simpleName!!, channel)
        addFragmentOnTop(R.id.content, fragment, false)
    }

    override fun openStoreBoxContent(channel: Channel) {
        val fragment = ChannelContentStoreboxComponentFragment().putArg(
            Channel::class.java.simpleName,
            channel
        )
        addFragmentOnTop(R.id.content, fragment, false)
    }

    override fun openEkeyContent() {
        startActivity(Intent(this, EkeyContentActivity::class.java))
        finish()
    }
}
