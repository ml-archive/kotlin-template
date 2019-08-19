package dk.nodes.template.presentation.ui.sample

import dk.nodes.template.models.Post
import dk.nodes.template.presentation.ui.base.BaseAction
import dk.nodes.template.presentation.ui.base.BaseChange
import dk.nodes.template.presentation.ui.base.BaseState
import dk.nodes.template.presentation.util.SingleEvent
import dk.nodes.template.presentation.util.ViewError

data class SampleViewState(
        val posts: List<Post> = emptyList(),
        val viewError: SingleEvent<ViewError>? = null,
        val isLoading: Boolean = false
) : BaseState

sealed class Change : BaseChange {
    data class PostsLoadingError(val throwable: Throwable) : Change()
    data class PostsLoaded(val posts: List<Post>) : Change()
    object PostsLoading : Change()
}


sealed class Action : BaseAction {
    object LoadPosts : Action()
}