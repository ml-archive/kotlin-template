package dk.nodes.template.presentation.ui.main

import androidx.lifecycle.viewModelScope
import dk.nodes.template.presentation.nstack.NStackPresenter
import dk.nodes.template.presentation.ui.base.BaseViewModel
import dk.nodes.template.presentation.ui.base.Reducer
import dk.nodes.template.presentation.ui.sample.Change
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val nStackPresenter: NStackPresenter
) : BaseViewModel<MainAction, MainChange, MainViewState>() {
    override val initState: MainViewState = MainViewState()


    override val reducer: Reducer<MainViewState, MainChange> = { state, change ->
        when(change) {
            is MainChange.Reminder -> state.copy(nstackRateReminder = change.nstackRateReminder)
            is MainChange.Changelog -> state.copy(nstackUpdate = change.nstackUpdate)
            is MainChange.Message -> state.copy(nstackMessage = change.nstackUpdate)
        }
    }


    override fun emitAction(action: MainAction): Flow<MainChange> {
        return when(action) {
            is MainAction.CheckNstackAction -> bindCheckNstackAction()
        }
    }

    private fun bindCheckNstackAction() : Flow<MainChange> = flow {
        // Delay popup a bit so it's not super intrusive
        withContext(Dispatchers.IO) { delay(1000) }
         nStackPresenter
                .whenChangelog {
                  emit(MainChange.Changelog(it))
                }.whenMessage {
                     emit(MainChange.Message(it))
                }.whenRateReminder {
                     emit(MainChange.Reminder(it))
                }
    }
}