package dk.nodes.template.screens.splash

import dk.nodes.nstack.kotlin.models.AppUpdate
import dk.nodes.template.util.SingleEvent

data class SplashViewState(
    val doneLoading: Boolean,
    val nstackUpdateAvailable: SingleEvent<AppUpdate>?
)