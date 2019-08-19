package dk.nodes.template.presentation.nstack

import dk.nodes.nstack.kotlin.models.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NStackPresenter @Inject constructor() {

    private var appUpdate: AppUpdateData? = null

    fun saveAppState(appUpdate: AppUpdateData) {
        this.appUpdate = appUpdate
    }

    suspend fun whenMessage(callback: suspend (Message) -> Unit): NStackPresenter {
        appUpdate?.message?.let { callback(it) }
        return this
    }

    suspend fun whenChangelog(callback:  suspend (AppUpdate) -> Unit): NStackPresenter {
        if (appUpdate?.update?.state == AppUpdateState.CHANGELOG) {
            callback.invoke(appUpdate?.update ?: return this)
        }
        return this
    }

    suspend fun whenRateReminder(callback: suspend (RateReminder) -> Unit): NStackPresenter {
        appUpdate?.rateReminder?.let { callback(it) }
        return this
    }
}