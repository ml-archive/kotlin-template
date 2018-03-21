package dk.eboks.app.domain.repositories

import dk.eboks.app.domain.models.sender.CollectionContainer

/**
* Created by bison on 01/02/18.
* @author   bison
* @since    01/02/18.
*/
interface CollectionsRepository {
    fun getCollections(cached : Boolean = false) : List<CollectionContainer>
}