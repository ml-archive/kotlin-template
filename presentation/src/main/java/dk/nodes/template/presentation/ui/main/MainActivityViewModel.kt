package dk.nodes.template.presentation.ui.main

import androidx.lifecycle.viewModelScope
import dk.nodes.template.presentation.nstack.NStackPresenter
import dk.nodes.template.presentation.ui.base.BaseViewModel
import dk.nodes.template.presentation.util.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val nStackPresenter: NStackPresenter
) : BaseViewModel<MainActivityViewState>() {
    override val initState: MainActivityViewState = MainActivityViewState()

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