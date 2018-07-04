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
class PrivacyPresenter @Inject constructor(@Named("NAME_BASE_URL") val baseUrl: String) :
        PrivacyContract.Presenter,
        BasePresenterImpl<PrivacyContract.View>() {

    override fun onViewCreated(view: PrivacyContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)

        runAction { v ->
            val urlString = baseUrl.plus("resources/${Config.currentMode.countryCode}/privacypolicy")
            v.loadUrl(urlString)
        }
    }
}