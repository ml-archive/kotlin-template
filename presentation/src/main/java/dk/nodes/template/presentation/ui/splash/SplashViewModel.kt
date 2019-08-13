package dk.nodes.template.presentation.ui.splash

import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.models.AppOpenResult
import dk.nodes.template.presentation.nstack.NStackPresenter
import dk.nodes.template.presentation.ui.base.BaseViewModel
import dk.nodes.template.presentation.ui.base.Reducer
import dk.nodes.template.presentation.util.SingleEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class SplashViewModel @Inject constructor(
        private val nStackPresenter: NStackPresenter
) : BaseViewModel<SplashAction, SplashChange, SplashViewState>() {

    override val initState: SplashViewState = SplashViewState(doneLoading = false, nstackUpdateAvailable = null)


    override val reducer: Reducer<SplashViewState, SplashChange> = { state, change ->
        when (change) {
            is SplashChange.AppOpenedError -> state.copy(doneLoading = true)
            is SplashChange.AppOpenedSuccess -> state.copy(
                    doneLoading = true,
                    nstackUpdateAvailable = SingleEvent(change.appOpenResult)
            )
        }
    }

    override fun emitAction(action: SplashAction): Flow<SplashChange> {
        return when (action) {
            is SplashAction.InitApp -> bindInitAction()
        }
    }

    private fun bindInitAction(): Flow<SplashChange> = flow {
        Timber.d("initAppState() - start")
        val appOpenResult = NStack.appOpen()
        // Other API calls that might be needed
        // ...
        // Splash should be shown for min. x milliseconds
        delay(2000)
        Timber.d("initAppState() - end")
        when (appOpenResult) {
            is AppOpenResult.Success -> {
                nStackPresenter.saveAppState(appOpenResult.appUpdateResponse.data)
                emit(SplashChange.AppOpenedSuccess(appOpenResult.appUpdateResponse.data))
            }
            else -> emit(SplashChange.AppOpenedError)
        }
    }
}