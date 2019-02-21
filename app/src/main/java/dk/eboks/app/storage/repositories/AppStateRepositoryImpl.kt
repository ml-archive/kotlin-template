package dk.eboks.app.storage.repositories

/**
 * Created by bison on 09/10/17.
 */

import android.content.Context
import com.google.gson.Gson
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.repositories.AppStateRepository
import dk.eboks.app.storage.base.GsonFileStorageRepository

/**
 * Created by bison on 01-07-2017.
 */
class AppStateRepositoryImpl(context: Context, gson: Gson) :
    GsonFileStorageRepository<AppState>(context, gson, "app_state.json"),
    AppStateRepository {
    var appState: AppState? = null

    override fun saveState(state: AppState) {
        save(state)
    }

    override fun loadState(): AppState {
        try {
            if (appState == null)
                appState = load(AppState::class.java)
            return appState!!
        } catch (e: Exception) {
            return AppState()
        }
    }
}