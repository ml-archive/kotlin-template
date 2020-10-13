package dk.nodes.template.nstack

import dk.nodes.nstack.kotlin.models.AppOpenData
import dk.nodes.nstack.kotlin.models.AppUpdate
import dk.nodes.nstack.kotlin.models.AppUpdateState
import dk.nodes.nstack.kotlin.models.Message
import dk.nodes.nstack.kotlin.models.RateReminder
import dk.nodes.nstack.kotlin.models.state
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NStackPresenter @Inject constructor() {

    private var appOpenData: AppOpenData? = null

    fun saveAppState(appUpdate: AppOpenData) {
        this.appOpenData = appUpdate
    }

    fun whenMessage(callback: (Message) -> Unit): NStackPresenter {
        appOpenData?.message?.let(callback)
        return this
    }

    fun whenChangelog(callback: (AppUpdate) -> Unit): NStackPresenter {
        if (appOpenData?.update?.state == AppUpdateState.CHANGELOG) {
            callback.invoke(appOpenData?.update ?: return this)
        }
        return this
    }

    fun whenRateReminder(callback: (RateReminder) -> Unit): NStackPresenter {
        appOpenData?.rateReminder?.let(callback)
        return this
    }
}