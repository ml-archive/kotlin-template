package dk.eboks.app.storage.managers

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.repositories.AppStateRepository
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class AppStateManagerImpl @Inject constructor(val appStateRepository: AppStateRepository) : AppStateManager {
    override var state: AppState? = null

    init {
        Timber.e("Loading previous appstate")
        state = appStateRepository.loadState()
    }

    override fun save() {
        launch(CommonPool) {
            state?.let { appStateRepository.saveState(it) }
        }
    }
}