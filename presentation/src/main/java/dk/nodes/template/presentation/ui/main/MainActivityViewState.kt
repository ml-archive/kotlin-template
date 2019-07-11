package dk.nodes.template.presentation.ui.main

import dk.nodes.template.presentation.util.SingleEvent

data class MainActivityViewState(
    val errorMessage: SingleEvent<String>? = null,
    val isLoading: Boolean = false
)