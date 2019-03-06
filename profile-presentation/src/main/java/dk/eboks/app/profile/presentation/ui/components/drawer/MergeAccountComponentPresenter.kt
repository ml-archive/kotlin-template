package dk.eboks.app.profile.presentation.ui.components.drawer

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class MergeAccountComponentPresenter @Inject constructor(val appState: AppStateManager) :
    MergeAccountComponentContract.Presenter,
    BasePresenterImpl<MergeAccountComponentContract.View>() {

    init {
    }

    override fun setMergeStatus(shouldMerge: Boolean) {
        appState.state?.verificationState?.let { state ->
            Timber.e("Setting profile merge status to $shouldMerge")
            state.shouldMergeProfiles = shouldMerge
        }
        runAction { v -> v.close() }
    }
}