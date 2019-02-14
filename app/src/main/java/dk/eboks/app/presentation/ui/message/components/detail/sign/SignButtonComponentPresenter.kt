package dk.eboks.app.presentation.ui.message.components.detail.sign

import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class SignButtonComponentPresenter @Inject constructor() :
    SignButtonComponentContract.Presenter, BasePresenterImpl<SignButtonComponentContract.View>() {

    init {
    }

    override fun sign(msg: Message) {
        runAction { v -> v.startSigning(msg) }
    }
}