package dk.nodes.template.presentation.ui.main

import dk.nodes.nstack.kotlin.models.AppUpdate
import dk.nodes.nstack.kotlin.models.Message
import dk.nodes.nstack.kotlin.models.RateReminder
import dk.nodes.template.presentation.ui.base.BaseAction
import dk.nodes.template.presentation.ui.base.BaseChange
import dk.nodes.template.presentation.ui.base.BaseState
import dk.nodes.template.presentation.util.SingleEvent

data class MainViewState(
    val errorMessage: SingleEvent<String>? = null,
    val isLoading: Boolean = false,
    val nstackMessage: Message? = null,
    val nstackRateReminder: RateReminder? = null,
    val nstackUpdate: AppUpdate? = null
) : BaseState



sealed class MainAction : BaseAction {
    object CheckNstackAction: MainAction()
}

sealed class MainChange: BaseChange {
    data class Changelog(val nstackUpdate: AppUpdate?) : MainChange()
    data class Reminder(val nstackRateReminder: RateReminder?) : MainChange()
    data class Message(val nstackUpdate: dk.nodes.nstack.kotlin.models.Message?) : MainChange()

}