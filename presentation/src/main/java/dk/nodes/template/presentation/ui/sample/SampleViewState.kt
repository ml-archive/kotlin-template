package dk.nodes.template.presentation.ui.sample

import dk.nodes.template.domain.models.Post
import dk.nodes.template.presentation.util.SingleEvent
import dk.nodes.template.presentation.util.ViewError

data class SampleViewState(
    val posts: List<Post> = emptyList(),
    val viewError: SingleEvent<ViewError>? = null,
    val isLoading: Boolean = false
)