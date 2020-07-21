package dk.nodes.template.presentation.ui.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dk.nodes.template.presentation.nstack.NStackPresenter
import dk.nodes.template.presentation.ui.base.BaseViewModel
import dk.nodes.template.presentation.util.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel @ViewModelInject constructor(
    private val nStackPresenter: NStackPresenter,
    @Assisted private val savedState: SavedStateHandle
) : BaseViewModel<MainActivityViewState>(MainActivityViewState()) {

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