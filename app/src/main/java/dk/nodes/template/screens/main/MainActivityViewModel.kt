package dk.nodes.template.screens.main

import androidx.lifecycle.viewModelScope
import dk.nodes.template.nstack.NStackPresenter
import dk.nodes.template.base.BaseViewModel
import dk.nodes.template.util.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val nStackPresenter: dk.nodes.template.nstack.NStackPresenter
) : dk.nodes.template.base.BaseViewModel<MainActivityViewState>(MainActivityViewState()) {

    fun checkNStack() = viewModelScope.launch {
        // Delay popup a bit so it's not super intrusive
        withContext(Dispatchers.IO) { delay(1000) }
        nStackPresenter
                .whenChangelog {
                    state = state.copy(nstackUpdate = SingleEvent(it))
                }.whenMessage {
                    state = state.copy(nstackMessage = SingleEvent(it))
                }.whenRateReminder {
                    state = state.copy(nstackRateReminder = SingleEvent(it))
                }
    }
}