package dk.eboks.app.domain.repositories

import dk.eboks.app.domain.models.AppState

/**
 * Created by bison on 09/10/17.
 */
interface AppStateRepository {
    fun saveState(state: AppState)
    fun loadState(): AppState
}