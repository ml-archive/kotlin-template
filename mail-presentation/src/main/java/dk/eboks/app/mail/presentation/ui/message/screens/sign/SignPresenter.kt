package dk.eboks.app.mail.presentation.ui.message.screens.sign

import dk.eboks.app.mail.domain.interactors.message.GetSignLinkInteractor
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.shared.Link
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by Christian on 5/23/2018.
 * @author Christian
 * @since 5/23/2018.
 */
internal class SignPresenter @Inject constructor(
    private val getSignLinkInteractor: GetSignLinkInteractor
) :
    SignContract.Presenter,
    BasePresenterImpl<SignContract.View>(),
    GetSignLinkInteractor.Output {
    init {
        getSignLinkInteractor.output = this
    }

    override fun setup(msg: Message) {
        getSignLinkInteractor.input = GetSignLinkInteractor.Input(msg)
        getSignLinkInteractor.run()
    }

    /**
     * GetSignLinkInteractor callbacks
     */
    override fun onGetSignLink(result: Link) {
        view { loadUrl(result.url) }
    }

    override fun onGetSignLinkError(error: ViewError) {
        view { showErrorDialog(error) }
    }
}