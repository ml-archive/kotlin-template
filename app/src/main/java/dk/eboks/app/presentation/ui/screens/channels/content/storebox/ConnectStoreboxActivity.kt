package dk.eboks.app.presentation.ui.screens.channels.content.storebox

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by Christian on 5/14/2018.
 * @author   Christian
 * @since    5/14/2018.
 */
class ConnectStoreboxActivity: BaseActivity(), ConnectStoreboxContract.View {

    @Inject
    lateinit var presenter: ConnectStoreboxContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_storebox_connect)
        component.inject(this)

        mainTb.title = Translation.storeboxlogin.title
        mainTb.setNavigationIcon(R.drawable.icon_48_close_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            finish()
        }

        presenter.onViewCreated(this, lifecycle)
    }

    override fun showFound() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNotFound() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}