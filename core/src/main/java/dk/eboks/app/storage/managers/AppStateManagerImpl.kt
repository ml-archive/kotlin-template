package dk.eboks.app.storage.managers

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.repositories.AppStateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
internal class AppStateManagerImpl @Inject constructor(private val appStateRepository: AppStateRepository) :
        AppStateManager {
    override var state: AppState? = null

    init {
        Timber.e("Loading previous appstate")
        state = appStateRepository.loadState()
    }

    override fun save() {
        GlobalScope.launch(Dispatchers.Default) {
            state?.let { appStateRepository.saveState(it) }
        }
    }
}