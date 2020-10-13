package dk.nodes.template.screens.sample

import androidx.lifecycle.viewModelScope
import dk.nodes.template.core.interactors.base.Fail
import dk.nodes.template.core.interactors.base.InteractorResult
import dk.nodes.template.core.interactors.base.Loading
import com.test.common.interactors.posts.PostFlowInteractor
import dk.nodes.template.core.interactors.base.Success
import dk.nodes.template.core.interactors.SwitchThemeInteractor
import dk.nodes.template.core.interactors.base.asResult
import dk.nodes.template.core.interactors.base.invoke
import dk.nodes.template.core.interactors.base.runInteractor
import dk.nodes.template.base.BaseViewModel
import dk.nodes.template.util.SingleEvent
import dk.nodes.template.util.ThemeHelper
import dk.nodes.template.util.ViewErrorController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SampleViewModel @Inject constructor(
        private val postsInteractor: com.test.common.interactors.posts.PostFlowInteractor,
        private val switchThemeInteractor: dk.nodes.template.core.interactors.SwitchThemeInteractor
) : dk.nodes.template.base.BaseViewModel<SampleViewState>(SampleViewState()) {

    init {
        viewModelScope.launch {
            postsInteractor.flow().collect {
                state = state.copy(posts = it)
            }
        }
    }

    fun fetchPosts() {
        viewModelScope.launch(Dispatchers.Main) {
            state = mapResult(dk.nodes.template.core.interactors.base.Loading())
            val result = dk.nodes.template.core.interactors.base.runInteractor(postsInteractor.asResult())
            state = mapResult(result)
        }
    }

    fun switchTheme() {
        viewModelScope.launch {
            ThemeHelper.applyTheme(switchThemeInteractor())
        }
    }

    private fun mapResult(result: dk.nodes.template.core.interactors.base.InteractorResult<Unit>): SampleViewState {
        return when (result) {
            is dk.nodes.template.core.interactors.base.Success -> state.copy(isLoading = false)
            is dk.nodes.template.core.interactors.base.Loading -> state.copy(isLoading = true)
            is dk.nodes.template.core.interactors.base.Fail -> state.copy(
                viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                isLoading = false
            )
            else -> SampleViewState()
        }
    }
}