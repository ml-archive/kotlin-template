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

    fun whenMessage(callback: (Message) -> Unit): NStackPresenter {
        appUpdate?.message?.let(callback)
        return this
    }

    fun whenChangelog(callback: (AppUpdate) -> Unit): NStackPresenter {
        if(appUpdate?.update?.state == AppUpdateState.CHANGELOG) {
            callback.invoke(appUpdate?.update ?: return this)
        }
        return this
    }

    fun whenRateReminder(callback: (RateReminder) -> Unit): NStackPresenter {
        appUpdate?.rateReminder?.let(callback)
        return this
    }

}