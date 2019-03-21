package dk.eboks.app.presentation.ui.channels.screens.overview

import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class ChannelOverviewPresenter @Inject constructor() :
    ChannelOverviewContract.Presenter,
    BasePresenterImpl<ChannelOverviewContract.View>() {
    init {
    }
}