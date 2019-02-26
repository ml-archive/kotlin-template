package dk.eboks.app.mail.presentation.ui.folder.screens

import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class FolderPresenter @Inject constructor() :
    FolderContract.Presenter,
    BasePresenterImpl<FolderContract.View>() {
    init {
    }
}