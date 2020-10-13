package dk.nodes.template.screens.splash

import androidx.lifecycle.viewModelScope
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.models.Result
import dk.nodes.template.nstack.NStackPresenter
import dk.nodes.template.base.BaseViewModel
import dk.nodes.template.util.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val nStackPresenter: dk.nodes.template.nstack.NStackPresenter
) : dk.nodes.template.base.BaseViewModel<SplashViewState>(
    SplashViewState(
        doneLoading = false,
        nstackUpdateAvailable = null
    )
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
            state = when (appOpenResult) {
                is Result.Success -> {
                    nStackPresenter.saveAppState(appOpenResult.value.data)
                    state.copy(
                        doneLoading = true,
                        nstackUpdateAvailable = SingleEvent(appOpenResult.value.data.update)
                    )
                }
                else -> state.copy(doneLoading = true)
            }
        }
    }
}