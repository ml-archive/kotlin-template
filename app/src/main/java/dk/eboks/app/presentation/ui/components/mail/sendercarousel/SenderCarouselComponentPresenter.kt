package dk.eboks.app.presentation.ui.components.mail.sendercarousel

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class SenderCarouselComponentPresenter @Inject constructor(val appState: AppStateManager) : SenderCarouselComponentContract.Presenter, BasePresenterImpl<SenderCarouselComponentContract.View>() {

    init {
    }

}