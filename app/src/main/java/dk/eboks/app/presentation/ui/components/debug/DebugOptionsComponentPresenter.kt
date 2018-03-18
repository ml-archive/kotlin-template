package dk.eboks.app.presentation.ui.components.debug

import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class DebugOptionsComponentPresenter @Inject constructor(val appState: AppStateManager) : DebugOptionsComponentContract.Presenter, BasePresenterImpl<DebugOptionsComponentContract.View>() {

    init {
    }

    override fun setup() {
        setupConfigurationView()
    }

    override fun setConfig(name : String)
    {
        Config.changeConfig(name)
        Timber.e("Config changed: new current configuration is ${Config.getCurrentConfigName()}")
    }

    private fun setupConfigurationView()
    {
        when(Config.getCurrentConfigName())
        {
            "danish" -> {
                runAction { v->v.showCountrySpinner(0) }
            }
            "swedish" -> {
                runAction { v->v.showCountrySpinner(2) }
            }
            "norwegian" -> {
                runAction { v->v.showCountrySpinner(1) }
            }
            else -> {}
        }
    }

}