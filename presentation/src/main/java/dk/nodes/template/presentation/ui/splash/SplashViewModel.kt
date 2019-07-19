package dk.nodes.template.presentation.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.models.AppOpenResult
import dk.nodes.nstack.kotlin.models.AppUpdateData
import dk.nodes.nstack.kotlin.models.AppUpdateState
import dk.nodes.template.presentation.nstack.NStackPresenter
import dk.nodes.template.presentation.ui.base.BaseViewModel
import dk.nodes.template.presentation.ui.main.*
import dk.nodes.template.presentation.ui.sample.SampleViewState
import dk.nodes.template.presentation.util.SingleEvent
import io.reactivex.Single
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

class SplashViewModel@Inject constructor(
    private val nStackPresenter: NStackPresenter
) : BaseViewModel<SplashViewState>() {

    override val initState: SplashViewState = SplashViewState(doneLoading = false, nstackUpdateAvailable = null)

    fun initAppState() = viewModelScope.launch {
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
        state = when(appOpenResult) {
            is AppOpenResult.Success -> {
                nStackPresenter.saveAppState(appOpenResult.appUpdateResponse.data)
                state.copy(doneLoading = true, nstackUpdateAvailable = SingleEvent(appOpenResult.appUpdateResponse.data))
            }
            else -> state.copy(doneLoading = true)
        }
    }

}