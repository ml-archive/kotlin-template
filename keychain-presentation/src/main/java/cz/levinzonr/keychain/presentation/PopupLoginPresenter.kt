package cz.levinzonr.keychain.presentation

import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class PopupLoginPresenter @Inject constructor() : PopupLoginContract.Presenter,
    BasePresenterImpl<PopupLoginContract.View>() {
    init {
    }
}