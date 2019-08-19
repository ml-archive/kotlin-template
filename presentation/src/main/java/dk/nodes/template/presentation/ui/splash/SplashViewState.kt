package dk.nodes.template.presentation.ui.splash

import dk.nodes.nstack.kotlin.models.AppOpenResult
import dk.nodes.nstack.kotlin.models.AppUpdateData
import dk.nodes.template.presentation.ui.base.BaseAction
import dk.nodes.template.presentation.ui.base.BaseChange
import dk.nodes.template.presentation.ui.base.BaseState
import dk.nodes.template.presentation.util.SingleEvent

data class SplashViewState(
    val doneLoading: Boolean,
    val nstackUpdateAvailable: SingleEvent<AppUpdateData>?
) : BaseState

sealed class SplashChange : BaseChange {
    data class AppOpenedSuccess(val appOpenResult: AppUpdateData) : SplashChange()
    object AppOpenedError : SplashChange()
}


sealed class SplashAction : BaseAction {
    object InitApp : SplashAction()

}