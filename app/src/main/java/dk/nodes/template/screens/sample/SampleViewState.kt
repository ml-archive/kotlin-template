package dk.nodes.template.screens.sample

import dk.nodes.template.core.entities.Post
import dk.nodes.template.util.SingleEvent
import dk.nodes.template.util.ViewError

data class SampleViewState(
        val posts: List<dk.nodes.template.core.entities.Post> = emptyList(),
        val viewError: SingleEvent<ViewError>? = null,
        val isLoading: Boolean = false
)