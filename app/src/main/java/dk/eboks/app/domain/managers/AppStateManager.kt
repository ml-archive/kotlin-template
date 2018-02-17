package dk.eboks.app.domain.managers

import dk.eboks.app.domain.models.internal.AppState

/**
 * Created by bison on 09-02-2018.
 */
// TODO add instanced/local state concept for views like mail lists of which there can be more than one
interface AppStateManager {
    var state : AppState?
    fun save()
}