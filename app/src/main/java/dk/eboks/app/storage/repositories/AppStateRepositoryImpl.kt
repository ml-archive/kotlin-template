package dk.eboks.app.storage.repositories

/**
 * Created by bison on 09/10/17.
 */

import android.content.Context
import com.google.gson.Gson
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.repositories.AppStateRepository
import dk.eboks.app.storage.base.GsonFileStorageRepository
import dk.eboks.app.util.guard
import javax.inject.Inject

/**
 * Created by bison on 01-07-2017.
 */
class AppStateRepositoryImpl @Inject constructor(context: Context, gson: Gson) :
    GsonFileStorageRepository<AppState>(context, gson, "app_state.json"),
    AppStateRepository {
    var appState: AppState? = null

    override fun saveState(state: AppState) {
        save(state)
    }

    override fun loadState(): AppState {
        return try {
            appState.guard { appState = load(AppState::class.java) }
            appState!!
        } catch (e: Exception) {
            AppState()
        }
    }
}