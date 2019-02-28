package dk.eboks.app.profile.presentation.ui.components

import androidx.lifecycle.Lifecycle
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Christian on 5/23/2018.
 * @author Christian
 * @since 5/23/2018.
 */
internal class PrivacyPresenter @Inject constructor(@Named("NAME_BASE_URL") private val baseUrl: String) :
    PrivacyContract.Presenter,
    BasePresenterImpl<PrivacyContract.View>() {

    override fun onViewCreated(view: PrivacyContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)

        runAction { v ->
            v.loadUrl("${baseUrl}resources/privacypolicy")
            // v.loadData(Translation.profile.privacyStatement)
        }
    }
}