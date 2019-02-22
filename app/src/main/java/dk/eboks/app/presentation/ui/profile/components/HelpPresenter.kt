package dk.eboks.app.presentation.ui.profile.components

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.config.AppConfig
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by Christian on 5/23/2018.
 * @author Christian
 * @since 5/23/2018.
 */
class HelpPresenter @Inject constructor(private val appConfig: AppConfig) :
    HelpContract.Presenter,
    BasePresenterImpl<HelpContract.View>() {

    override fun onViewCreated(view: HelpContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)

        runAction { v ->
            appConfig.getResourceLinkByType("support")?.let { link ->
                v.loadUrl(link.link.url)
            }
        }
    }
}