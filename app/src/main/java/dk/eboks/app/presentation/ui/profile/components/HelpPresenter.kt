package dk.eboks.app.presentation.ui.profile.components

import android.arch.lifecycle.Lifecycle
import dk.eboks.app.domain.config.Config
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Christian on 5/23/2018.
 * @author   Christian
 * @since    5/23/2018.
 */
class HelpPresenter @Inject constructor(@Named("NAME_BASE_URL") val baseUrl: String) :
        HelpContract.Presenter,
        BasePresenterImpl<HelpContract.View>() {

    override fun onViewCreated(view: HelpContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)

        runAction { v ->
            Config.getResourceLinkByType("support")?.let { link->
                v.loadUrl(link.link.url)
            }
        }
    }
}