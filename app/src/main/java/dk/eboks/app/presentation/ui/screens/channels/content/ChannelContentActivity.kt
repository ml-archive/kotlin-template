package dk.eboks.app.presentation.ui.screens.channels.content

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.components.channels.opening.ChannelOpeningComponentFragment
import dk.eboks.app.util.putArg
import javax.inject.Inject

class ChannelContentActivity : BaseActivity(), ChannelContentContract.View {
    @Inject lateinit var presenter: ChannelContentContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_channel_content)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        intent.getSerializableExtra(Channel::class.java.simpleName)?.let { channel ->
            supportFragmentManager.beginTransaction().add(
                    R.id.content,
                    ChannelOpeningComponentFragment().putArg(Channel::class.java.simpleName, channel),
                    ChannelOpeningComponentFragment::class.java.simpleName).commit()
        }

    }

}
