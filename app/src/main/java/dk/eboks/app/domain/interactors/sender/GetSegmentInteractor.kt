package dk.eboks.app.domain.interactors.sender

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Segment
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by chnt on 01/02/18.
 * @author chnt
 * @since 01/02/18.
 */
interface GetSegmentInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(val id: Long)

    interface Output {
        fun onGetSegment(segment: Segment)
        fun onError(error: ViewError)
    }
}