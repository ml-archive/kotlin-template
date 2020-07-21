package dk.nodes.template.presentation.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.models.Result
import dk.nodes.template.presentation.nstack.NStackPresenter
import dk.nodes.template.presentation.ui.base.BaseViewModel
import dk.nodes.template.presentation.util.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashViewModel @ViewModelInject constructor(
    private val nStackPresenter: NStackPresenter
) : BaseViewModel<SplashViewState>(
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