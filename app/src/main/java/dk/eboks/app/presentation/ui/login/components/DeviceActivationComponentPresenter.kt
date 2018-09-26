package dk.eboks.app.presentation.ui.login.components

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.CryptoManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class DeviceActivationComponentPresenter @Inject constructor(
        val appState: AppStateManager
) :
        DeviceActivationComponentContract.Presenter,
        BasePresenterImpl<DeviceActivationComponentContract.View>()
{

    init {

    }

}