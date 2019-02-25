package dk.eboks.app.presentation.ui.debug.components

import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.managers.PrefManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class DebugOptionsComponentPresenter @Inject constructor(
    private val prefManager: PrefManager,
    private val appConfig: AppConfig
) : DebugOptionsComponentContract.Presenter,
    BasePresenterImpl<DebugOptionsComponentContract.View>() {

    init {
    }

    override fun setup() {
        setupConfigurationView()
        runAction { v ->
            v.showEnvironmentSpinner(
                appConfig.currentMode.environments,
                appConfig.currentMode.environment
            )
        }
    }

    override fun setConfig(name: String) {
        appConfig.changeConfig(name)
        Timber.e("appConfig changed: new current configuration is ${appConfig.currentConfigName}")
        runAction { v ->
            v.showEnvironmentSpinner(
                appConfig.currentMode.environments,
                appConfig.currentMode.environment
            )
        }
        prefManager.setString("config", name)
        for ((key, env) in appConfig.currentMode.environments) {
            if (env == appConfig.currentMode.environment) {
                prefManager.setString("environment", key)
            }
        }
    }

    override fun setEnvironment(name: String) {
        appConfig.changeEnvironment(name)
        Timber.e("Environment changed to $name")
        prefManager.setString("environment", name)
    }

    private fun setupConfigurationView() {
        when (appConfig.currentConfigName) {
            "danish" -> {
                runAction { v -> v.showCountrySpinner(0) }
            }
            "swedish" -> {
                runAction { v -> v.showCountrySpinner(2) }
            }
            "norwegian" -> {
                runAction { v -> v.showCountrySpinner(1) }
            }
            else -> {
            }
        }
    }
}