package dk.nodes.template.presentation.ui.splash

import androidx.lifecycle.viewModelScope
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.models.AppOpen
import dk.nodes.nstack.kotlin.models.AppUpdateState
import dk.nodes.nstack.kotlin.models.Result
import dk.nodes.nstack.kotlin.models.state
import dk.nodes.template.presentation.navigation.Route
import dk.nodes.template.presentation.nstack.NStackPresenter
import dk.nodes.template.presentation.ui.base.BaseViewModel
import dk.nodes.template.presentation.ui.main.MainActivity
import dk.nodes.template.presentation.util.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val nStackPresenter: NStackPresenter
) : BaseViewModel<SplashViewState>(
    SplashViewState(nstackUpdateAvailable = null)
) {

    fun initAppState() {
        viewModelScope.launch {
            Timber.d("initAppState() - start")
            val deferredAppOpen = async(Dispatchers.IO) { NStack.appOpen() }
            // Other API calls that might be needed
            // ...
            // Splash should be shown for min. x milliseconds
            val deferredMinDelay = async(Dispatchers.IO) { delay(2000) }

            // Parallel execution, wait on both to finish
            val appOpenResult = deferredAppOpen.await()
            deferredMinDelay.await()

            Timber.d("initAppState() - end")
            state = mapAppOpenResult(appOpenResult)
        }
    }

    private fun mapAppOpenResult(appOpenResult: Result<AppOpen>) : SplashViewState {
        return when (appOpenResult) {
            is Result.Success -> {
                val appData = appOpenResult.value.data
                nStackPresenter.saveAppState(appData)
                state.copy(nstackUpdateAvailable = SingleEvent(appData.update)).also {
                    if (appData.update.state != AppUpdateState.FORCE) navigateToMain()
                }
            }
            else -> state.also { navigateToMain() }
        }
    }

    private fun navigateToMain() {
        navigateTo(Route.Activity(
                clazz = MainActivity::class.java,
                finish = true
        ))
    }
}