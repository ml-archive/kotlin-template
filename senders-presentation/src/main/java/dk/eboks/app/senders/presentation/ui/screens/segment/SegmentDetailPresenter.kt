package dk.eboks.app.senders.presentation.ui.screens.segment

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.domain.senders.interactors.GetSegmentInteractor
import dk.eboks.app.domain.senders.interactors.register.RegisterInteractor
import dk.eboks.app.domain.senders.interactors.register.UnRegisterInteractor
import dk.eboks.app.senders.presentation.ui.screens.segment.SegmentDetailContract
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 * @author bison
 * @since 20-05-2017.
 */
internal class SegmentDetailPresenter @Inject constructor(
    private val getSegmentInteractor: GetSegmentInteractor,
    private val registerInteractor: RegisterInteractor,
    private val unregisterInteractor: UnRegisterInteractor
) :
    SegmentDetailContract.Presenter, BasePresenterImpl<SegmentDetailContract.View>(),
    GetSegmentInteractor.Output,
    RegisterInteractor.Output,
    UnRegisterInteractor.Output {

    init {
        getSegmentInteractor.output = this
        registerInteractor.output = this
        unregisterInteractor.output = this
    }

    override fun onGetSegment(segment: Segment) {
        runAction { v ->
            v.showSegment(segment)
        }
    }

    override fun loadSegment(id: Long) {
        getSegmentInteractor.input = GetSegmentInteractor.Input(id)
        getSegmentInteractor.run()
    }

    override fun registerSegment(id: Long) {
        registerInteractor.inputSegment = RegisterInteractor.InputSegment(id)
        registerInteractor.run()
    }

    override fun unregisterSegment(id: Long) {
        unregisterInteractor.inputSegment = UnRegisterInteractor.InputSegment(id)
        unregisterInteractor.run()
    }

    override fun onSuccess() {
        Timber.i("Success")
        runAction { v ->
            v.showSuccess()
        }
    }

    override fun onError(error: ViewError) {
        runAction { v ->
            v.showError(error.message ?: "")
        }
    }
}