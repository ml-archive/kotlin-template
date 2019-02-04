package dk.eboks.app.domain.interactors.sender

import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by Christian on 3/13/2018.
 * @author Christian
 * @since 3/13/2018.
 */
interface GetCollectionsInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(val cached: Boolean)

    interface Output {
        fun onGetCollections(collections: List<CollectionContainer>)
    }
}