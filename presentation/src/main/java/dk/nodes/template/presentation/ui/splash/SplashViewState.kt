package dk.nodes.template.presentation.ui.splash

import dk.nodes.nstack.kotlin.models.AppUpdate
import dk.nodes.template.presentation.util.SingleEvent

data class SplashViewState(
    val nstackUpdateAvailable: SingleEvent<AppUpdate>?
)